var params = getQueryParams(location.search);

var app = angular.module('projectApp', []);

app.controller('projectController', function($scope, $http) {

        var data = {
                url:params.url
        };

        var config = {
                params: data,
                headers : {'Accept' : 'application/json'}
        };

        $http.get('/jreport/projects/', config).then(function(response) {
                $scope.projects = response.data;
        }, function(response) {

        });

        $scope.getIssuesFromJira = function getIssuesFromJira(projectKey) {
                window.location.href="/mainboard/analyze.html?url="+params.url+"&projectKey="+projectKey;
        }
});


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