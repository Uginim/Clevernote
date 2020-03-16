<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmf" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>${board.title}</title> 
<style>
    
    input,select,textarea{
        border: none;
    }    
   
    .no-border{
        border:none;
    }
 
    
    
    .attachments>td button{
        display: inline-block;
        width:auto;
    }
    .attachments>td button.invisible-mode{
        display: none;
    }
</style>
	<%@include file="/WEB-INF/views/include/meta.jsp"%>
	<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/comment.css" />
	<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/view-page.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/comment.js" ></script>

</head>
<body>
<%@include file="/WEB-INF/views/include/header.jsp"%>
<main class="py-5">
	<div class="container">
		<div class="row py-2">
			<h1>${board.title}</h1>
		</div>    
		<div class="row justify-content-between">
			<fmf:formatDate value="${board.createdAt}" pattern="yyyy-MM-dd hh:mm" var="createdAt"/>
			    <div class="author col-sm-auto">
			        <span>&nbsp;by</span>
			        <span class="font-weight-bold"  >${board.username}</span>
			    </div> 			
			    <div class="col-2-auto text-muted">${createdAt}</div>    
			    <div class="category col-auto ">
			        <span>${board.type.name}</span>
			    </div>
				<div class="offset-md-6 col-sm-auto">
					<span>조회수</span>
				    <span>${board.hit}</span>    
				</div>
			    
		</div>
		<hr>

		<div class="content row mb-5 border-bottom shadow p-4 mb-5 bg-white rounded">
			<%-- <span><label>내용</label></span> --%>
			<p class="p-3"><pre >${board.content}</pre></p>
		</div> 
		    
		 
		<div class=" tools row mb-3">
				<div class="tools btn-group">
					<button class="btn btn-secondary" type="button" id="replyBtn" data-returnPage="${returnPage }" data-bnum="${board.postNum}">답글</button> 
					<c:if test="${sessionUser.userNum == board.userNum}">               
						<button class="btn btn-secondary" type="button" id="deleteBtn" data-returnPage="${returnPage }" data-bnum="${board.postNum}">삭제</button>
						<button class="btn btn-secondary" type="button" id="modifyBtn" data-returnPage="${returnPage }" data-bnum="${board.postNum}">수정</button>
					</c:if>
					<button class="btn btn-secondary" type="button" data-returnPage="${returnPage }" id="listBtn" >목록</button>
				</div>
			</div>
		
			<div class="attachments row mb-2" >
				<div class="col">
					<div class="row mb-2">
						<h3>첨부파일목록</h3>
					</div>				
					<div class="row mb-1">
						<table class="table table-sm">
						  <thead>
						    <tr>
						    	<th scope="col">파일명</th>
						      	<th scope="col">크기</th>
						      	<th scope="col">타입</th>						      
						   </tr>
						  </thead>
						    <tbody>						  
<!-- 						<ul id="file-list" class="list-unstyled"> -->
							<c:if test="${!empty files}">
								<c:forEach var="file" items="${files}">
<%-- 										<li class="file-item" data-fid="${file.attachmentNum}"> --%>
									<tr>
								
									<td><a class="text-black" href="/board/file/${file.attachmentNum}">${file.name }</a></td><td>${file.fileSize }</td><td>${file.mimetype }</td>
									</tr> 
<!-- 									</li> -->
								</c:forEach>
							</c:if>
<!-- 						</ul> -->
						</tbody>
						
						</table>
						
					</div>
				</div>
			        
			</div> 
		
			<%@ include file="comment.jsp"  %>
	</div>
</main>
<%@ include file="/WEB-INF/views/include/footer.jsp"  %>	
    
</body>
</html>
