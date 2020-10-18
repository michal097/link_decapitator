var app = angular.module('app', ['ngMessages','ngMaterial','ui.grid','ui.grid.pagination']);
app.service('UserCRUDService', ['$http', function ($http) {

    this.getAllLinks = function getAllLinks() {
        return $http({
            method: 'GET',
            url: 'allUrls/'
        });
    }

    this.getPagination = function getPagination(pageNumber, pageSize) {
        pageNumber = pageNumber > 0 ? pageNumber - 1 : 0;
        return $http({
            method: 'GET',
            url: 'allUrls/?pageSize=' + pageSize + '&pageNumber=' + pageNumber
        });

    }

    this.getPaginationIP = function getPaginationIP(pageNumber, pageSize) {
        pageNumber = pageNumber > 0 ? pageNumber - 1 : 0;
        return $http({
            method: 'GET',
            url: 'checkIP/?pageSize=' + pageSize + '&pageNumber=' + pageNumber
        });

    }


    this.getCount = function getCount() {
        return $http({
            method: 'GET',
            url: 'countAllUrls/'
        });
    }
    this.checkIP = function checkIP() {
        return $http({
            method: 'GET',
            url: 'checkIP/'
        });
    }

    this.deleteUser = function deleteUser(link) {
        return $http({
            method: 'DELETE',
            url: 'delete/' + link.deleteKey
        });
    }


}]);

app.controller('UserCRUDCtrl', ['$scope', 'UserCRUDService',
    function ($scope, UserCRUDService, $http) {


        $scope.submitted = false;


        const paginationOptions = {
            pageNumber: 1,
            pageSize: 5,
            sort: null
        };

        UserCRUDService.getPagination(paginationOptions.pageNumber,paginationOptions.pageSize).success(function (data){
            $scope.gridOptions.data = data;
            $scope.gridOptions.totalItems = data.totalElements;
        });
        $scope.gridOptions = {
            paginationPageSizes: [5, 10, 20],
            paginationPageSize: paginationOptions.pageSize,
            enableColumnMenus:false,
            useExternalPagination: true,
            columnDefs: [

                { field: 'originalName', minWidth: 200, width:500},
                { field: 'newName',width: '30%', maxWidth: 200, minWidth: 70},
                { field: 'counter',width: '30%', maxWidth: 200, minWidth: 70},
                { field: 'deleteKey', maxWidth: 50},
                { field: 'generationDate',width: '30%', maxWidth: 200, minWidth: 70},
                { field: 'ip'},
            ],
            onRegisterApi: function(gridApi) {
                $scope.gridApi = gridApi;
                gridApi.pagination.on.paginationChanged(
                    $scope,
                    function (newPage, pageSize) {
                        paginationOptions.pageNumber = newPage;
                        paginationOptions.pageSize = pageSize;
                        UserCRUDService.getPagination(newPage,pageSize)
                            .success(function(data){
                                $scope.gridOptions.data = data;
                                $scope.gridOptions.totalItems = data.totalElements;
                            });
                    });
            }
        };

        $scope.deleteUser = function () {
            UserCRUDService.deleteUser($scope.link)
                .then(function error(response) {
                        if (response.status !== 200) {
                            $scope.errorMessage = response.data.message;
                            $scope.link.deleteKey = ' ';
                            $scope.getAllLinks();
                            $scope.checkIP();
                            $scope.getCount();
                            $scope.errorMessage = true;
                            $scope.message = 'Failed to delete link'
                        } else {

                            $scope.link.deleteKey = ' ';
                            $scope.getAllLinks();
                            $scope.checkIP();
                            $scope.getCount();
                            $scope.message = 'Link has been deleted!';
                            $scope.errorMessage = false;
                        }

                    }
                );
        }


        $scope.getAllLinks = function () {
            UserCRUDService.getAllLinks()
                .then(function success(response) {
                        $scope.links = response.data;
                    },
                    function error(response) {
                        $scope.message = '';
                        $scope.errorMessage = 'Error getting users!';
                    });
        };

        $scope.getCount = function () {
            UserCRUDService.getCount()
                .then(function success(response) {
                        $scope.stats = response.data;
                    },
                    function error(response) {

                    });
        };
        $scope.checkIP = function () {
            UserCRUDService.checkIP()
                .then(function success(response) {
                        $scope.ip = response.data;
                    },
                    function error(response) {

                    });
        };
        $scope.checkIP();
        $scope.getCount();
        $scope.getAllLinks();
    }]);