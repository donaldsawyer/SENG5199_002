app.config(function ($routeProvider) {
    $routeProvider
        .when('/home', {
            templateUrl: '/app/home.htm',
            controller: 'mainController'
        })
        .when('/search', {
            templateUrl: '/app/search.htm',
            controller: 'searchController'
        })
        .when('/userDetail', {
            templateUrl: '/app/userDetail.htm',
            controller: 'userDetailController'
        })
        .otherwise({
            redirectTo: '/home'
        });
});