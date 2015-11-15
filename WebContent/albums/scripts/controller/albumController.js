var albumApp = angular.module('albumViewApp', []);

albumApp.controller('albumViewController', function($scope, $http) {
	var albumId = 0;
	
	$http.get('albums/get.do', { params :{
		"albumId" : albumId
	}}).then(function successCallback(response) {
		console.log(response);
	  }, function errorCallback(response) {
		  console.log(response);
	  });
});