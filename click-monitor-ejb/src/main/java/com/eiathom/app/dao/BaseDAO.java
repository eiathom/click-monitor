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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eiathom.app.datasource.DataSourceConnection;
import com.eiathom.app.entity.Entity;

/**
 * This base DAO class defines common methods an extending class should use
 * 
 * @author eiathom
 *
 */
public abstract class BaseDAO<E extends Entity> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseDAO.class);

    protected Connection getDataSourceConnection() {
        return DataSourceConnection.getInstance().getDatabaseConnection();
    }

    protected abstract List<E> findAll();

    protected abstract E find(final Long id);

    protected abstract E update(final E entity);

    protected abstract E create(final E entity);

    protected abstract E processRow(final ResultSet queryResult) throws SQLException;

}
