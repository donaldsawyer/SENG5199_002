angular.module('app').controller('userDetailController', function($scope, $location, $http, authService, accountService) {
    if(!authService.isLoggedIn())
        $location.path("/home");

    $scope.message = "User Detail Controller";

    $scope.auth = {};
    $scope.auth.token = authService.getToken();
    $scope.auth.username = authService.getUsername();
    $scope.account = accountService.getAccount();

    $scope.pageStatus = "";

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
                $scope.myDetail = ($scope.auth.username == $scope.account.handle);
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

    $scope.getAuthAccount = function() {
        $http.get('/account/handle/' + $scope.auth.username,
            {headers: {'X-Auth-Token': authService.getToken().toString()}})
            .success( function(data) {
                authService.setAccount(data);
            })
            .error( function(error) {
                //TODO: error handling
            })
            .finally( function() {
                $scope.auth.account = authService.getAccount();
                $location.path("/userDetail");
            })
    };

    $scope.startFollowing = function() {
        $scope.pageStatus = "";
        $http.post('/accounts/' + $scope.auth.account.id + '/startFollowing?followAccount=' + $scope.account.id,
            {headers: {'X-Auth-Token': authService.getToken().toString()}})
            .success( function(data) {
                $scope.getAccount();
            })
            .error( function(error) {
                //TODO: error handling
            })
            .finally( function() {
                $scope.pageStatus = "Page load complete";
                $location.path("/userDetail");
            })
    };

    $scope.saveDetails = function() {
        var updates = new Object();
        updates.displayName = $scope.account.displayName;
        updates.emailAddress = $scope.account.emailAddress;

        $scope.pageStatus = "";

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
                $scope.getAuthAccount();
                $scope.pageStatus = "Page load complete";
                $location.path("/userDetail");
            })
    };

    if($scope.auth.account == null)
        $scope.getAuthAccount();

    $scope.pageStatus = "Page load complete";
});
