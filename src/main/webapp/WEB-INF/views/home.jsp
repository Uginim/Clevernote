<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmf"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%-- <%@ page session="false" %> --%>
<html>
<head>
	<meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
	<title>CleverNote Home</title>
	<%@include file="include/head.jsp"%>
</head>
<body>
<header>

<c:choose>
	<c:when test="${sessionScope['sessionUser']==null}">
		<a href='<c:url value="/user/signup" />'><button type="button">sign up</button></a>
		<a href='<c:url value="/signin" />'><button type="button">sign in</button>
	</c:when>
	<c:when test="${sessionScope['sessionUser']!=null}">
		<a href='#'><button type="button">프로필 수정</button></a>
		<a href='<c:url value="/signout" />'><button type="button">sign out</button>
	</c:when>
</c:choose>
</header>
<nav>
	<a href="#">기능소개</a>
	<a href="<c:url value="/board/list"/>">문의 게시판</a>
	<a href="<c:url value="/note"/>" >노트</a>
	<a href="#">개발자 블로그</a>
</nav>
<h1>
    CleverNote에 오신 것을 환영합니다~
</h1>
<p>CleverNote를 쓰시면<br> 언제든 손쉽게 메모하실 수 있습니다.</p>

<%-- <P>  The time on the server is ${serverTime}. </P> --%>
</body>
</html>
