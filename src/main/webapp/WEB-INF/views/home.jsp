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
	<%@include file="/WEB-INF/views/include/meta.jsp"%>
	<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/home.css" />
	
</head>
<body>

	
	<%@include file="/WEB-INF/views/include/header.jsp"%>
	
<main role="main">

	<section class="jumbotron text-center">
    <div class="container">
      <h1> CleverNote에 오신 것을 환영합니다~</h1>
      <p class="lead text-muted">CleverNote를 쓰시면<br> 언제든 손쉽게 메모하실 수 있습니다.</p>
      <p>      
        <a href="<c:url value="/user/signup" />" class="btn btn-primary my-2">가입하기</a>
        <a href="<c:url value="/signin" />" class="btn btn-secondary my-2">로그인 하기</a>
      </p>
    </div>
  </section>
		<article id="introduction-of-features" class="container justify-content-md-center">
            <h1>
                서비스 소개
            </h1>
            <a class="featrue-image" href="#">
                <h4>메모</h4>
                <img src="<c:url value="resources/img/memo.svg"/>" alt="">
            </a>
            <a class="featrue-image" href="#">
                <h4>공유</h4>
                <img src="<c:url value="resources/img/share.svg"/>" alt="">
            </a>
            <a class="featrue-image" href="#">
                <h4>일정</h4>
                <img src="<c:url value="resources/img/schedule.svg"/>" alt="">
            </a>
            <a class="featrue-image" href="#">
                <h4>템플릿</h4>
                <img src="<c:url value="resources/img/template.svg"/>" alt="">
            </a>
        </article>
	

	
</main>
	<%@ include file="/WEB-INF/views/include/footer.jsp"  %>


</body>
</html>
