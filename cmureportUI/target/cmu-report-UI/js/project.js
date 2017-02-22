var params = getQueryParams(location.search);

var app = angular.module('projects-app',[]);

app.controller("JiraReportController",
     ['$scope', function($scope) {
            $scope.greeting = 'Hola!';
}]);



function getQueryParams(qs) {
        qs = qs.split('+').join(' ');

        var params = {},
            tokens,
            re = /[?&]?([^=]+)=([^&]*)/g;

        while (tokens = re.exec(qs)) {
                params[decodeURIComponent(tokens[1])] = decodeURIComponent(tokens[2]);
        }

        return params;
}