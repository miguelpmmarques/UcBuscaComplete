<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" href="style.css" >
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
	<script src="updateAdmin.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
</head>
<body>
<s:include value="navbar.jsp"></s:include>

<div class="row">
	<div class="col-3">

	</div>
	<div class="col-6 center-block">
		<h1 class="display-2 text-center"> <b>ALL USERS</b> </h1>
		<br>
		<br>
		<div id="hotreload" class="col-12 center-block" style=" height: 30vw; overflow: auto;">
			<ul class="list-group text-center" >
				<c:forEach items="${ManageUsersModel.manageusers}" var="value">
					<li class="list-group-item text-center"> <h3> <b><c:out value="${value}" /> </b></h3>
						<c:if test="${fn:endsWith(value, 'User')}">
							<button id="${value}" class="users btn btn-light btn-outline-secondary">Make Admin</button>
						</c:if>

					</li>
				</c:forEach>
			</ul>
		</div>

	</div>
	<div class="col-3">

	</div>
</div>

</body>
</html>