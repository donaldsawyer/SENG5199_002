angular.module('app').controller('searchController', function ($scope, $location, $http, authService, accountService) {
    if(!authService.isLoggedIn())
        $location.path("/home");

    $scope.message = "Search tweets";

    $scope.auth = {};
    $scope.auth.token = authService.getToken();
    $scope.auth.username = authService.getUsername();
    $scope.account = accountService.getAccount();

    $scope.doSearch = function () {
        $http.get('/message/search',
            {
                params: {handle: $scope.search.tweeterHandle, text: $scope.search.tweetMessage},
                headers: {'X-Auth-Token': authService.getToken().toString()}
            })
            .success(function (data) {
                accountService.setAccount(data);
                $scope.account = accountService.getAccount();
            })
            .error(function (error) {
                $scope.notFound = "No tweets found"
            })
            .finally(function () {
                $scope.account = accountService.getAccount();
                $location.path("/search");
            })
    };

});
