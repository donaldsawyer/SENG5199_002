angular.module('app').controller('userDetailController', function ($scope, $location, $http, authService, accountService) {
    if (!authService.isLoggedIn())
        $location.path("/home");

    $scope.message = "User Detail Controller";

    $scope.auth = {};
    $scope.auth.token = authService.getToken();
    $scope.auth.username = authService.getUsername();
    $scope.account = accountService.getAccount();

    $scope.pageStatus = "";

    var qs = $location.search();
    if (!qs['handle']) {
        $scope.viewHandle = $scope.auth.username;
    }
    else {
        $scope.viewHandle = qs['handle'];
    }

    $scope.getAccount = function () {
        $scope.getAccountError = "";
        $http.get('/account/handle/' + $scope.viewHandle,
            {headers: {'X-Auth-Token': authService.getToken().toString()}})
            .success(function (data) {
                accountService.setAccount(data);
                $scope.account = accountService.getAccount();
                $scope.getTweets();
                $scope.isFollower();
                $scope.getAccountError = "";
            })
            .error(function (error) {
                $scope.getAccountError = "Get account error, please try again."
            })
            .finally(function () {
                $scope.myDetail = ($scope.auth.username == $scope.account.handle);
                $location.path("/userDetail");
            })
    };

    $scope.getTweets = function () {
        $scope.getTweetsError = "";
        $http.get('/accounts/' + $scope.account.id + '/messages',
            {headers: {'X-Auth-Token': authService.getToken().toString()}})
            .success(function (data) {
                accountService.setTweets(data);
                $scope.getTweetsError = "";
            })
            .error(function (error) {
                $scope.getTweetsError = "Get tweets error, please try again."
            })
            .finally(function () {
                $scope.account.tweets = accountService.getTweets();
                $location.path("/userDetail");
            })
    };

    $scope.isFollower = function () {
        $scope.isFollowerError = "";
        $http.get('/accounts/' + $scope.account.id + '/followers',
            {headers: {'X-Auth-Token': authService.getToken().toString()}})
            .success(function (data) {
                accountService.setFollowers(data);
                $scope.account.followers = data;
                $scope.isFollowerError = "";
            })
            .error(function (error) {
                $scope.isFollowerError = "Get follower error, please try again."
            })
            .finally(function () {
                $scope.account.amIfollowing = accountService.isFollower($scope.auth.username);
                $location.path("/userDetail");
            })
    };

    $scope.getAuthAccount = function () {
        $scope.getAuthAccountError = "";
        $http.get('/account/handle/' + $scope.auth.username,
            {headers: {'X-Auth-Token': authService.getToken().toString()}})
            .success(function (data) {
                authService.setAccount(data);
                $scope.getAuthAccountError = ""
            })
            .error(function (error) {
                $scope.getAuthAccountError = "Get authenticated account error, please try again."
            })
            .finally(function () {
                $scope.auth.account = authService.getAccount();
                $location.path("/userDetail");
            })
    };

    $scope.startFollowing = function () {
        $scope.pageStatus = "";
        $scope.startFollowError = "";
        $http.post('/accounts/' + $scope.auth.account.id + '/startFollowing?followAccount=' + $scope.account.id,
            {headers: {'X-Auth-Token': authService.getToken().toString()}})
            .success(function (data) {
                $scope.getAccount();
                $scope.startFollowError = "";
            })
            .error(function (error) {
                $scope.startFollowError = "Follow user error, please try again."
            })
            .finally(function () {
                $scope.pageStatus = "Page load complete";
                $location.path("/userDetail");
            })
    };

    $scope.saveDetails = function () {
        var updates = new Object();
        updates.displayName = $scope.account.displayName;
        updates.emailAddress = $scope.account.emailAddress;

        $scope.pageStatus = "";
        $scope.saveUserDetailsError = "";

        var jsonString = JSON.stringify(updates);

        $http.put('/accounts/' + $scope.account.id, updates,
            {headers: {'X-Auth-Token': authService.getToken().toString()}})
            .success(function (data) {
                $scope.saveUserDetailsError = "";
            })
            .error(function (error) {
                $scope.saveUserDetailsError = "Save user details error, please try again.";
            })
            .finally(function () {
                $scope.getAccount();
                $scope.getAuthAccount();
                $scope.pageStatus = "Page load complete";
                $location.path("/userDetail");
            })
    };

    $scope.tweetMessage = function() {
        var message = new Object();
        message.messageText = $scope.account.messageContent;

        $scope.pageStatus = "";
        $scope.showAlert = true;

        var jsonString = JSON.stringify(message);

        $http.post('/message/tweet?accountId=' + $scope.account.id, message,
            {
                headers: {'X-Auth-Token': authService.getToken().toString()}
            })
            .success(function(data){
                $scope.alerts = [
                    { type: 'success', msg: 'Message posted successfully' }
                ];
                $scope.getTweets();
            })
            .error(function (error) {
                $scope.alerts = [
                    { type: 'danger', msg: 'Error posting message' }
                ];
            })
            .finally(function () {
                $scope.account = accountService.getAccount();
                $scope.pageStatus = "Page load complete";
                $location.path("/userDetail");
            })
    };

    $scope.reset=function(tweetForm) {
        tweetForm.$setPristine();
        tweetForm.$setUntouched();
        tweetForm.getElementById('message-to-post').value = ""; //TBD: this doesn't seem to clear the textbox
    };

    $scope.closeAlert = function() {
        $scope.showAlert = false;
    };

    if ($scope.auth.account == null)
        $scope.getAuthAccount();

    $scope.pageStatus = "Page load complete";
});
