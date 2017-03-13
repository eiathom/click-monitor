require(['jquery', 'underscore', 'backbone'], function($, _, Backbone) {
    var AppRouter = Backbone.Router.extend({
        routes: {
            "": "main"
        },
        main: function() {
            $("#content").html("<h1>hello world!</h1>").show();
        }
    });
    var router = new AppRouter();
    Backbone.history.start();
});