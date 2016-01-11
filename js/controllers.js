'use strict';

var myAppControllers = angular.module('myAppControllers', []);

myAppControllers.factory('userApi', userApi)
                .factory('logoutApi', logoutApi)
                .factory('userService', userService)
                .config(function ($httpProvider) {
        $httpProvider.interceptors.push(function ($q, $injector, userService) {
            return {
                // This is the request interceptor
                request: function (request) {
                    request.headers.authorization = userService.getAuthorization();
                    return request;
                },
                
                // This is the responseError interceptor
                responseError: function (rejection) {
                    
                    var authenticate = function () {
                            
                            var $modal = $injector.get('$modal');
                            var modal = $modal.open({
                                    template: '<div style="padding: 15px">' +
                                        '  <input type="password" ng-model="pwd">' +
                                        '  <button ng-click="submit(pwd)">' +
                                        '    Submit' +
                                        '  </button>' +
                                        '</div>',
                                    controller: function ($scope, $modalInstance) {
                                        $scope.submit = function (pwd) {
                                            $modalInstance.close(pwd);
                                        };
                                    }
                                });

                      /* `modal.result` is a promise that gets resolved when 
                       * $modalInstance.close() is called */
                            return modal.result.then(function (pwd) {
                                password = pwd;
                            });
                        };
                    
                    var status = rejection.status;
                    var config = rejection.config;
                    var method = config.method;
                    var url = config.url;
                    
                    if (rejection.status === 502) {
                        // Return a new promise
                        return authenticate().then(function() {
                            return $injector.get('$http')(rejection.config);
                        });
                        
                    }

                        /* If not a 401, do nothing with this error.
                         * This is necessary to make a `responseError`
                         * interceptor a no-op. */
                    return $q.reject(rejection);
                }
            };
        });
    }); 

myAppControllers.controller('MainCtrl', function ($scope) {
    $scope.showModal = false;
    $scope.toggleModal = function () {
        $scope.showModal = !$scope.showModal;
    };
});

myAppControllers.controller('Image', function ($scope) {
      $scope.imageSource = 'https://localhost/simple-service-webapp/webapi/myresource/images/Bp-1.jpg';

});


myAppControllers.controller('GetUser', ['$scope', 'userApi', function ($scope, userApi) {
    
    var loading = false;
    function isLoading() {
        return loading;
    }
    
    $scope.user = [];
    $scope.errorMessage = '';
    $scope.successMessage = '';
    $scope.isLoading = isLoading;
    $scope.refreshUser = refreshUser;

    function refreshUser() {
        loading = true;
        $scope.user = [];
        $scope.errorMessage = '';
        $scope.successMessage = '';
        $scope.refreshUser = refreshUser;
   
        userApi.getUser()
            .success(function (data, status, headers) {
                console.log(headers());
                $scope.user = data;
                $scope.status = status;
                $scope.successMessage = "Hello!";
                loading = false;
            
            
            })
            .error(function (status) {
                $scope.errorMessage = "Error!";
                $scope.status = status;
                loading = false;
                // This section uses the directives.js to display the modal in index.jsp. This modal doesn't use controller.
                $scope.showModal = true;
                $scope.toggleModal = function () {
                    $scope.showModal = !$scope.showModal;
                };
            });
    }
}]);

myAppControllers.controller('LogOut', ['$scope', 'logoutApi', function ($scope, logoutApi) {
   
    var loading = false;
    function isLoading() {
        return loading;
    }

    $scope.user = [];
    $scope.errorMessage = '';
    $scope.successMessage = '';
    $scope.isLoading = isLoading;
    $scope.logOut = logOut;
    
    function logOut() {
        loading = true;
        $scope.user = [];
        $scope.errorMessage = '';
        $scope.successMessage = '';
        logoutApi.logOut_()
            .success(function (data) {
                $scope.user = data;
                $scope.successMessage = "LogOut is successfull!";
                loading = false;
            })
            .error(function () {
                $scope.errorMessage = "Error!";
                loading = false;
            });
    }
}]);

myAppControllers.controller('SearchCtrl', ['$scope', '$http',
    function ($scope, $http) {
        $scope.url = 'https://localhost/mbook-1/rest/newuser';
        $scope.keywords = '';
        $scope.errorMessage = '';
        $scope.successMessage = '';

    // The function that will be executed on change (ng-change="search()")
        $scope.search = function () {

        // Create the http post request
        // the data holds the keywords
        // The request is a JSON request.
            $http.get($scope.url + '/' + $scope.keywords)
                .success(function (data, status) {
                    $scope.status = status;
                    $scope.successMessage = "Okay";
                    $scope.data = data;
                    $scope.result = data; // Result
                })
                .error(function (data, status) {
                    $scope.data = data || "Request failed";
                    $scope.status = status;
                    $scope.errorMessage = 'Error!';

                });
        }
    }]);

function userApi($http) {
    return {
        getUser: function () {
            var url = '/login/admin';
            
            var iterationCount = 1000;
var keySize = 128;
var plaintext = "G";
var passphrase = "SecretPassphrase";
var iv = "F27D5C9927726BCEFE7510B1BDD3D137";
var salt = "3FF2EC019C627B945225DEBAD71A01B6985FE84C95A70EB132882F88C0A59A55";

            var aesUtil = new AesUtil(keySize, iterationCount);
            var ciphertext = aesUtil.encrypt(salt, iv, passphrase, plaintext);
            
            var config = {headers: {
                    'Ciphertext': ciphertext
                }
                    };
            return $http.get(url, config);
        }
    };
}

function logoutApi($http) {
    return {
        logOut_: function () {
            var url = '/login/logout';
            return $http.get(url);
        }
    };
}

function userService() {
    return {       
        getAuthorization: function () {
            return 'Taco';
        }
    };
}