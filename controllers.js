'use strict';

var myAppControllers = angular.module('myAppControllers', []);

myAppControllers.factory('userApi', userApi)
                .factory('logoutApi', logoutApi);

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
    
    $scope.token = CryptoJS.AES.encrypt("Message", "SecretPassphrase");
    
    $scope.callRestService= function() {
    $http({method: 'GET', url: 'https://localhost/login/admin'}).
        success(function(data, status, headers, config) {
            $scope.results.push(data);  //retrieve results and add to existing results
    })
}

    function refreshUser() {
        loading = true;
        $scope.user = [];
        $scope.errorMessage = '';
        $scope.successMessage = '';
        $scope.token = CryptoJS.AES.encrypt("Message", "SecretPassphrase");
        //$scope.decodedToken = CryptoJS.AES.decrypt(encrypted, "SecretPassphrase");;

   
        userApi.getUser()
            .success(function (data) {
                $scope.user = data;
                $scope.successMessage = "Hello!";
                $scope.token = CryptoJS.AES.encrypt("Message", "SecretPassphrase");
                loading = false;
            })
            .error(function () {
                $scope.errorMessage = "Error!";
                loading = false;
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

myAppControllers.controller('SearchCtrl', ['$scope','$http',
   function ($scope, $http) {
    $scope.url = 'https://localhost/mbook-1/rest/newuser';
    $scope.keywords = '';
    $scope.errorMessage = '';
    // The function that will be executed on button click (ng-click="search()")
    $scope.search = function() {

        // Create the http post request
        // the data holds the keywords
        // The request is a JSON request.
    $http.get($scope.url+'/'+$scope.keywords)
            .success(function(data, status) {
                $scope.status = status;
                $scope.errorMessage = data;
                $scope.data = data;
                $scope.result = data; // Result
     })
            .error(function(data, status) {
                $scope.data = data || "Request failed";
                $scope.status = status;
                $scope.errorMessage = "Username is already taken!";

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
    "use strict";
    return {
        logOut_: function () {
            var url = '/login/logout';
            return $http.get(url);
        }
    };
}