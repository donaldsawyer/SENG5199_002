angular.module('app').controller('searchController', function ($scope, $location, $http, authService, accountService) {

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

    $scope.goToUserDetails = function() {
        //TBD - navigate to user details page based on the handle
        if($scope.testMessage == "Test")
        {
            $scope.testMessage = "Toggle Test";
        }
        else {
            $scope.testMessage = "Test";
        }
    };
});

