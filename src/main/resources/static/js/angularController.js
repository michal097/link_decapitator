var app = angular.module('app',[]);
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



        $scope.deleteUser = function () {
            UserCRUDService.deleteUser($scope.link)
                .then (function success(response){
                        $scope.message = 'Link has been successfully deleted!';
                        $scope.errorMessage='Wrong delete key';
                    },
                    function error(response){
                        $scope.errorMessage = 'Wrong delete key';
                        $scope.message='Wrong delete key';

                    })
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

    }]);