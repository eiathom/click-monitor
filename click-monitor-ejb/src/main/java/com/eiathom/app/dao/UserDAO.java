/**
Copyright (C) 2017 eiathom. 
All rights reserved. Copyright (C) 2017 eiathom.
Permission is hereby granted, free of charge, 
to any person obtaining a copy of this software and associated 
documentation files (the "Software"), to deal in the Software without restriction, 
including without limitation the rights to use, copy, modify, merge, publish, 
distribute, sublicense, and/or sell copies of the Software, 
and to permit persons to whom the Software is furnished to do so, 
subject to the following conditions:
The above copyright notice and this permission notice shall be included 
in all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
IN THE SOFTWARE.
 */
package com.eiathom.app.dao;

import static com.eiathom.app.query.QueryHelper.SELECT_FROM_AND_ORDER_BY;
import static com.eiathom.app.query.TableAndColumnNames.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.eiathom.app.entity.User;
import com.eiathom.app.exception.DatabaseConnectionException;

/**
 * DAO for accessing User data from the database
 * 
 * @author eiathom
 *
 */
public class UserDAO extends BaseDAO<User> {

    private static final String UPDATE_USER_SET_QUERY = "UPDATE User SET name=?, votes=? WHERE id=?";
    private static final String GET_USERS_QUERY = String.format(SELECT_FROM_AND_ORDER_BY, User.class.getSimpleName(), ID_COLUMN);
    private static final String GET_USER_QUERY = String.format("SELECT * FROM %s WHERE id = ", User.class.getSimpleName());
    private static final String INSERT_INTO_USER_QUERY = "INSERT INTO User (name, votes) VALUES (?, ?)";

    @Override
    public List<User> findAll() {
        ResultSet queryResult = null;
        final List<User> users = new ArrayList<>(1);
        try {
            queryResult = super.getDataSourceConnection().createStatement().executeQuery(GET_USERS_QUERY);
            while (queryResult.next()) {
                users.add(processRow(queryResult));
            }
        } catch (final SQLException | DatabaseConnectionException e) {
            LOGGER.error("could not access database table using: '{}' because : {}", GET_USERS_QUERY, e.getMessage());
        } finally {
            try {
                if (queryResult != null) {
                    queryResult.close();
                }
            } catch (final SQLException exception) {
                LOGGER.error("sql error while getting users from the database", exception);
            }
        }
        LOGGER.info("users from '{}' query: '{}'", GET_USERS_QUERY, users);
        return users;
    }

    @Override
    public User find(final Long id) {
        ResultSet queryResult = null;
        User user = null;
        try {
            queryResult = super.getDataSourceConnection().createStatement().executeQuery(GET_USER_QUERY + id);
            while (queryResult.next()) {
                user = processRow(queryResult);
                return user;
            }
        } catch (final SQLException | DatabaseConnectionException e) {
            LOGGER.error("could not access database table using: '{}' because : {}", GET_USERS_QUERY, e.getMessage());
        } finally {
            try {
                if (queryResult != null) {
                    queryResult.close();
                }
            } catch (final SQLException exception) {
                LOGGER.error("sql error while getting users from the database", exception);
            }
        }
        return user;
    }

    public User save(final User user) {
        return user.getId() > 0 ? update(user) : create(user);
    }

    @Override
    public User update(final User user) {
        PreparedStatement preparedStatement = null;
        try {
            LOGGER.info("UPDATE {}", user);
            preparedStatement = super.getDataSourceConnection().prepareStatement(UPDATE_USER_SET_QUERY);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setInt(2, user.getVotes());
            preparedStatement.setLong(3, user.getId());
            preparedStatement.executeUpdate();
        } catch (final SQLException exception) {
            LOGGER.error("error while trying to perform update on the database", exception);
        } finally {
            try {
                super.getDataSourceConnection().commit();
                if (preparedStatement != null) {
                    preparedStatement.closeOnCompletion();
                    preparedStatement.close();
                }
            } catch (final SQLException exception) {
                LOGGER.error("sql error while updating users in the database", exception);
            }
        }
        return user;
    }

    @Override
    public User create(final User user) {
        PreparedStatement preparedStatement = null;
        ResultSet queryResult = null;
        try {
            preparedStatement = super.getDataSourceConnection().prepareStatement(INSERT_INTO_USER_QUERY, new String[] { "ID" });
            preparedStatement.setString(1, user.getName());
            preparedStatement.setInt(2, user.getVotes());
            preparedStatement.executeUpdate();
            queryResult = preparedStatement.getGeneratedKeys();
            queryResult.next();
            user.setId(queryResult.getLong(1));
            LOGGER.info("saving new user: '{}'", user);
        } catch (final DatabaseConnectionException | SQLException exception) {
            LOGGER.error("could not access database table using: '{}' because : {}", INSERT_INTO_USER_QUERY, exception.getMessage());
        } finally {
            try {
                super.getDataSourceConnection().commit();
                if (preparedStatement != null) {
                    preparedStatement.closeOnCompletion();
                    preparedStatement.close();
                }
                if (queryResult != null) {
                    queryResult.close();
                }
            } catch (final SQLException exception) {
                LOGGER.error("sql error while adding users to the database", exception);
            }
        }
        return user;
    }

    @Override
    public User processRow(final ResultSet queryResult) throws SQLException {
        final User user = new User();
        user.setId(queryResult.getLong(ID_COLUMN));
        user.setName(queryResult.getString(NAME_COLUMN));
        user.setVotes(queryResult.getInt(VOTES_COLUMN));
        return user;
    }

}
