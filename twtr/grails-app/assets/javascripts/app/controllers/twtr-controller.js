angular.module('app').controller('headerController', function ($scope, $location) {
    $scope.message = 'I manage the header, meaning the nav tabs';
    $scope.isActive = function (viewLocation) {
        return viewLocation == $location.path();
    };
});
//
angular.module('app').controller('mainController', function ($scope, $location, $http, authService) {

    $scope.auth = {};
    $scope.auth.token = authService.getToken();

    $scope.doLogin = function() {

        var credentials = new Object();
        credentials.username = $scope.auth.username;
        credentials.password = $scope.auth.password;

        var jsonString= JSON.stringify(credentials);

        authService.setToken({});

        //TODO: Refactor this into the authService
        $http.post('/api/login', credentials)
            .success(function (data) {
                authService.setUsername(data.username);
                authService.setToken(data.access_token);
                //$scope.auth.roles = data.roles;
            })
            .error(function (error) {
                $scope.auth.authError = error;
            })
            .finally(function () {
                $scope.auth.token = authService.getToken();
                $location.path("/home");
            });
    }
});

angular.module('app').controller('searchController', function ($scope, $location, $http, authService) {

    $scope.message = "Search Controller";

    $scope.auth = {};
    $scope.auth.token = authService.getToken();
});

//
//app.controller('aboutController', function ($scope) {
//    $scope.message = 'About Page';
//});
//
//app.controller('contactController', function ($scope) {
//    $scope.message = 'Contact Us';
//});

//app.controller('attendeeController', function ($scope, $location, $routeParams, attendeeService) {
//    $scope.attendee = {};
//    $scope.mode = 'Add';
//    if ('edit' == $routeParams.action) {
//        $scope.mode = 'Edit';
//        var id = $routeParams.id;
//        var attendees = attendeeService.getAttendees();
//        for (i = 0; i < attendees.length; i++) {
//            if (attendees[i].id == id) {
//                $scope.attendee = attendees[i];
//            }
//        }
//    }
//
//    $scope.saveCurrentAttendee = function () {
//        if ($scope.attendee.first && $scope.attendee.last) {
//            if ($scope.attendee.id) {
//                attendeeService.updateAttendee($scope.attendee);
//            } else {
//                attendeeService.addAttendee($scope.attendee);
//            }
//            attendeeService.attendee = {};
//            $location.path("/home");
//        }
//    };
//
//    $scope.message = 'Wire up controller in html (Not really good practice)';
//
//    $scope.return = function () {
//        $location.path("/home");
//    };
//});
