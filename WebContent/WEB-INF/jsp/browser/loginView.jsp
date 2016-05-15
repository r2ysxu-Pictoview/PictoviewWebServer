<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html ng-app="albumViewApp">
<header>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>Login</title>
	<script type="text/javascript" src="../lib/angular.min.js"></script>
	<script type="text/javascript" src="scripts/controller/loginController.js"></script>
</header>
<body ng-controller="LoginViewController">
    <fieldset>
        <legend>Please Login</legend>
        <c:url var="loginUrl" value="/login/login.do" />
		<form name="loginForm" action="${loginUrl}" method="POST">
			<div>${ errorMessage }</div>
	        <label for="username">Username</label>
	        <input type="text" id="username" name="username"/>
	        <label for="password">Password</label>
	        <input type="password" id="password" name="password"/>
	        <div>
	            <button type="submit">Log in</button>
	        </div>
		    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		</form>
		<input type="button" value="Register">
	</fieldset>
	
	<div id="registerModal">
		<fieldset>
			<legend>Registration</legend>
			
			<form name="registerForm" ng-submit="validateRegister()" >
		        <label for="usernameReg">Username</label>
				<input type="text" id="usernameReg" ng-model="rgdata.username"
					ng-blur="validateUser()" required />
		        <label for="passwordReg">Password</label>
				<input type="password" id="passwordReg" ng-model="rgdata.password" minlength="8" required />
		        <label for="passwordConfirmReg">Confirm Password</label>
				<input type="password" id="passwordConfirmReg" ng-model="rgdata.confirmpassword" minlength="8" 
					ng-blur="validatePassword()" required />
		        <label for="emailReg">Email</label>
				<input type="email" id="emailReg" ng-model="rgdata.email" required />
		        <label for="nameReg">Name</label>
				<input type="text" id="nameReg" ng-model="rgdata.name" required />
				<label for="description" >Description</label>
				<textarea rows="4" cols="50" id="descriptionReg" ng-model="rgdata.description" ></textarea>
		    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		    	<div>
		            <button type="submit">Register</button>
		        </div>
			</form>
		</fieldset>
	</div>
</body>
</html>