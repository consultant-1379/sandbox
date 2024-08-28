/**
 * Created By   : pascal leloup
 * Class        : HeaderController
 */
(function() {
    'use strict';

    sandbox.controller('HeaderController',

        function HeaderController($rootScope, $location, $scope, ngDialog) {
            $scope.subtitle = 'ericsson analytics community';
            //$('[data-toggle="tooltip"]').tooltip();
            $scope.init = function(subtitle, loggedIn) {
                typeof(subtitle) !== 'undefined' ? $scope.subtitle = subtitle: $scope.subtitle = 'ericsson analytics community';

                typeof(loggedIn) !== 'undefined' ? $scope.logged = true: $scope.logged = false;

            };

            $scope.goHome = function() {
                window.location.href = '/';
            };
            $scope.GetStarted = function() {
                window.location.href = '#/GetStarted';
            };
            $scope.Disclaimer = function() {
                ngDialog.open({
                    template: 'app/templates/shared/_announce.html',
                    plain: false
                });
            };
            $scope.AboutUs = function() {
                ngDialog.open({
                    template: 'app/templates/shared/_aboutus.html',
                    plain: false
                });
            };
            $scope.showYammer = function() {
                ngDialog.open({
                    template: 'app/templates/shared/_yammer.html',
                    plain: false,
                    width: 550
                });
            };
            $scope.Login = function() {
                ngDialog.open({
                    template: 'app/templates/shared/_login.html',
                    plain: false
                });
                // window.location.href="http://eselivm3v260l.lmera.ericsson.se:23307/wp-login.php";
            };
            $scope.logout = function() {
                window.location.href = '#/MachineLearning';
            };
        });
})();