var albumApp = angular.module('albumViewApp', []);

// Album Controller
var albums = [];
var categories = [{'category' : 'tags', 'tags' : ''}];
var albumPath = [{'id' : 0, 'name' : 'All'}];

let albumFlow = new AlbumFlow($('#albumList'));

albumApp.controller('AlbumViewController', ['$scope','$http', function($scope, $http) {
	let albumId = 0;
	let maxCategory = 14;

	$scope.categories = categories;
	$scope.userControls = PageConfig.userControls;

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
	
	// Album Subscription
	
	$scope.subscribeToAlbum = function(albumid) {
		let subscribeData = { "albumId" : albumid };
		$.post('/PictureViewerWebServer/albums/subscribe.do', subscribeData)
			.then(function successCallback(response) {
				console.log(response);
			  }, function errorCallback(response) {});
	}

	$scope.selectAlbum = function(index) {
		selectAlbum = albumFlow.selectAlbum;
	}
	
	// Get album
	$http.get(PageConfig.fetchUrl, { params :{
		"albumId" : albumId
		}}).then(function successCallback(response) {
			albums[0] = response.data;
			$scope.albums = albums[0] || [];
			albumFlow.loadData($scope.albums);
	  }, function errorCallback(response) {
		  console.log(response);
	});

	$scope.albumPath = albumPath;
	$scope.selectedIndex = 0;
}]);

albumApp.directive('alFlow', ['$document', function($document) {
	return {
		link: function(scope, element, attr) {
			element.bind('mousewheel', function(event) {
				var event = window.event || event; // old IE support

				let target = $('.flow_selected');
				let source = $('#albumList');
				if (event.wheelDelta > 0) {
					scope.selectAlbum(scope.selectedIndex - 1);
				} else if ( event.wheelDelta < 0) {
					scope.selectAlbum(scope.selectedIndex + 1);
				}
				scope.$apply();

                //element.scrollLeft(element.scrollLeft() - (event.wheelDelta / 2) );
                event.preventDefault();
			});
		}
	}
}]);