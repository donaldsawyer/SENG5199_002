app.service('authService', function() {
    var token = null;
    var username = null;
    var account = null;

    var getToken = function() {
        return token;
    };

    var setToken = function(newToken) {
        token = newToken;
    };

    var getUsername = function() {
        return username;
    };

    var setUsername = function(newUsername) {
        username = newUsername;
    };

    var isLoggedIn = function() {
        var rval = false;

        if(token)
        {
            //TODO: check to see if token is expired
            rval = true;
        }
        return rval;
    };

    var setAccount = function(acct) {
        account = acct;
    };

    var getAccount = function() {
        return account;
    };

    var logout = function() {
        token = null;
        username = null;
        account = null;
    }

    return {
        getToken : getToken,
        setToken : setToken,
        getUsername : getUsername,
        setUsername : setUsername,
        isLoggedIn : isLoggedIn,
        setAccount : setAccount,
        getAccount : getAccount,
        logout : logout
    };
});
