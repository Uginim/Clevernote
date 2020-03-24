<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav>
	<ul class="list-unstyled list-group list-group-horizontal">
	<li class="mr-2 px-1"> <a class="text-white" href="#">기능소개</a> </li>
	<li class="mr-2 px-1"> <a class="text-white" href="<c:url value="/board/list"/>">사용자 게시판</a> </li>
	<li class="mr-2 px-1"> <a class="text-white" href="<c:url value="/note"/>" >노트</a> </li>
	<li class="mr-2 px-1"> <a class="text-white" href="http://uginim.com">개발자 블로그</a> </li>
	</ul>
</nav>