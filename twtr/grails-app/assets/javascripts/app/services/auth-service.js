app.service('authService', function() {
    var token = {};
    var username = {};

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

    return {
        getToken : getToken,
        setToken : setToken,
        getUsername : getUsername,
        setUsername : setUsername,
        isLoggedIn : isLoggedIn
    };
});
