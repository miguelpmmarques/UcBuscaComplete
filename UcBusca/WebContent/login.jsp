<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="bootstrap-social.css">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.11.2/css/all.min.css" integrity="sha256-+N4/V/SbAFiW1MPBCXnfnP9QSN3+Keu+NlB+0ev/YKQ=" crossorigin="anonymous" />
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"
            integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q"
            crossorigin="anonymous"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"
            integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
            crossorigin="anonymous"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.11.2/js/all.min.js" integrity="sha256-qM7QTJSlvtPSxVRjVWNM2OfTAz/3k5ovHOKmKXuYMO4=" crossorigin="anonymous"></script>
    <script src="login.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

    <title>Uc Busca</title>
</head>
<body>


<s:if test="hasActionErrors()">
	<div class="alert alert-danger" role="alert">
		<h5> <s:actionerror /></h5>
	</div>


</s:if>
<div class="row" style="height: 50px;"></div>
<div class="row">

    <div class="col-3"></div>

    <div class="card bg-warning col-6 mx-auto text-center">
        <div class="col-12 text-center">

            <img class="logo" src="logo-SD.png">

            <h1><b><s:text name="LOGIN"/></b></h1>
            <br>
            <s:form action="login" method="post">
                <h3>Username:</h3>
                <s:textfield cssClass="searchlanding" name="username"/>
                <h3>Password:</h3>
                <s:password cssClass="searchlanding" name="password"/>
                <br>
                <br>
                <s:submit cssClass="btn btn-light btn-outline-secondary smallmarginleft" value="Login"/>
                <input type="button" class="btn btn-light btn-outline-secondary smallmarginleft" value="Back"
                       onclick="window.location.href = '/Prime'">
            </s:form>
        </div>
        <br>
    </div>
    <div class="col-3"></div>
</div>
</div>

<div class="row">
    <div class="col-3"></div>
    <div class="col-6">

        <button class="btn btn-block btn-social btn-facebook w-25 h-50 pb-3 m-3" id="signin_facebook">
            <i class="fab fa-facebook-f pt-2" style="color:#fff"></i> Sign in with Facebook
        </button>
    </div>

</div>

</body>
</html>
