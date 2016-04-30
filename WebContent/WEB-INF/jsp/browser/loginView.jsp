<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html ng-app="albumViewApp">
<header>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>Login</title>
	<script type="text/javascript" src="../lib/angular.min.js"></script>
	<script type="text/javascript" src="scripts/controller/loginController.js"></script>
</header>
<body ng-controller="LoginViewController">
    <c:url var="loginUrl" value="account/login.do" />
	<form name="loginForm" action="${loginUrl}" method='POST'>
		<c:if test="${param.error != null}">
		    <div class="alert alert-danger">
		        <p>Invalid username and password.</p>
		    </div>
		</c:if>
		<c:if test="${param.logout != null}">
		    <div class="alert alert-success">
		        <p>You have been logged out successfully.</p>
		    </div>
		</c:if>
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		<label for="username">Username</label>
		<input type="text" id="username" name="username"  />
		<label for="password">Password</label>
		<input type="password" id="password" name="password" />
		<input type="submit" value="submit" />
	</form>
</body>
</html>