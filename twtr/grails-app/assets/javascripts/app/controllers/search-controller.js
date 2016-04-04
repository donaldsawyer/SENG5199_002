angular.module('app').controller('searchController', function ($scope, $location, $http, authService) {
    if(!authService.isLoggedIn())
        $location.path("/home");

    $scope.message = "Search Controller";

    $scope.auth = {};
    $scope.auth.token = authService.getToken();
});

