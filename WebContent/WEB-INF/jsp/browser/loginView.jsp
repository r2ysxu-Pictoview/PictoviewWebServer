<html ng-app="albumViewApp">
<header>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>Login</title>
	<script type="text/javascript" src="../lib/angular.min.js"></script>
	<script type="text/javascript" src="scripts/controller/loginController.js"></script>
</header>
<body ng-controller="LoginViewController">
	<form name="loginForm" ng-submit="verifyLogin()">
		<label for="username">Username</label>
		<input type="text" id="username" name="username" ng-model="loginData.username" />
		<label for="password">Password</label>
		<input type="password" id="password" name="passkey" ng-model="loginData.password" />
		<input type="submit" />
	</form>
</body>
</html>