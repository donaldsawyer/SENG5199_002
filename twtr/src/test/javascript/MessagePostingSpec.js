describe("userDetailController", function()
{
    //it("Should be true", function(){
    //    expect(true).toBeTruthy()
    //
    //});

    beforeEach(module('app'));

    var $scope;
    var $controller;
    var $httpBackend;
    var authService;
    var accountService;
    var userToken;
    var userTweets;
    var userAccount;

    var tokenToSet = "ABC123";

    var authService = {
        getToken: function () {
            return userToken;
        },
        setToken: function() {
            userToken = tokenToSet;
        }
    };

    var accountService = {
        getTweets: function () {
            return userTweets;
        },
        getAccount: function() {
            return userAccount;
        }
    };

    beforeEach(inject(function (_$controller_, _$httpBackend_, _authService_, _accountService_) {
        $controller = _$controller_;
        $scope = {};
        $scope.account = {};
        $scope.account.messageContent = "New Message";
        $scope.account.id = 1;
        $httpBackend = _$httpBackend_;
        authService = authService.setToken();
        accountService = _accountService_;
    }));

    describe('tweetMessage', function () {

        beforeEach(function ()
        {
            $controller('userDetailController', {$scope: $scope});
        });

        it('handles successful message post', function () {
            $httpBackend.expectPOST('/message/tweet?accountId=1', {messageText: 'New Message'}).respond(201);
            $httpBackend.expectGET('/accounts/1/messages').respond(200, [{handle: "admin", messageText: "New Message", dateCreated: "05-02-2016", lastUpdated: "05-02-2016"}]);
            $controller.tweetMessage();
            $httpBackend.flush();
            expect(accountService.getTweets).toHaveBeenCalled();


        });

    });

});