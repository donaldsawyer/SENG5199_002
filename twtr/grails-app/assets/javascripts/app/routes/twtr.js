app.config(function ($routeProvider) {
    $routeProvider
        .when('/home', {
            templateUrl: '/app/home.htm',
            controller: 'mainController'
        })
        .when('/about', {
            templateUrl: 'twtr/partials/about.html',
            controller: 'aboutController'
        })
        .when('/contact', {
            templateUrl: 'twtr/partials/contact.html',
            controller: 'contactController'
        })
        .when('/attendee/:action?/:id?', {
            templateUrl: 'twtr/partials/attendee.html'
        })
        .otherwise({
            redirectTo: '/home'
        });
});