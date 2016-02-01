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

	$scope.expandAlbum = function(albumData) {
		
		// Get sub albums
		if (albumData.subalbums.length == 0){
			
			$http.get('albums/get.do', { params :{
				"albumId" : albumData.id
			}}).then(function successCallback(response) {
				albumData.subalbums = response.data;
			}, function errorCallback(response) {});
		} else {
			var elem = document.getElementById('subAlbums-' + albumData.id);
			if (elem.style.display == 'block') elem.style.display = 'none';
			else elem.style.display = 'block';
		}
		
		// Get album tags
		if (albumData.categories == null) {
			$http.get('tag/get.do', { params : {
				"albumId" : albumData.id
			}}).then( function successCallback(response) {
				albumData.categories = response.data;
			}, function errorCallback(response) {});
			
			albumData.categories =[];
		} else {
			var elem = document.getElementById('albumTagInfo-' + albumData.id);
			if (elem.style.display == 'block') elem.style.display = 'none';
			else elem.style.display = 'block';
		}
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