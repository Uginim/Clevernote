<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header>
  	<div class="bg-dark collapse" id="navbarHeader" style="">
    	<div class="container">
		      <div class="row mb-0">
			        <div class="col-sm-8 py-4">
			        	<h4 class="text-white">CleverNote</h4>        	
			          	<%@include file="/WEB-INF/views/include/nav.jsp"%>          
			        </div>
			        <div class="col-sm-4 py-4">
						<div class="row justify-content-md-end">							
					        <c:choose>	        
							<c:when test="${sessionScope['sessionUser']==null}">
								<a class="btn btn-light mr-2" href='<c:url value="/user/signup" />'>sign up</a>
								<button id="global-signin-btn" class="btn btn-light mr-2" >sign in</button>
<%-- 								<a class="btn btn-light mr-2" href='<c:url value="/signin" />'>sign in</a> --%>
							</c:when>
							<c:when test="${sessionScope['sessionUser']!=null}">
								<a class="btn btn-light mr-2" href='#'>프로필 수정</a>
<%-- 								<a class="btn btn-light mr-2" href='<c:url value="/signout" />'>sign out</a> --%>
									<button id="global-signout-btn" class="btn btn-light mr-2">sign out</button>
							</c:when>		
							</c:choose>
						</div>
					</div>
		      </div> 
      	</div>   
  	</div>
  <div class="navbar navbar-dark bg-dark shadow-sm">
    <div class="container d-flex justify-content-between">
      <a href="<c:url value="/"/>" class="navbar-brand d-flex align-items-center">
		<img src="${pageContext.request.contextPath }/resources/img/temp_white.svg" class="logo" alt="logo">
        <strong>CleverNote</strong>
      </a>
      <button class="navbar-toggler collapsed" type="button" data-toggle="collapse" data-target="#navbarHeader" aria-controls="navbarHeader" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
    </div>
  </div>
</header>
<!-- <header id="main-header" class="container d-flex justify-content-between"> -->

<%-- 	<a href="<c:url value="/"/>" > --%>
<%--     <img src="<c:url value="resources/img/temp_.svg" />" class="logo" alt="logo"> --%>
    
<!--     </a> -->
	
</header>