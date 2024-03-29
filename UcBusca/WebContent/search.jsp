<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" href="style.css" >
	<link rel="stylesheet" href="bootstrap-social.css">
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.11.2/css/all.min.css" integrity="sha256-+N4/V/SbAFiW1MPBCXnfnP9QSN3+Keu+NlB+0ev/YKQ=" crossorigin="anonymous" />
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.11.2/js/all.min.js" integrity="sha256-qM7QTJSlvtPSxVRjVWNM2OfTAz/3k5ovHOKmKXuYMO4=" crossorigin="anonymous"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
	<script src="updateSearch.js"></script>
	<script src="facebookSharing.js"></script>
</head>
<body>
<s:include value="navbar.jsp"></s:include>
<div class="col-12 text-left">
	<br>
	<s:form id="myForm" cssClass = "searchanswer" action="search" method="get">
		<s:textfield id="searchfield" cssClass="searchlanding" name="words" />
		<s:submit  cssClass="btn btn-light btn-outline-secondary smallmarginleft" value="Search" />
	</s:form>
		<button class="btn btn-block btn-social btn-primary btn-facebook btn-lg m-3" style="width:40%" id="share_facebook">
			<i class="fab fa-facebook-f pt-2" style="color:#fff"></i> Share your Searches With facebook
		</button>
</div>
<br>
<c:set var = "results" value = "${SearchModel.research}" />
<p class="searchanswer">
	<i>
		Around
		${fn:length(results)}
		results found
		(0,01 seconds, faster than google)
	</i>
</p>
<div class="dotline"></div>
<br>

<div id="searches">
	<c:forEach items="${results}" var="element">

		<div class="searchanswer">
			<h4 class="title">
				<a  href='${element.get('url')}'>
					<c:out value="${element.get('title')}" />

				</a>
			</h4>
			<a class="linkurl" style="color:limegreen;" href='${element.get('url')}'>
				<c:out value="${element.get('url')}" />
				<b> <h7 class="language"></h7> </b>

			</a>
			<p class="description">
				<c:out value="${element.get('description')}" />
			</p>
		</div>
		<br>
	</c:forEach>

</div>


</body>
</html>