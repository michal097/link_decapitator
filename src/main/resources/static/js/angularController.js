var app = angular.module('app',['ngMessages']);
app.service('UserCRUDService', [ '$http', function($http) {

    this.getAllLinks = function getAllLinks() {
        return $http({
            method: 'GET',
            url: 'allUrls/'
        });
    }
    this.deleteUser = function deleteUser(link) {
        return $http({
            method: 'DELETE',
            url: 'delete/' + link.deleteKey
        });
    }
}]);

app.controller('UserCRUDCtrl', ['$scope','UserCRUDService',
    function ($scope,UserCRUDService, $http) {

    $scope.submitted= false;


        $scope.deleteUser = function () {
            UserCRUDService.deleteUser($scope.link)
                .then (function error(response){
                    if(response.status===409){
                        $scope.errorMessage = response.data.message;
                    }else
                    {
                        $scope.errorMessage = 'Invalid delete key';
                    }
                    $scope.message = 'as';

                    })
            $scope.getAllLinks();
        }


        $scope.getAllLinks = function () {
            UserCRUDService.getAllLinks()
                .then(function success(response) {
                        $scope.links = response.data;
                        $scope.id = 0;
                        $scope.originalName='';
                        $scope.newName='';
                        $scope.counter = '';
                        $scope.deleteKey = '';
                    },
                    function error (response) {
                        $scope.message='';
                        $scope.errorMessage = 'Error getting users!';
                    });
        }

        $scope.getAllLinks();

    }]);