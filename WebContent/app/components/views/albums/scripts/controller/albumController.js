var albumApp = angular.module('albumViewApp', []);

// Album Controller
var categories = [{'category' : 'tags', 'tags' : ''}];
var albumPath = [{'id' : 0, 'name' : 'All'}];

let albumList = new AlbumList($('#albumList'));

albumApp.controller('AlbumViewController', ['$scope','$http', function($scope, $http) {
    let albumId = 0;
    let maxCategory = 14;

    $scope.categories = categories;
    $scope.userControls = PageConfig.userControls;

    const defaultAlbum = {
        albumName: '',
        albumSub: '',
        description: '',
        parentId: 0,
        _csrf: csrf_token,
        file: {}
     };

    $scope.newAlbum = defaultAlbum;

    // Search
    $scope.showCriteria = function() {
        $("#searchAdditionalCriteria").toggle();
        $("#albumSearchInput").toggleClass('fullshow');
    }

    $scope.addCriteria = function() {
        if (maxCategory >= 0){
            maxCategory--;
        }
    }

    $scope.minusCriteria = function(index) {
        $scope.categories.splice(index, 1);
    }

    $scope.submitSearch = function() {
        let name =  matchSpaceQuotes($scope.search.name);

        let searchData = { "names" : name, "cate" : []};
        for (let i = 0; i < categories.length; i++) {
            let tags = matchSpaceQuotes(categories[i].tags);
            searchData.cate.push({ "category" : categories[i].category, "tags" : tags });
        }

        $http.post(PageConfig.searchUrl, searchData).then(function successCallback(response) {
            console.log(response);
            albums[0] = response.data;
          }, function errorCallback(response) {
        });

        return false;
    }

    function matchSpaceQuotes(str) {
        return str.match(/(".*?"|[^" \s]+)(?=\s* |\s*$)/g) || [];
    }

    // Expand subAlbums
    $scope.expandSubalbums = function(albumData) {

        if (!albums[albumData.id]) {
            $http.get('albums/get.do', { params :{
                "albumId" : albumData.id
            }}).then(function successCallback(response) {
                albums[albumData.id] = response.data;
                $scope.albumPath.push({'id' : albumData.id, 'name' : albumData.name});
            }, function errorCallback(response) {});
        } else {
            if ($scope.albumPath.length > 0 && $scope.albumPath[$scope.albumPath.length - 1].id != albumData.id) {
                $scope.albumPath.push({'id' : albumData.id, 'name' : albumData.name});
            }
        }
    }

    $scope.expandSubalbumsPath = function(id, index) {
        $scope.albumChunks = chunkify(albums[id]) || [];
        $scope.albumPath.length = index + 1;
    }

    // Tags & Categories

    $scope.fetchCategories = function(albumData) {
        if (!albumData.categories) {
            // Get album tags
            if (albumData.categories == null) {
                $http.get('tag/get.do', { params : {
                    "albumId" : albumData.id
                }}).then( function successCallback(response) {
                    albumData.categories = response.data;
                }, function errorCallback(response) {
                    albumData.categories = [];
                });
            }
        }
        let elem = $('#albumCellCategory-' + albumData.id);
        if (elem.attr('style') !== undefined && elem.attrs('style')) {
            elem.removeAttr('style');
        }
        elem.toggleClass('fadeout');
    }

    $scope.hideAlbumInfoPanel = function() {
        $('.albumCellInfoContainer').css('display', 'none');
    }

    $scope.albumShowAddNewCategory = function(albumData) {
        $('#albumNewCategoryOption-' + albumData.id).toggle();
    }

    $scope.albumAddNewCategory = function(albumData) {
        let categoryName = $('#albumExtraCategory-' + albumData.id).val();
        if (categoryName && categoryName != '')
            albumData.categories.push({"category" : categoryName, "tags" : []});
    }

    $scope.albumAddNewTag = function(albumid, categoryData) {
        $('#albumExtraTag-' + albumid + '-' + categoryData.category).toggle();
    }

    $scope.albumSaveNewTag = function(albumData, categoryData, index) {
        $.get('/PictureViewerWebServer/albums/tag/create.do', {
            "albumId" : albumData.id,
            "categoryName" : categoryData.category,
            "tags" : categoryData.newTags
        }).then(function() {
            $http.get('tag/get.do', { params : {
                "albumId" : albumData.id
            }}).then( function successCallback(response) {
                albumData.categories = response.data;
            }, function errorCallback(response) {});
        }).fail(function() {
            alert("Response Failed");
        });
    }

    // Create Albums
    $scope.modalDialog = 'modalDialog';
    $scope.showCreateAlbumModal = function(parentId) {
        $scope.modalDialog = 'modalDialog showDialog';
        $('#newAlbumParentId').val(parentId);
    }

    $scope.closeAlbumModal = function() {
        $scope.modalDialog = 'modalDialog';
        $('#newAlbumParentId').val(0);
        return false;
    }

    $scope.createAlbum = function(data) {
        console.log('data', data)
        let formData = new FormData();
        Object.keys(data).forEach(key => formData.append(key, data[key]));
        for (var pair of formData.entries()) {
            console.log(pair[0]+ ', ' + pair[1]);
        }
        $http.post('/PictureViewerWebServer/albums/create.do', formData, {
            transformRequest: angular.identity,
                headers: { 'enctype': 'multipart/form-data', 'Content-Type': undefined }
            }).success(function(){ console.log('success') }).error(function(){ alert("unsuccesful uploading") })
        $scope.newAlbum = defaultAlbum
    }

    // Album Subscription

    $scope.subscribeToAlbum = function(albumid) {
        let subscribeData = { "albumId" : albumid };
        $.post('/PictureViewerWebServer/albums/subscribe.do', subscribeData)
            .then(function successCallback(response) {
                console.log(response);
              }, function errorCallback(response) {});
    }

    // Get album
    $http.get(PageConfig.fetchUrl, { params :{
        "albumId" : albumId
        }}).then(function successCallback(response) {
        	albumList.loadData(response.data);
            $scope.albums = albumList.albums;
      }, function errorCallback(response) {
          console.log(response);
    });

    $scope.albumPath = albumPath;
    $scope.selectedIndex = 0;
}]);