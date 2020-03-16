<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>${boardVO.title }</title>
<%@include file="/WEB-INF/views/include/meta.jsp"%>
<script src="<c:url value='/resources/js/modifying-form.js'/>"> </script>
</head>
<body>
<%@include file="/WEB-INF/views/include/header.jsp"%>
<main class="py-4">
        <div class="container">
	<h3>게시글 수정</h3>
	<form:form modelAttribute="board" action='${pageContext.request.contextPath}/board/modify/${returnPage}' method="POST" id="modify-form" enctype="multipart/form-data">
		<form:input type="hidden" path="postNum" />
		<form:input type="hidden" path="username" />
		<form:input type="hidden" path="userNum" />
		
		<div class="form-group row mb-1 px-3">
			<form:input class="form-control form-control-lg border-0" path="title" type="text" cols="70" placeholder="제목을 입력하세요.."/> 
		</div>
		<form:errors cssClass="error" path="title"/>
		<div class="form-group row mb-2">
			<form:label class="col-sm-1" path="type.typeNum">분류</form:label>                            
			<div class="col-sm-11">
				<form:select path="type.typeNum" class="form-control" id="type" cols="70">
					<option value="0">== 선 택 ==</option>
					<form:options path="type.typeNum" items="${boardTypes}" itemValue="typeNum" itemLabel="name" />
				</form:select>          
			</div>              								
		</div>
		<form:errors cssClass="error" path="type.typeNum"/>                       
		<div class="form-group row mb-2">
			<label class="col-sm-1">작성자</label>
			<div class="col-sm-11">
				<input class="form-control" value="${sessionUser.username}" readonly cols="70"/>
			</div>
        </div>
        <form:errors path="content" cssClass="error"/>
		<div class="form-group mb-2">
			<label>내용</label>
			<form:textarea id="post-content-editor" class="form-control" path="content" cols="70" rows="40"></form:textarea>
		</div>
		
		
		<div class="custom-file  mb-2">
			<label class="custom-file-label">첨부</label><input id="attachments"  type="file" class="custom-file-input" name="files" multiple/>
			<form:errors path="files"/>
		</div>
		<ul id="fileList" class="list-unstyled  list-group list-group-flush"></ul>
		
		<div class="row justify-content-end mb-5">
			<div class="btn-group">            	
				<form:button class="btn btn-outline-primary" id="submit-btn" data-returnPage="${returnPage }"
				data-bnum="${board.postNum}">수정</form:button>
				<form:button type="button" class="btn btn-outline-primary" data-returnPage="${returnPage }" data-bnum="${board.postNum}" id="cancel-btn">취소</form:button>
				<form:button type="button" class="btn btn-outline-primary" data-returnPage="${returnPage }" id="list-btn">목록</form:button>
			</div>
		</div>
		
<%-- 			
		<td class="tools" colspan="3">
			<button type="button" id="submit-btn" data-returnPage="${returnPage }"
				data-bnum="${board.postNum}">수정</button>
			<button type="button" id="cancel-btn" data-returnPage="${returnPage }"
				data-bnum="${board.postNum}">취소</button>
			<button type="button" data-returnPage="${returnPage }"
				id="list-btn">목록</button>
		</td> --%>
		<div class="attachments row mb-2" >
				<div class="col">
					<div class="row mb-2">
						<h4>첨부파일목록</h3>
					</div>				
					<div class="row mb-1">
						<table class="table table-sm">
						  <thead>
						    <tr>
						    	<th scope="col">파일명</th>
						      	<th scope="col">크기</th>
						      	<th scope="col">타입</th>
								<th scope="col">삭제여부</th>
						   </tr>
						  </thead>
						    <tbody id="file-list">						  
							<c:if test="${!empty files}">
								<c:forEach var="file" items="${files}">
									<tr class="file-item" data-fid="${file.attachmentNum}">
								
									<td>${file.name }</td><td>${file.fileSize }</td><td>${file.mimetype }</td><td><button class="btn btn-outline-dark delete-file-btn" data-fid="${file.attachmentNum}" type="button">삭제</button></td>
									</tr> 
								</c:forEach>
							</c:if>
						</tbody>
						
						</table>
						
					</div>
				</div>
			        
			</div> 
			<%-- <td colspan="8">
				<ul id="file-list">
					<c:if test="${!empty files}">
						<c:forEach var="file" items="${files}">
							<li class="file-item" data-fid="${file.attachmentNum}">
								${file.name } || ${file.fileSize } || ${file.mimetype }
								<button class="modify-mode delete-file-btn" data-fid="${file.attachmentNum}" type="button">삭제</button>
								</li>
						</c:forEach>
					</c:if>
				</ul> --%>
	
	</form:form>
</div>
</main>
<%@ include file="/WEB-INF/views/include/footer.jsp"  %>	

</body>
</html>
