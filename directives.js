var myAppDirectives = angular.module('myAppDirectives', []);

myAppDirectives.directive('myOnKeyDownCall', function () {
    return function (scope, element, attrs) {
var numKeysPress=0;
element.bind("keydown keypress", function (event) {   
         numKeysPress++;
             if(numKeysPress>=3){
                scope.$apply(function (){
                    scope.$eval(attrs.myOnKeyDownCall);
                });
                event.preventDefault();
              }
        });
    };
});