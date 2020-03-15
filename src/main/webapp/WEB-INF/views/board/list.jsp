<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmf" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<title>사용자 게시판</title>
    <script src="<c:url value='/resources/js/common.js'/>"> </script>
	<%@include file="/WEB-INF/views/include/meta.jsp"%>
<script>

window.addEventListener("load", init,false);

	function init(){
		var writeBtn = document.getElementById('writeBtn');
		var searchBtn = document.getElementById('searchBtn');
		var keyword = document.getElementById('keyword');
		var searchType = document.getElementById('searchType');
 
		// 글쓰기 버튼 클릭시
		writeBtn.addEventListener("click",function(e){
			var returnPage = e.target.getAttribute('data-returnPage');
			e.preventDefault();
			location.href="/board/write/"+returnPage;
		},false);

		// 검색 버튼 클릭시
		searchBtn.addEventListener("click",function(e){
				e.preventDefault();
				// 검색어 입력값이 없으면
				if(keyword.value.trim() ===0){
					alert('검색어를 입력하시오');
					keyword.value="";
					keyword.focus();
					return false;
				}	

				var stype = searchType.value; // 검색 유형
				var kword = keyword.value; // 검색어

				location.href ="/board/list/1/" + stype +"/" + kword;
			},false);
	}
</script>

<%@include file="/WEB-INF/views/include/header.jsp"%>
<c:set var="contextRoot" value='${pageContext.request.contextPath}'/>
</head>
<body>
<main role="main" class="py-4">
<div class="container">	
		
		<div class="orw">
			<h2 class="title">게시판</h2>
		</div>
		<div class="row">
			<table id="board" class="table table-bordered table-hover">
				<tr>
					<th scope="col">번호</th>
					<th scope="col">분류</th>
					<th scope="col">제목</th>
					<th scope="col">작성자</th>
					<th scope="col">작성일</th>
					<th scope="col">조회수</th>
				</tr>
				<c:forEach var="row" items="${list}">
					<fmf:formatDate value="${row.createdAt}" pattern="yyyy-MM-dd" var="createdAt"/>
					<tr>
						<td>${row.postNum}</td>
						<td>${row.type.name}</td>
						<td>
						<c:forEach begin="1" end="${row.indent}">
						&nbsp;&nbsp;
						</c:forEach>
						<c:if test="${row.indent > 0 }">
							<img alt="attachment(첨부파일)" src="${pageContext.request.contextPath}/resources/img/post_reply.svg">
						</c:if>
						<a class="text-body" href="${pageContext.request.contextPath}/board/view/${pageManager.rc.currentPage}/${row.postNum }">
							${row.title}
							<c:if test="${row.hasPicture }"><img alt="attachment(첨부파일)" src="${pageContext.request.contextPath}/resources/img/attachment.svg"></c:if> 
						</a>
						</td>
						<td><b>${row.username}</b><!-- (${row.userNum}) --></td>
	<%-- 						<td>${row.bcdate}</td> --%>
						<td>${createdAt}</td>
						<td>${row.hit}</td>
					</tr>
				</c:forEach>
			</table>
		</div>
		<div class="row tool  justify-content-end">				
<%-- 				<a href="${pageContext.request.contextPath}/board/writeForm"><button type="button">글쓰기</button></a> --%>
			
			<a href="<c:url value='/board/write'/>"><button class="btn btn-dark" data-returnPage="${pageManager.rc.currentPage}" type="button" id="writeBtn">글쓰기</button></a>
		</div>
		<div id="paging" class="row control justify-content-center mb-5">
			<!-- 처음페이지 / 이전페이지 이동 -->
			<div class="btn-group mr-sm-2">
				<c:if test="${pageManager.prev}">
					<a class="btn btn-secondary" href="${contextRoot }/board/list/1/${pageManager.src.searchType}/${pageManager.src.keyword}">처음 페이지</a>
					<a class="btn btn-secondary" href="${contextRoot }/board/list/${pageManager.startPageNum-1}/${pageManager.src.searchType}/${pageManager.src.keyword}">이전 페이지</a>
				</c:if>
			</div>
			<div class="btn-group mr-sm-2">
				
				<c:forEach var="pageNum" begin="${pageManager.startPageNum }"
					end="${pageManager.endPageNum }">
					<!-- 현재페이지와 요청페이지가 다르면 -->
					<c:if test="${pageManager.rc.currentPage != pageNum }">
						<a class="btn btn-secondary" href="${contextRoot }/board/list/${pageNum }/${pageManager.src.searchType}/${pageManager.src.keyword}" class="off">${pageNum }</a>
					</c:if>
					<!-- 현재페이지와 요청페이지가 같으면 -->
					<c:if test="${pageManager.rc.currentPage == pageNum }">
						<a class="btn btn-secondary" href="${contextRoot }/board/list/${pageNum }/${pageManager.src.searchType}/${pageManager.src.keyword}" class="on">${pageNum }</a>
					</c:if>
				</c:forEach>
			</div>
			<!-- 다음페이지 / 최종페이지 이동 -->
			<div class="btn-group mr-sm-2">
				<c:if test="${pageManager.next}">
					<a class="btn btn-secondary" href="${contextRoot }/board/list/${pageManager.endPageNum+1}/${pageManager.src.searchType}/${pageManager.src.keyword}">다음 페이지</a>
					<a class="btn btn-secondary" href="${contextRoot }/board/list/${pageManager.finalEndPage}/${pageManager.src.searchType}/${pageManager.src.keyword}">최종 페이지</a>
				</c:if>
			</div>
		</div>
		<div class="row search-input control justify-content-center">
			<form class="form-inline mb-3" >
				<div class="input-group">
					<div class="input-group-prepend">
					 <label class="input-group-text" >검색 타입</label>
					</div>
					<select class="custom-select mr-sm-2"  name="searchType" id="searchType">
						<option value="TC" 
							<c:out value="${pageManager.src.searchType == 'TC' ? 'selected':'' }" />>제목+내용</option>
						<option value="T"
							<c:out value="${pageManager.src.searchType == 'T' ? 'selected':'' }" />>제목</option>
						<option value="C"
							<c:out value="${pageManager.src.searchType == 'C' ? 'selected':'' }" />>내용</option>
						<option value="N"
							<c:out value="${pageManager.src.searchType == 'N' ? 'selected':'' }" />>작성자</option>
						<option value="I"
							<c:out value="${pageManager.src.searchType == 'I' ? 'selected':'' }" />>아이디</option>
					</select>
				</div>
				<input type="search" class="form-control mr-sm-2" name="keyword" id="keyword" value="${pageManager.src.keyword }"/>
				<button id="searchBtn" class="btn btn-outline-success my-2 my-sm-0">검색</button>
			</form>			
		</div>
	</div>
</main>
<%@ include file="/WEB-INF/views/include/footer.jsp"  %>
</body>
</html>
