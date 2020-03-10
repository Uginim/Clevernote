<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmf" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>
    <script src="<c:url value="/resources/js/common.js"/>"> </script>

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
<style type="text/css">
table#board {
	border-collapse: collapse;
	table-layout: fixed;
	word-break: break-all;
	height: auto;
	border: 1px solid;
	table-layout: fixed;
	word-break: break-all;
	height: auto;
}

.row {
	display: flex;
	justify-content: center;
}

.board-table {
	
}
.tool{
	display: flex;
	flex-dirowtion :row-reverse;
	justify-content: flex-start;
}
.control {
	display: flex;	
	justify-content: center;
}
.control a {
	margin-left: 0.25em;
	margin-right: 0.25em;
}
table th,table td{
	padding:0.5em;
	border: 1px solid;
}
th {
	background-color: #bbbbbb;	
}
td {
}

h3.title {
	text-align: center;
}
</style>
<c:set var="contextRoot" value='${pageContext.request.contextPath}'/>
</head>
<body>

	<h2 class="title">게시글 목록</h3>
	<div class="row">
		<div class="board-table">
			<table id="board">
				<tr>
					<th>번호</th>
					<th>분류</th>
					<th>제목</th>
					<th>작성자</th>
					<th>작성일</th>
					<th>조회수</th>
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
						ㄴ
						</c:if>
						<a href="${pageContext.request.contextPath}/board/view/${pageManager.rc.currentPage}/${row.postNum }">${row.title}${row.hasPicture }</a>
						</td>
						<td><b>${row.username}</b><!-- (${row.userNum}) --></td>
<%-- 						<td>${row.bcdate}</td> --%>
						<td>${createdAt}</td>
						<td>${row.hit}</td>
					</tr>
				</c:forEach>
			</table>
			<div class="tool">				
<%-- 				<a href="${pageContext.request.contextPath}/board/writeForm"><button type="button">글쓰기</button></a> --%>
				
				<a href="<c:url value="/board/write" />"><button data-returnPage="${pageManager.rc.currentPage}" type="button" id="writeBtn">글쓰기</button></a>
			</div>
			<div id="paging" class="control">
				<!-- 처음페이지 / 이전페이지 이동 -->
				<c:if test="${pageManager.prev}">
					<a href="${contextRoot }/board/list/1/${pageManager.src.searchType}/${pageManager.src.keyword}">처음 페이지</a>
					<a href="${contextRoot }/board/list/${pageManager.startPageNum-1}/${pageManager.src.searchType}/${pageManager.src.keyword}">이전 페이지</a>
				</c:if>
				<c:forEach var="pageNum" begin="${pageManager.startPageNum }"
					end="${pageManager.endPageNum }">
					<!-- 현재페이지와 요청페이지가 다르면 -->
					<c:if test="${pageManager.rc.currentPage != pageNum }">
						<a href="${contextRoot }/board/list/${pageNum }/${pageManager.src.searchType}/${pageManager.src.keyword}" class="off">${pageNum }</a>
					</c:if>
					<!-- 현재페이지와 요청페이지가 같으면 -->
					<c:if test="${pageManager.rc.currentPage == pageNum }">
						<a href="${contextRoot }/board/list/${pageNum }/${pageManager.src.searchType}/${pageManager.src.keyword}" class="on">${pageNum }</a>
					</c:if>
				</c:forEach>
				<!-- 다음페이지 / 최종페이지 이동 -->
				<c:if test="${pageManager.next}">
					<a href="${contextRoot }/board/list/${pageManager.endPageNum+1}/${pageManager.src.searchType}/${pageManager.src.keyword}">다음 페이지</a>
					<a href="${contextRoot }/board/list/${pageManager.finalEndPage}/${pageManager.src.searchType}/${pageManager.src.keyword}">최종 페이지</a>
				</c:if>
			</div>
			<div class="search-input control">
				<form>
					<select name="searchType" id="searchType">
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
									<input type="search" name="keyword" id="keyword" value="${pageManager.src.keyword }"/>
									<button id="searchBtn">검색</button>
				</form>
				
			</div>
		</div>
	</div>
</body>
</html>
