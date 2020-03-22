<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${!empty sessionUser}">
	<jsp:forward page="/"></jsp:forward>	
</c:if>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/views/include/meta.jsp"%>
<meta charset="UTF-8">
<title>로 그 인</title>
	<%@include file="/WEB-INF/views/include/meta.jsp"%>
  	<link rel="stylesheet" href="<c:url value="/resources/css/signin.css" />">
<%--   <script src="<c:url value="/resources/js/login.js" />"></script> --%>

<title>Sign In</title>
</head>
<body>
	<header>
    <img src="<c:url value="/resources/img/temp_.svg"/>" class="logo" alt="logo">
    <h2>CleverNote</h3>
  </header>
  
	<div class="container">
	<div class="login-wrapper">
		<c:choose>
			<c:when test="${next!=null }">
				<form id="loginForm" action="<c:url value='/signin?next=${next}' />" method="POST">	
			</c:when>
			<c:when test="${next==null }">
				<form  id="loginForm" action="<c:url value='/signin?' />" method="POST">	
			</c:when>
		</c:choose>
			<div><h3 class="login-title">Sign In!</h3></div>
	        <hr>
			<div class="login-content col px-0">
				<div class="row">
					<input name="email" type="email" placeholder="이메일"/>
				</div>
				<div><span class="errmsg" id="idMsg"></span></div>
				<div class="row">
					<input name="pw" type="password" placeholder="비밀번호"/>
				</div>
				<div><span class="errmsg" id="pwMsg">${error}${errMsg}</span></div>		
				<div class="row"><button type="submit" class="loginBtn">로그인</button></div>		
			</div>
			<div class="keeping-login-info">
	          <label for=""><input type="checkbox" name="keeping-login-info" id="keeping-login-info">로그인 정보 기억하기</label>
	        </div>
	        <button type="button" class="google-oauth">Google로 로그인</button>
		</form>
		<div class="find_info">
	        <a href="#">아이디 찾기</a>
	        <span>|</span>
	        <a href="#">비밀번호 찾기</a>
	        <span>|</span>
	        <a href="signup.html">회원가입</a>
	      </div>
	</div>
	</div>
</body>
</html>

