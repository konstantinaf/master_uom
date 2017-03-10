
var params = getQueryParams(location.search);

var query_data = {
    projectKey: params.projectKey,
    oauthVerifier: params.oauth_verifier
};

var config = {
    params: query_data,
    headers: {'Accept': 'application/json'}
};

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

function MainCtrl($scope, $http){

    $http.get('/jreport/issues/', config).then(function(response) {
        var data = {
            "xData": response.data.xdata,
            "yData": response.data.ydata

        };
        $scope.lineChartYData=data.yData;
        $scope.lineChartXData=data.xData
    }, function(response) {

    });

}

function BugsPerDevCtrl($scope, $http){

    $http.get('/jreport/devbugs/', config).then(function(response) {
        var data = {
            "xData": response.data.xdata,
            "yData": response.data.ydata

        };
        //$scope.giniRatio = response.data.giniCoefficient;
        $scope.lineChartYData=data.yData;
        $scope.lineChartXData=data.xData
    }, function(response) {

    });

}