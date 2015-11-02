// Code goes here

angular.module('App', []);

angular.module('App').service('Api', api);

api.$inject = ['$http', '$q'];

function api($http, $q){
    return {

        validateEmail: validateEmail
    
    };
    
    function validateEmail(email){
        var deferred = $q.defer();
        
        setTimeout(function(){
                   var data =
                   {
                   email:[
                          {type:'unique', message:'This email is already in use'}
                          ],
                   name:[
                         {type:'maxlength', message:'Your name is to long, get a new one :)'}
                         ]
                   };
                   deferred.reject(data)
                   });
        
        
        return deferred.promise;
    }
}

angular.module('App').controller('AppController', appController);

appController.$inject = ['$scope', 'Api'];

function appController($scope, Api){

    $scope.serverValidations = {};
    $scope.attemptSignUp = function(){
        
        Api.validateEmail($scope.email).then(angular.noop, function(data){
                                             
                                             $scope.serverValidations = data
                                             
                                             for(prop in $scope.serverValidations){
                                             
                                                 if($scope.signUpForm[prop]){
                                             
                                                     angular.forEach($scope.serverValidations[prop], function(validation){
                                                             
                                                         $scope.signUpForm[prop].$setValidity(validation.type, false);
                                                         
                                                     });
                                                    }
                                                }
                                             });
    }
}