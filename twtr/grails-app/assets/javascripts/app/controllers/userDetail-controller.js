angular.module('app').controller('userDetailController', function($scope, $location, $http, authService, accountService) {
    $scope.message = "User Detail Controller";

    $scope.auth = {};
    $scope.auth.token = authService.getToken();
    $scope.auth.username = authService.getUsername();
    $scope.account = accountService.getAccount();
    //$scope.myDetail = $scope.auth.username == $scope.account.handle;

    var qs = $location.search();
    if(!qs['handle']) {
        $scope.viewHandle = $scope.auth.username;
    }
    else {
        $scope.viewHandle = qs['handle'];
    }


    $scope.getAccount = function() {
        $http.get('/account/handle/' + $scope.viewHandle,
            {headers: {'X-Auth-Token': authService.getToken().toString()}})
            .success(function (data) {
                accountService.setAccount(data);
                $scope.account = accountService.getAccount();
                $scope.getTweets();
                $scope.isFollower();
            })
            .error(function (error) {
                //TODO: error handling
            })
            .finally( function() {

                $scope.myDetail = $scope.auth.username == $scope.account.handle;
                $location.path("/userDetail");
            })
    };

    $scope.getTweets = function() {
        $http.get('/accounts/' + $scope.account.id + '/messages',
            {headers: {'X-Auth-Token': authService.getToken().toString()}})
            .success(function(data) {
                accountService.setTweets(data);
            })
            .error(function (error) {
                //TODO: error handling
            })
            .finally( function() {
                $scope.account.tweets = accountService.getTweets();
                $location.path("/userDetail");
            })
    };

    $scope.isFollower = function() {
        $http.get('/accounts/' + $scope.account.id + '/followers',
            {headers: {'X-Auth-Token': authService.getToken().toString()}})
            .success(function(data) {
                accountService.setFollowers(data);
                $scope.account.followers = data;
            })
            .error(function(error) {
                //TODO: error handling
            })
            .finally( function() {
                $scope.account.amIfollowing = accountService.isFollower($scope.auth.username);
                $location.path("/userDetail");
            })
    };

    $scope.saveDetails = function() {
        var updates = new Object();
        updates.displayName = $scope.account.displayName;
        updates.emailAddress = $scope.account.emailAddress;

        var jsonString = JSON.stringify(updates);

        $http.put('/accounts/' + $scope.account.id, updates,
            { headers: {'X-Auth-Token': authService.getToken().toString()}})
            .success( function(data) {

            })
            .error( function(error) {
                //TODO: add error handling
            })
            .finally (function() {
                $scope.getAccount();
            })
    };

    $scope.startFollowing = function() {

    }
});
