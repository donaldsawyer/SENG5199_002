angular.module('app').controller('searchController', function ($scope, $location, $http, authService, accountService) {
    if(!authService.isLoggedIn())
        $location.path("/home");

    $scope.message = "Search tweets";
    $scope.notFound = "";
    $scope.errorMessage = "";
    $scope.pageStatus = "";

    $scope.auth = {};
    $scope.auth.token = authService.getToken();
    $scope.auth.username = authService.getUsername();
    $scope.account = accountService.getAccount();

    $scope.doSearch = function () {
        $http.get('/message/search',
            {
                params: {handle: $scope.search.tweeterHandle, text: $scope.search.tweetContent},
                headers: {'X-Auth-Token': authService.getToken().toString()}
            })
            .success(function (data) {
                if(data.length == 0) {
                    $scope.notFound = "No tweets found"
                }
                else {
                    $scope.notFound = "";
                    accountService.setAccount(data);
                    $scope.account = accountService.getAccount();
                }
            })
            .error(function (error) {
                $scope.searchError = "Error searching for tweets"
            })
            .finally(function () {
                $scope.account = accountService.getAccount();
                $scope.pageStatus = 'Page load complete';
                $location.path("/search");
            })
    };

});
