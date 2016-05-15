<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app="albumViewApp">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>User Information</title>
</head>
<body ng-controller="UserInfoViewController">
	<fieldset>
		<label>${ userData.username }</label>
		<label>${ userData.name }</label>
		<label>${ userData.email }</label>
	</fieldset>
	<fieldset>
		<label>${ userData.gender }</label>
		<label>${ userData.address }</label>
		<label>${ userData.description }</label>
	</fieldset>
</body>
</html>