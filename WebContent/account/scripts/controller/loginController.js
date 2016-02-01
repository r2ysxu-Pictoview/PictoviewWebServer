var albumApp = angular.module('albumViewApp', []);


albumApp.controller('LoginViewController', ['$scope','$http', '$location', function($scope, $http, $location) {
	
	$scope.loginData = {};
	
	$scope.verifyLogin = function() {
		var credentials = $scope.loginData;
		
		var passkey = credentials.password;
		
		$http.post('login/verify.do', { "username" : credentials.username, "passkey" : passkey }).then(function successCallback(response) {
			console.log(response);
			if (response.data == -1) {
				$location.path("/albums/albums.do");
			} else {
				$location.path("/albums/albums.do");
			}
		  }, function errorCallback(response) {
		});
		
		return true;
	}
}]);