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
		<c:choose>
			<c:when test="${session.loggedin == true}">
				<ul class=" navbar-right nav navbar-nav ">
					<li class="nav-item">
						<h2 class="nav-link text-white"> <b> Welcome ${session.username} </b> </h2>
					</li>
				</ul>
				<ul class=" navbar-right nav navbar-nav ">
					<li class="nav-item">
						<a class="nav-link text-white" href='<s:url action="logout" ></s:url>'>  <b> Logout</b> </a>
					</li>
				</ul>
			</c:when>
			<c:otherwise>
				<ul class=" navbar-right nav navbar-nav ">
					<li class="nav-item">
						<a class="nav-link text-white" href='<s:url action="showLogin" ></s:url>'> <b>Login </b> </a>
					</li>
					<li class="nav-item">
						<a class="nav-link text-white"> <b> Register</b> </a>
					</li>
				</ul>
			</c:otherwise>
		</c:choose>


	</div>
</nav>

	<div class="col-12 text-center">

		<img class="logo" src="logo-SD.png">
	</div>
	<div class="col-12 text-center">
		<h1> <b><s:text name="Uc Busca?" /></b> </h1>

		<br>
		<s:form action="search" method="post">
			<s:textfield cssClass = "searchlanding" name="SearchModel.seachWords" />
			<s:submit  cssClass="btn btn-light btn-outline-secondary smallmarginleft" value="Search" />


		</s:form>

	</div>
</body>
</html>