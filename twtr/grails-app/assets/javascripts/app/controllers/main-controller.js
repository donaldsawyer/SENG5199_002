angular.module('app').controller('mainController', function ($scope, $location, $http, authService) {
    $scope.auth = new Object();
    $scope.auth.token = authService.getToken();
    $scope.auth.username = authService.getUsername();
    $scope.isLoggedIn = authService.isLoggedIn();
    $scope.logoutHappened = false;

    $scope.doLogin = function() {

        var credentials = new Object();
        credentials.username = $scope.auth.username;
        credentials.password = $scope.auth.password;

        var jsonString= JSON.stringify(credentials);

        authService.setToken({});
        $scope.isLoggedIn = authService.isLoggedIn();

        //TODO: Refactor this into the authService
        $http.post('/api/login', credentials)
            .success(function (data) {
                authService.setUsername(data.username);
                authService.setToken(data.access_token);
                $scope.logoutHappened = false;
                $scope.errorMessage = {};
            })
            .error(function (error) {
                $scope.auth.authError = error;
                $scope.errorMessage = "Invalid Login";
            })
            .finally(function () {
                $scope.auth.token = authService.getToken();
                $scope.auth.username = authService.getUsername();
                $scope.isLoggedIn = authService.isLoggedIn();
                $location.path("/home");
            });
    };

    $scope.doLogout = function() {

        //TODO: logout api doesn't work from web, but works from postman.  might need to fix this.
        //      for now, just clear out the token and let it expire.
        //$http.post('/api/logout',
        //    {headers: {'X-Auth-Token': authService.getToken().toString()}})
        //    .success( function(data) {
        //        authService.logout();
        //        $scope.auth = null;
        //        $scope.logoutHappened = true;
        //        $scope.isLoggedIn = false;
        //    })
        //    .error( function(error) {
        //        //TODO: error handling
        //    })
        //    .finally( function() {
        //        $location.path("/home");
        //    })
        authService.logout();
        $scope.auth = null;
        $scope.logoutHappened = true;
        $scope.isLoggedIn = false;
    };
});
