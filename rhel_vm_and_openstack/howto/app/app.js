/**
 * Created By   : Pascal Leloup
 * Class        : bps , 'angularBootstrapNavTree','treeControl','ngDragDrop'
 */
var sandbox;
(function() {
    'use strict';
    sandbox = angular
        .module('sandbox', ['ngResource', 'ngRoute', 'ngDialog', 'LocalStorageModule'])
        .config(function($sceDelegateProvider) {
            $sceDelegateProvider.resourceUrlWhitelist([
                // Allow same origin resource loads.
                'self',
                // Allow loading from our assets domain.  Notice the difference between * and **.
                'http://atrcxb2994.athtem.eei.ericsson.se:23307/wp-json/wp/v2/**'
            ]);

            // The blacklist overrides the whitelist so the open redirect here is blocked.
            $sceDelegateProvider.resourceUrlBlacklist([

            ]);
        })
        .filter('toArray', function() {
            return function(obj) {
                if (!(obj instanceof Object)) return obj;
                return _.map(obj, function(val, key) {
                    return Object.defineProperty(val, '$key', { __proto__: null, value: key });
                });
            }
        })
        .filter('html', ['$sce', function($sce) {
            return function(text) {
                return $sce.trustAsHtml(text);
            };
        }])
        .filter('htmlToPlaintext', function() {
            return function(text) {
                return text ? String(text).replace(/<[^>]+>/gm, '') : '';
            };
        })
        .filter('propsFilter', function() {
            return function(items, props) {
                var out = [];

                if (angular.isArray(items)) {
                    var keys = Object.keys(props);

                    items.forEach(function(item) {
                        var itemMatches = false;

                        for (var i = 0; i < keys.length; i++) {
                            var prop = keys[i];
                            var text = props[prop].toLowerCase();
                            if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
                                itemMatches = true;
                                break;
                            }
                        }

                        if (itemMatches) {
                            out.push(item);
                        }
                    });
                } else {
                    // Let the output be the input untouched
                    out = items;
                }

                return out;
            };
        })
        .filter('search', function($filter) {
            return function(items, text) {
                if (!text || text.length === 0)
                    return items;

                // split search text on space
                var searchTerms = text.split(' ');

                // search for single terms.
                // this reduces the item list step by step
                searchTerms.forEach(function(term) {
                    if (term && term.length)
                        items = $filter('filter')(items, term);
                });

                return items;
            };
        })
        .config(function($routeProvider, $locationProvider) {

            $routeProvider
                .when('/', {
                    templateUrl: 'app/templates/sandbox_home.html',
                    controller: 'SandboxController as vm'
                })
                .when('/Sandbox/:ip', {
                    templateUrl: 'app/templates/sandbox/sandbox_home.html',
                    controller: 'SandboxController as vm'
                })
                .otherwise({ redirectTo: '/' });

            $locationProvider.html5Mode(false);

        })
        .run(
            function($rootScope, $location, $window, $http, $interval, WPService, localStorageService) {
                //localStorageService.remove("_help");
                // $rootScope.help = localStorageService.get('_help');
                // if (!$rootScope.help) {
                //     WPService.appBuilderHelp()
                //         .then(function(data) {
                //             var _data = data[0].content.rendered;
                //             var _help = JSON.parse(_data.substring(_data.indexOf('['), _data.lastIndexOf(']') + 1));
                //             localStorageService.set('_help', _help);
                //             $rootScope.help = _help;
                //         }, function(data, status, headers, config) {
                //             console.log("error");
                //         });
                // }
            })
        .filter('trustUrl', ['$sce', function($sce) {
            return function(url) {
                return $sce.trustAsResourceUrl(url);
            };
        }])
        .run(function($rootScope, $location) {

            var history = [];

            $rootScope.$on('$routeChangeSuccess', function() {
                history.push($location.$$path);
            });

            $rootScope.back = function() {
                var prevUrl = history.length > 1 ? history.splice(-2)[0] : "/";
                $location.path(prevUrl);
            };

        });
})();