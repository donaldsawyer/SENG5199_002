app.directive('followingStatus', function() {
        return {
            template: '<button id="follow" class="btn" ng-click="startFollowing()" ng-disabled="myDetail" ' +
                      'ng-show="!myDetail && !account.amIfollowing">Follow</button>' +
                      '<div id="follow-message" ng-show="!myDetail && account.amIfollowing"><em>You are following {{' +
                      'account.displayName }}</em></div>'
        };
    });
