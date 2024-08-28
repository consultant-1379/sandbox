/**
 * Created By   : Pascal Leloup
 * Class        : WordPress Service
 */
(function() {
    sandbox.factory('WPService',
        function($resource, $q, $http, $routeParams, $rootScope) {

            return {
                getUsers: function() {

                    var deferred = $q.defer();
                    //usSpinnerService.spin('spinner-1');

                    var resource = $resource(
                        API.USERS, {

                        }, {
                            query: { method: 'GET', isArray: true }
                        }
                    );

                    resource.query(

                        function(item) {

                            //usSpinnerService.stop('spinner-1');
                            deferred.resolve(item);
                        },

                        function(error) {

                            //usSpinnerService.stop('spinner-1');
                            deferred.reject(error)
                        }
                    );
                    return deferred.promise;

                },
                getUser: function(_uid) {

                    var deferred = $q.defer();
                    //usSpinnerService.spin('spinner-1');

                    var resource = $resource(
                        API.USER, {
                            userid: _uid
                        }, {
                            query: { method: 'GET', isArray: false }
                        }
                    );

                    resource.query(

                        function(item) {

                            //usSpinnerService.stop('spinner-1');
                            deferred.resolve(item);
                        },

                        function(error) {

                            //usSpinnerService.stop('spinner-1');
                            deferred.reject(error)
                        }
                    );
                    return deferred.promise;

                },
                getBlogs: function() {

                    var deferred = $q.defer();
                    //usSpinnerService.spin('spinner-1');

                    var resource = $resource(
                        API.BLOGS, {

                        }, {
                            query: { method: 'GET', isArray: true }
                        }
                    );

                    resource.query(

                        function(item) {

                            //usSpinnerService.stop('spinner-1');
                            deferred.resolve(item);
                        },

                        function(error) {

                            //usSpinnerService.stop('spinner-1');
                            deferred.reject(error)
                        }
                    );
                    return deferred.promise;

                },
                getCategory: function(catId) {

                    var deferred = $q.defer();
                    //usSpinnerService.spin('spinner-1');

                    var resource = $resource(
                        API.CATEGORY, {
                            catid: catId
                        }, {
                            query: { method: 'GET', isArray: true }
                        }
                    );

                    resource.query(

                        function(item) {

                            //usSpinnerService.stop('spinner-1');
                            deferred.resolve(item);
                        },

                        function(error) {

                            //usSpinnerService.stop('spinner-1');
                            deferred.reject(error)
                        }
                    );
                    return deferred.promise;

                },
                getNews: function() {

                    var deferred = $q.defer();
                    //usSpinnerService.spin('spinner-1');

                    var resource = $resource(
                        API.NEWS, {

                        }, {
                            query: { method: 'GET', isArray: true }
                        }
                    );

                    resource.query(

                        function(item) {

                            //usSpinnerService.stop('spinner-1');
                            deferred.resolve(item);
                        },

                        function(error) {

                            //usSpinnerService.stop('spinner-1');
                            deferred.reject(error)
                        }
                    );
                    return deferred.promise;

                },
                getCategories: function(_parentId) {

                    var deferred = $q.defer();
                    //usSpinnerService.spin('spinner-1');

                    var resource = $resource(
                        API.CATEGORIES, {
                            parent: _parentId
                        }, {
                            query: { method: 'GET', isArray: true }
                        }
                    );

                    resource.query(

                        function(item) {

                            //usSpinnerService.stop('spinner-1');
                            deferred.resolve(item);
                        },

                        function(error) {

                            //usSpinnerService.stop('spinner-1');
                            deferred.reject(error)
                        }
                    );
                    return deferred.promise;

                },
                getDoc: function(_docname) {

                    var deferred = $q.defer();
                    //usSpinnerService.spin('spinner-1');

                    var resource = $resource(
                        API.DOC, {
                            docname: _docname
                        }, {
                            query: { method: 'GET', isArray: true }
                        }
                    );

                    resource.query(

                        function(item) {

                            //usSpinnerService.stop('spinner-1');
                            deferred.resolve(item);
                        },

                        function(error) {

                            //usSpinnerService.stop('spinner-1');
                            deferred.reject(error)
                        }
                    );
                    return deferred.promise;

                },
                getNotebooks: function(_SandboxIp) {

                    var deferred = $q.defer();
                    //usSpinnerService.spin('spinner-1');
                    API.NOTEBOOKS = 'http://' + _SandboxIp + ':9090/api/notebook';
                    //API.NOTEBOOKS = 'http://131.160.133.21' + ':9090/api/notebook';
                    console.log(API.NOTEBOOKS);
                    var resource = $resource(
                        API.NOTEBOOKS, {}, {
                            query: { method: 'GET', isArray: false }
                        }
                    );

                    resource.query(

                        function(item) {

                            //usSpinnerService.stop('spinner-1');
                            deferred.resolve(item);
                        },

                        function(error) {

                            //usSpinnerService.stop('spinner-1');
                            deferred.reject(error)
                        }
                    );
                    return deferred.promise;

                },
                appBuilderHelp: function() {

                    var deferred = $q.defer();
                    //usSpinnerService.spin('spinner-1');

                    var resource = $resource(
                        API.DOC, {
                            docname: 'appbuilder_help'
                        }, {
                            query: { method: 'GET', isArray: true }
                        }
                    );

                    resource.query(

                        function(item) {

                            //usSpinnerService.stop('spinner-1');
                            deferred.resolve(item);
                        },

                        function(error) {

                            //usSpinnerService.stop('spinner-1');
                            deferred.reject(error)
                        }
                    );
                    return deferred.promise;

                }
            }
        });
})();
