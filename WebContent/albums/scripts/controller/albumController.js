var albumApp = angular.module('albumViewApp', []);

// Album Controller
var albums = [];
var categories = [{'category' : 'tags', 'tags' : ''}];
var albumPath = [{'id' : 0, 'name' : 'All'}];

albumApp.controller('AlbumViewController', ['$scope','$http', function($scope, $http) {
	var albumId = 0;
	var maxCategory = 14;

	$scope.categories = categories;
	
	var albumId = 0;

	$scope.addCriteria = function() {
		if (maxCategory >= 0){
			$scope.categories.push({'category' : '', 'tags' : ''});
			maxCategory--;
		}
	}
	
	$scope.submitSearch = function() {
		var name =  matchSpaceQuotes($scope.search.name);
		
		var searchData = { "names" : name, "cate" : []};
		for (var i = 0; i < categories.length; i++) {
			var tags = matchSpaceQuotes(categories[i].tags);
			searchData.cate.push({ "category" : categories[i].category, "tags" : tags });
		}
		
		$http.post('search.do', searchData).then(function successCallback(response) {
			console.log(response);
			$scope.albumList = response.data;
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
			}, function errorCallback(response) {});
		}
		if ($scope.albumPath.length > 0 && $scope.albumPath[$scope.albumPath.length - 1].id != albumData.id) {
			$scope.albumPath.push({'id' : albumData.id, 'name' : albumData.name});
			$scope.albumChunks = chunkify(albums[albumData.id]) || [];
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
		var elem = $('#albumCellSubtitle-' + albumData.id);
		$('#albumCellCategory-' + albumData.id).toggleClass('rollout');
		elem.hasClass('albumCell_subtitle_left') ? elem.toggleClass('rolloutSlider_left') : elem.toggleClass('rolloutSlider_right');
	}
	
	$scope.albumShowAddNewCategory = function(albumData) {
		$('#albumNewCategoryOption-' + albumData.id).toggle();
	}
	
	$scope.albumAddNewCategory = function(albumData) {
		var categoryName = $('#albumExtraCategory-' + albumData.id).val();
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
	
	// Album Subscription
	
	$scope.subscribeToAlbum = function(albumid) {
		var subscribeData = { "albumId" : albumid };
		$.post('/PictureViewerWebServer/albums/subscribe.do', subscribeData)
			.then(function successCallback(response) {
				console.log(response);
			  }, function errorCallback(response) {});
	}
	
	// Get Album
	
	var chunkify = function(albumList) {
		var i = 0, chunks = 2;
		var chunkList = [];
		for (i < 0; i < albumList.length; i += chunks) {
			chunkList.push(albumList.slice(i, i + chunks));
		}
		return chunkList;
	};
	
	// Get album list
	$http.get('albums/get.do', { params :{
		"albumId" : albumId
		}}).then(function successCallback(response) {
			$scope.albumChunks = chunkify(response.data) || [];
	  }, function errorCallback(response) {
		  console.log(response);
	  });

	$scope.albumChunks = [];
	$scope.albumPath = albumPath;

}]);