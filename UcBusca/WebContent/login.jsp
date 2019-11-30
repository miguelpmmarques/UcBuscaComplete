<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
	<link rel="stylesheet" href="style.css" >
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Uc Busca</title>
</head>
<body>
<nav class="navbar navbar-center navbar-expand-sm bg-warning navbar-dark">
	<div class="container-fluid">
		<ul class=" nav navbar-nav navbar-left">
			<li class="nav-item">
				<a href="/Prime"><img class="logoNavBar" src="logo-SD.png"></a>
			</li>
		</ul>
	</div>
</nav>
<h1>

</h1>
	<div class="col-12 text-center">

		<img class="logo" src="logo-SD.png">
	</div>
	<div class="col-12 text-center">
		<h1> <b><s:text name="LOGIN" /></b> </h1>
		<br>
		<s:form action="login" method="post">
			<h3>Username:</h3>
			<s:textfield cssClass = "searchlanding" name="username" />
			<h3>Password:</h3>
			<s:password cssClass = "searchlanding" name="password" />
			<br>
			<s:submit cssClass="btn btn-light btn-outline-secondary smallmarginleft" value="Login" />
		</s:form>
	</div>
</body>
</html>