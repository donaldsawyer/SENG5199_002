app.service('accountService', function() {

    var account;
    var tweets;

    var getAccount = function() {
        return account;
    }

    var setAccount = function(acct) {
        account = acct;
    }

    var getTweets = function() {
        return tweets;
    }

    var setTweets = function(twts) {
        tweets = twts;
    }

    //var getAccount = function() {
    //    var handle = authService.getUsername().toString();
    //    var token = authService.getToken().toString();
    //
    //    var account = $http.get('/account/handle/' + handle,
    //        { headers: {'X-Auth-Token': token}})
    //        .success( function(result) {
    //            return result;
    //        })
    //};

    return {
        getAccount : getAccount,
        setAccount : setAccount,
        getTweets : getTweets,
        setTweets : setTweets
    };
});
