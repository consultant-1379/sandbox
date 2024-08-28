(function() {

    'use strict';

    sandbox.controller('SandboxController',

        function SandboxController($scope, $rootScope, $route, $timeout, $routeParams, WPService, ngDialog) {

            var main = this;
            if (typeof($routeParams.ip) !== 'undefined') {
                $scope.sandboxIp = $routeParams.ip;
            } else {
                $scope.sandboxIp = window.location.hostname;
            }
            WPService.getDoc('sandbox_list')
                .then(function(data) {
                    var _data = data[0].content.rendered;
                    var _docs = JSON.parse(_data.substring(_data.indexOf('['), _data.lastIndexOf(']') + 1));
                    $scope.cards = _docs;
                })
                .then(function() {

                    WPService.getNotebooks($scope.sandboxIp)
                        .then(function(data) {
                            console.log(data);
                            $scope.aia_samples = data.body;
                            $timeout(function() {
                                angular.element('.card__share > a').on('click', function(e) {
                                    e.preventDefault(); // prevent default action - hash doesn't appear in url
                                    angular.element(this).parent().find('div').toggleClass('card__social--active');
                                    angular.element(this).toggleClass('share-expanded');
                                });

                            }, 500);

                        });
                });
            $scope.getNotebookUrl = function(_id) {

                window.location.href = "http://" + $scope.sandboxIp + ':9090/#/notebook/' + _id;
            };

            $scope.showSettings = function(args) {
                if (args.currentTarget.className.indexOf('info') > 0) {
                    $(args.currentTarget.parentElement.parentElement.parentElement).find('.effect__click').toggleClass('flipped');
                } else {
                    $(args.currentTarget.parentElement.parentElement).find('.effect__click').toggleClass('flipped');
                }

            }
            $scope.doc = function(_tech) {
                window.location.href = _tech;
            };
            $scope.launch = function(_tech) {
                window.location.href = "http://" + $scope.sandboxIp + _tech;
            };
            $scope.showYammer = function() {
                ngDialog.open({
                    template: 'app/templates/shared/_yammer.html',
                    plain: false,
                    width: 550
                });
            };
        });
})();