var require = {
        baseUrl: "js/",
        paths: {
            jquery: 'lib/jquery-3.1.1',
            underscore: 'lib/underscore',
            backbone: 'lib/backbone'
        },
        shim: {
            underscore: {
                exports: "_"
            },
            backbone: {
                deps: ['underscore', 'jquery'],
                exports: 'Backbone'
            }
        }
};