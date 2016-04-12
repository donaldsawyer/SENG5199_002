angular.module('app').controller('mainController', function ($scope, $location, $http, authService) {
    $scope.auth = new Object();
    $scope.auth.token = authService.getToken();
    $scope.auth.username = authService.getUsername();
    $scope.isLoggedIn = authService.isLoggedIn();
    $scope.logoutHappened = false;
    $scope.pageStatus = "";

    $scope.doLogin = function () {

        var credentials = new Object();
        credentials.username = $scope.auth.username;
        credentials.password = $scope.auth.password;

        var jsonString = JSON.stringify(credentials);

        authService.setToken({});
        $scope.isLoggedIn = authService.isLoggedIn();

        $scope.pageStatus = "";

        $http.post('/api/login', credentials)
            .success(function (data) {
                authService.setUsername(data.username);
                authService.setToken(data.access_token);
                $scope.logoutHappened = false;
                $scope.errorMessage = {};
            })
            .error(function (error) {
                authService.logout();
                $scope.auth.authError = error;
                $scope.errorMessage = "Username and Password do not match a valid user.";
            })
            .finally(function () {
                $scope.auth.password = null;
                $scope.auth.token = authService.getToken();
                $scope.auth.username = authService.getUsername();
                $scope.isLoggedIn = authService.isLoggedIn();
                $scope.pageStatus = 'Page load complete';
                $location.path("/home");
            });

    };

    $scope.doLogout = function () {
        $scope.pageStatus = "";
        authService.logout();
        $scope.auth = null;
        $scope.logoutHappened = true;
        $scope.isLoggedIn = false;
        $scope.pageStatus = 'Page load complete';
    };

    $scope.pageStatus = "Page load complete";
});
