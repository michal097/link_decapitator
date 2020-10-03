const app = angular.module("customerManagement", []);
angular.module('customerManagement').constant('SERVER_URL','/');


app.controller("customerManagementController",  function ($scope, $http, SERVER_URL) {

    $scope.customers = [];
    $scope.form = {
        id: -1,
        name: "",
        surname: ""
    };

    _refreshPageData();


    $scope.remove = function (customer) {

        $http({
            method: 'DELETE',
            url: SERVER_URL + '/delete/' + customer.id
        }).then(_success, _error);
    };


    function _refreshPageData() {
        $http({
            method: 'GET',
            url: SERVER_URL
        }).then(function successCallback(response) {
            $scope.customers = response.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }

    function _success(response) {
        _refreshPageData();

    }

    function _error(response) {
        alert(response.data.message || response.statusText);
    }

})