<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sign In</title>
</head>
<body>
	<c:choose>
		<c:when test="${next!=null }">
			<form action="<c:url value='/signin?next=${next}' />" method="POST">	
		</c:when>
		<c:when test="${next==null }">
			<form action="<c:url value='/signin?' />" method="POST">	
		</c:when>
	</c:choose>
	
	
		<label>이메일</label><input name="email" type="email"/>
		<label>비밀번호</label><input name="pw" type="password"/>
		<button type="submit">로그인</button>
	</form>
</body>
</html>