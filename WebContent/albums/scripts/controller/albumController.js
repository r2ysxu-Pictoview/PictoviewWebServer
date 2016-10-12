var albumApp = angular.module('albumViewApp', []);

// Search Controller
var categories = [{'category' : 'tags', 'tags' : ''}];

// Album Controller
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
		
		console.log(searchData);
		
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

	$scope.albumExpand = function(albumData) {

		$('#albumExpand-' + albumData.id).toggle();

		// Get sub albums
		if (albumData.subalbums.length == 0){
			$http.get('albums/get.do', { params :{
				"albumId" : albumData.id
			}}).then(function successCallback(response) {
				albumData.subalbums = response.data;
			}, function errorCallback(response) {});
		}

		// Get album tags
		if (albumData.categories == null) {
			$http.get('tag/get.do', { params : {
				"albumId" : albumData.id
			}}).then( function successCallback(response) {
				albumData.categories = response.data;
			}, function errorCallback(response) {});

			albumData.categories =[];
		}

		// Get album description
		if (albumData.description == null) {
			$http.get('albums/info.do', {params : { "albumId" : albumData.id }
			}).then( function successCallback(response) {
				albumData.description = response.data.description;
			}, function errorCallback(response){});
		}
	} //
	
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
		console.log(categoryData);
	}
	
	$scope.albumSaveNewTag = function(albumid, categoryData) {
		console.log(categoryData.newTags);
		$.get('/PictureViewerWebServer/albums/tag/create.do', {
			"albumId" : albumid,
			"categoryName" : categoryData.category,
			"tags" : categoryData.newTags
		}, function(response) {
		}).fail(function() {
			alert("Response Failed");
		});
	}

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
	
	$scope.subscribeToAlbum = function(albumid) {
		var subscribeData = { "albumId" : albumid };
		$.post('/PictureViewerWebServer/albums/subscribe.do', subscribeData)
			.then(function successCallback(response) {
				console.log(response);
			  }, function errorCallback(response) {}
		);
	}
	
	// Get album list
	$http.get('albums/get.do', { params :{
		"albumId" : albumId
		}}).then(function successCallback(response) {
			$scope.albumList = response.data;
	  }, function errorCallback(response) {
		  console.log(response);
	  });

	$scope.albumList = [];

}]);