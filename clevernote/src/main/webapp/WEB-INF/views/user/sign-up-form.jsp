<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sign Up</title>
</head>
<body>
	<form:form modelAttribute="newUserInfo" action="${pageContext.request.contextPath}/user/signup" method="post" accept-charset="UTF-8" >
		<form:label path="email">이메일</form:label> <form:input path="email" type="email"/>		
		<form:label path="pw">비밀번호</form:label> <form:input path="pw" type="password"/>		
		<form:label path="username">사용자 이름</form:label> <form:input path="username" type="text"/>	
		<form:button>submit</form:button>		
	</form:form>
</body>
</html>