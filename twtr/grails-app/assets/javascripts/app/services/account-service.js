app.service('accountService', function () {

    var account;
    var tweets;
    var followers;

    var getAccount = function () {
        return account;
    };

    var setAccount = function (acct) {
        account = acct;
    };

    var getTweets = function () {
        return tweets;
    };

    var setTweets = function (twts) {
        tweets = twts;
    };

    var setFollowers = function (flwrs) {
        followers = flwrs;
    };

    var getFollowers = function () {
        return followers;
    };

    var isFollower = function (handle) {
        var found = false;

        for (var i = 0; i < account.followers.length; i++) {
            if (account.followers[i].handle == handle)
                found = true;
        }

        return found;
    };

    return {
        getAccount: getAccount,
        setAccount: setAccount,
        getTweets: getTweets,
        setTweets: setTweets,
        setFollowers: setFollowers,
        getFollowers: getFollowers,
        isFollower: isFollower
    };
});
