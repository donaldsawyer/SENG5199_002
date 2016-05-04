describe("userDetailController", function () {
    beforeEach(module('app'));

    var $scope;
    var $controller;
    var $httpBackend;
    var authService;
    var accountService;

    var tokenToSet = "ABC123";

    beforeEach(inject(function (_$controller_, _$httpBackend_, _$rootScope_, _authService_, _accountService_) {
        authService = _authService_;
        accountService = _accountService_;

        authService.setUsername("admin");
        authService.setToken(tokenToSet);

        accountService.setAccount({id: 1, handle: 'admin'});

        $scope = _$rootScope_.$new();
        $controller = _$controller_;
        $httpBackend = _$httpBackend_;
    }));

    describe('tweetMessage', function () {

        beforeEach(function ()
        {
            spyOn(accountService, 'getTweets');
            spyOn(accountService, 'setTweets');
            $controller('userDetailController', {$scope: $scope, authService: authService, accountService: accountService});
        });

        it('handles successful message post', function () {
            $httpBackend.expectGET('/account/handle/admin').respond(200, {id: 1, handle: 'admin'});
            $httpBackend.expectPOST('/message/tweet?accountId=1', {messageText: "New Message"}).respond(201);
            $httpBackend.expectGET('/accounts/1/messages').respond(200, [{handle: "admin", messageText: "New Message", dateCreated: "05-02-2016", lastUpdated: "05-02-2016"}]);

            expect($scope).toBeDefined();
            expect($scope.account).toBeDefined();
            expect($scope.account.messageContent).toBeUndefined();

            $scope.account.messageContent = "New Message";
            expect($scope.account.messageContent).toBeDefined();

            $scope.tweetMessage();
            $httpBackend.flush();
            expect(accountService.getTweets).toHaveBeenCalledTimes(1);
            expect(accountService.setTweets).toHaveBeenCalledTimes(1);
        });

    });
});