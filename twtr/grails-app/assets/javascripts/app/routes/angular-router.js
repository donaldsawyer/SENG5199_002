app.config(function ($routeProvider) {
    $routeProvider
        .when('/home', {
            templateUrl: 'twtr/partials/home.html',
            controller: 'welcomeController'
        })
        //.when('/about', {
        //    templateUrl: 'angular-router/partials/about.html',
        //    controller: 'aboutController'
        //})
        //.when('/contact', {
        //    templateUrl: 'angular-router/partials/contact.html',
        //    controller: 'contactController'
        //})
        //.when('/attendee/:action?/:id?', {
        //    templateUrl: 'angular-router/partials/attendee.html'
        //})
        .otherwise({
            redirectTo: '/home'
        });
});
