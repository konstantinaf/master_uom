var params = getQueryParams(location.search);

var app = angular.module('projectApp', []);

app.controller('projectController', function($scope, $http) {

        var data = {
                oauthToken:params.oauth_token,
                oauthVerifier: params.oauth_verifier
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
                alert(projectKey);
                var data = {
                        projectKey:projectKey,
                        oauthVerifier: params.oauth_verifier
                };

                var config = {
                        params: data,
                        headers : {'Accept' : 'application/json'}
                };

                $http.get('/jreport/issues/', config).then(function(response) {
                        $scope.issues = response.data;
                }, function(response) {

                });

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