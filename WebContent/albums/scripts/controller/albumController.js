var albumApp = angular.module('albumViewApp', []);

// Search Controller

var categories = [{'category' : 'tags', 'tags' : ''}];

albumApp.controller('AlbumSearchController', function($scope) {
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
});

albumApp.controller('AlbumViewController', ['$scope','$http', function($scope, $http) {
	var albumId = 0;

	$scope.expandAlbum = function(albumData) {
		if (albumData.subalbums.length == 0){
			
			$http.get('albums/get.do', { params :{
				"albumId" : albumData.id
				}}).then(function successCallback(response) {
					albumData.subalbums = response.data;
			  }, function errorCallback(response) {
				  console.log(response);
			  });
		} else {
			var elem = document.getElementById('subAlbums-' + albumData.id);
			if (elem.style.display == 'block') elem.style.display = 'none';
			else elem.style.display = 'block';
		}
	}
	
	$http.get('albums/get.do', { params :{
		"albumId" : albumId
		}}).then(function successCallback(response) {
			$scope.albumList = response.data;
	  }, function errorCallback(response) {
		  console.log(response);
	  });

	$scope.albumList = [];

}]);