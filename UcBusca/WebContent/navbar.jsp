<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<head>
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
		<link rel="stylesheet" href="style.css" >
		<script src="realTimeNotifications.js"></script>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Uc Busca</title>
	</head>

</head>
<body>

<nav class="navbar navbar-center navbar-expand-sm bg-warning navbar-dark">
	<div class="container-fluid">
		<ul class=" nav navbar-nav navbar-left">
			<li class="nav-item">
				<a href="/UcBusca"><img class="logoNavBar" src="logo-SD.png"></a>
			</li>
		</ul>

		<c:choose>
			<c:when test="${session.loggedin == true}">
				<p id="username" style="visibility: hidden">${session.username}</p>
				<ul class=" navbar-right nav navbar-nav ">
					<c:choose>
						<c:when test="${session.admin == true}">
							<li class="nav-item">
								<a class="nav-link text-dark myTeamNav" href='<s:url action="manageusers" ></s:url>'>  <b>Manage users</b> </a>
							</li>
							<li class="nav-item">
								<a class="nav-link text-dark myTeamNav" href='<s:url action="systeminfo" ></s:url>'>  <b>System info</b> </a>
							</li>
							<li class="nav-item">
								<a class="nav-link text-dark myTeamNav"  href='<s:url action="showAddurl" ></s:url>'>  <b>Add url to UcBusca</b> </a>
							</li>
						</c:when>
					</c:choose>
					<li class="nav-item">
						<a class="nav-link text-white myTeamNav" href='<s:url action="history" ></s:url>'>  <b> My History</b> </a>
					</li>
					<li class="nav-item">
						<a class="nav-link text-white myTeamNav" href='<s:url action="logout" ></s:url>'>  <b> Logout</b> </a>
					</li>
				</ul>
			</c:when>
			<c:otherwise>


				<ul class=" navbar-center nav navbar-nav ">
					<li class="nav-item">
						<h1 class="nav-link text-white"> <b>Spaguetti Search</b></h1>
						<h7 id="username" style="display: none">Anonimous</h7>
					</li>
				</ul>
				<ul class=" navbar-right nav navbar-nav ">
					<li class="nav-item">
						<a class="nav-link text-white myTeamNav" href='<s:url action="showLogin" ></s:url>'> <b>Login </b> </a>
					</li>
					<li class="nav-item">
						<a class="nav-link text-white myTeamNav" href='<s:url action="showRegister" ></s:url>'> <b> Register</b> </a>
					</li>
				</ul>
			</c:otherwise>
		</c:choose>


	</div>
</nav>

<s:if test="hasActionErrors()">
	<div class="alert alert-danger" role="alert">
		<h5> <s:actionerror /></h5>
	</div>

</s:if>
<s:if test="hasActionMessages()">
	<div class="alert alert-success" role="alert">
		<h5> <s:actionmessage/></h5>
	</div>
</s:if>
</body>
</html>