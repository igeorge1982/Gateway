var hmacApp = angular.module('hmac', ['ab-base64','ng.deviceDetector'], function ($httpProvider) {
    // Add an HTTP interceptor which passes the request URL to the transformer
    // Allows to include the URL into the signature
    // Rejects request if no hmacSecret is available
    $httpProvider.interceptors.push(function($q) {
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
           
             // optional method
            'response': function(response) {
                
                // TODO: get response headers
              return response;
            },
           
       };
    });

    // Add a custom request transformer to generate required headers
    $httpProvider.defaults.transformRequest.push(function (data, headersGetter) {
        if (data) {

            
            // Add current time to prevent replay attacks
            var microTime = new Date().getTime();
            headersGetter()['X-MICRO-TIME'] = microTime;

            // Finally generate HMAC and set header
            headersGetter()['X-HMAC-HASH'] = CryptoJS.HmacSHA512(headersGetter()['X-URL'] + ':' + data + ':' + microTime, localStorage.hmacSecret).toString(CryptoJS.enc.Hex);

            // And remove our temporary header
            headersGetter()['X-URL'] = '';
        }
        return data;
    });
});

hmacApp.controller('LoginController', function ($scope, $http, base64, $location) {
    $scope.message = '';
    $scope.username = '';
    $scope.password = '';

    $scope.login = function () {
        // Generate HMAC secret (sha512('username:password'))
        var hash = CryptoJS.SHA3($scope.password, {outputLength: 512});
        localStorage.hmacSecret = CryptoJS.SHA512($scope.username + ":" + hash).toString(CryptoJS.enc.Hex);
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

        $http({
				method: 'POST',
				url: '/login/HelloWorld',
				data: encodedString,
				headers: {'Content-Type': 'application/x-www-form-urlencoded', 'authorization': 'Basic '+ token}
			}).
            success(function (data, status, headers, config) {

                // Generate new HMAC secret out of our previous (username + password) and the new session token
                // sha512("sha512('username:password'):JSESSIONID")
                // TODO: get response headers
                localStorage.hmacSecret = CryptoJS.SHA512(localStorage.hmacSecret);
                window.location.href = '/example/index.jsp'
            }).
            error(function (data, status, headers, config) {
				$scope.errorMsg = 'User is not found';
            });
    };

});

hmacApp.controller('myCtrl',['deviceDetector',function(deviceDetector){
    var vm = this;
    vm.data = deviceDetector;
    vm.allData = JSON.stringify(vm.data, null, 2);
    
}]);