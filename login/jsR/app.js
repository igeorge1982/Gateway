var hmacApp = angular.module('hmac', ['ab-base64','ng.deviceDetector']);

hmacApp.config(function ($httpProvider) {
    // Add an HTTP interceptor which passes the request URL to the transformer
    // Allows to include the URL into the signature
    // Rejects request if no hmacSecret is available
    $httpProvider.interceptors.push(function($q, jsonFilter) {
       return {
            'request': function(config) {
                if(!localStorage.hmacSecret) {
                    return $q.reject('No HMAC secret to sign request!');
                }

                config.headers['X-URL'] = config.url;
                return config || $q.when(config);
            },
                // This is the responseError interceptor
                responseError: function (rejection) {
                    
                    if (rejection.status === 502) {
                    }

                    return $q.reject(rejection);
                },
           
              // On request failure
              requestError: function (rejection) {
              console.log(rejection); // Contains the data about the error on the request.

                // Return the promise rejection.
                return $q.reject(rejection);
              },

             // On response success
            'response': function(response) {
            
            // do something on success
            //window.location.href = '/example/index.jsp'

        // Return the response or promise.
        return response || $q.when(response);
            },
           
       };
    });


    // Add a custom request transformer to generate required headers
    $httpProvider.defaults.transformRequest.push(function (data, headersGetter, jsonFilter) {
        if (data) {
           
            // Add session token header if available
            if (localStorage.sessionToken) {
                headersGetter()['X-SESSION-TOKEN'] = localStorage.sessionToken;
            }
            
            // Add current time to prevent replay attacks
            var microTime = new Date().getTime();
            headersGetter()['X-MICRO-TIME'] = microTime;
            
            // 4RI "Message", "secret"
            var hash = CryptoJS.HmacSHA512(headersGetter()['X-URL'] + ':' + data + ':' + microTime, localStorage.hmacSecret);
            var hashInBase64 = CryptoJS.enc.Base64.stringify(hash); 
            
            // Finally generate HMAC and set header
            headersGetter()['X-HMAC-HASH'] = hashInBase64;
            
            // And remove our temporary header
            headersGetter()['X-URL'] = '';
        }
        return data;
    });
});

/*
hmacApp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/index', {
        templateUrl: '../index.jsp',
      //  controller: 'AddOrderController'
      });
  }]);
*/

hmacApp.controller('LoginController', function ($scope, $http, base64, $location, jsonFilter) {
    $scope.message = '';
    $scope.username = '';
    $scope.password = '';

    $scope.login = function () {
        
        // Hash password
        var hash = CryptoJS.SHA3($scope.password, {outputLength: 512});

        // Generate HMAC secret (sha512('username:password'))
        var hmacSec = CryptoJS.HmacSHA512($scope.username, encodeURIComponent(hash));
        localStorage.hmacSecret = CryptoJS.enc.Base64.stringify(hmacSec);
                
        var token = '';
        var useR = $scope.username;
        $scope.password = hash;
        $scope.encoded = base64.encode(useR+":"+hash);
        var token = $scope.encoded;
        var guid = function() {

                var nav = window.navigator;
                var screen = window.screen;
                var guid = nav.mimeTypes.length;
                guid += nav.userAgent.replace(/\D+/g, '');
                guid += nav.plugins.length;
                guid += screen.height || '';
                guid += screen.width || '';
                guid += screen.pixelDepth || '';

                return guid;
            };
		
            var uuid = guid()
        
            var encodedString = 'user=' +
				encodeURIComponent($scope.username) +
				'&pswrd=' +
				encodeURIComponent(hash) +
				'&deviceId=' +
				encodeURIComponent(uuid);
        
        $scope.username = '';
        $scope.Result = [];

        $http({
				method: 'POST',
				url: '/login/HelloWorld',
				data: encodedString,
				headers: {'Content-Type': 'application/x-www-form-urlencoded', 'authorization': 'Basic '+ token}
			}).
            success(function (data, status, headers, config) {
           
            var token = function() {
            // Store session token 
            localStorage.sessionToken = headers('X-Token');            
            
                return token;
                
            };
            
            var sessionToken = token();

            // console.log(headers('X-Token'));
            
            // Generate new HMAC secret out of our previous (username + password) and the new session token
            // sha512("sha512('username:password'):sessionToken")
            
            localStorage.hmacSecret = CryptoJS.SHA512(localStorage.hmacSecret, sessionToken);
            $scope.Result = data;
            
				if ( data.Session === 'raked') {
					window.location.href = '/example/index.jsp';
				} else {
                // 
					$scope.errorMsg = "Login not correct";
				}            
            }).
            error(function (data, status, headers, config) {
				$scope.errorMsg = 'Login incorrect';
            });
    };

});

hmacApp.controller('myCtrl',['deviceDetector',function(deviceDetector){
    var vm = this;
    vm.data = deviceDetector;
    vm.allData = JSON.stringify(vm.data, null, 2);
    
}]);