<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>${boardVO.title }</title>
<style>
    table{
        border:1px solid;
        border-collapse: collapse;
        width:40em;
    }
    table td, table th{
        padding: 0.45em;
        border:1px solid;
    }
    input,select,textarea{
        border: none;
    }
    tr.content{
        height: 20em;
    }
    /* tr.content>td{
        height:auto;
    } */
    tr.content>td,
    tr.content>th{
          /* height:auto; */
          /* height:100%; */
          height: 20em;
    }
    td.tools{
        display: flex;
        padding: 0;        
    }
    tr.content>td>*{
        height:100%;
    }
    .no-border{
        border:none;
    }
    td>*{
        width:100%;
    }
    
    /* .{ */
    
    .attachments>td button{
        display: inline-block;
        width:auto;
    }
    .attachments>td button.invisible-mode{
        display: none;
    }
</style>
    <script src="<c:url value="/resources/js/common.js"/>"> </script>

<script>
    window.addEventListener("load",init,false);
    
    function init(e){


        
        var listBtn = document.getElementById('list-btn');
        var cancelBtn = document.getElementById('cancel-btn');
        var submitBtn = document.getElementById('submit-btn');
		var modifyForm = document.getElementById('modify-form');        

     	// 등록 버튼 클릭시
        submitBtn.addEventListener("click", function(e) {
            e.preventDefault();

            modifyForm.submit();
            
		},false);	
		// 취소 버튼 클릭시        
        cancelBtn.addEventListener("click", function(e) {
				e.preventDefault();
				console.log("취소");
				var returnPage = e.target.getAttribute('data-returnPage');
				var bnum = e.target.getAttribute('data-bnum');
				
				location.href="/board/view/"+returnPage+"/"+bnum ;
		},false);		
        
		// 목록 버튼 클릭시		
		listBtn.addEventListener("click",function(e){
			e.preventDefault();
			console.log("목록");
				var returnPage = e.target.getAttribute('data-returnPage');
			location.href="/board/list/"+returnPage;
		},false);  
				
    }

</script>
</head>
<body>
	<h3>게시글 보기</h3>
	<form:form modelAttribute="board"
		action='${pageContext.request.contextPath}/board/modify/${returnPage }'
		method="POST" id="modify-form" enctype="multipart/form-data">
		<form:input type="hidden" path="postNum" />
		<form:input type="hidden" path="username" />
		<form:input type="hidden" path="userNum" />
		<form:textarea style="display:none;" path="content"></form:textarea>
		<table>
			<tr class="category">
				<td>
				<form:label path="type.typeNum">분류</form:label>
				</td>
				<td class="" colspan="8">
					<form:select path="type.typeNum" id="type" cols="70">
						<option value="0">== 선 택 ==</option>
						<form:options path="type.typeNum" items="${boardTypes}"
							itemValue="typeNum" itemLabel="name" />
					</form:select>
				</td>

			</tr>
			<tr class="title">
				<th><label>제목</label></th>
				<td class="" colspan="8">
<!-- 					<div> -->
						<form:input path="title" type="text" cols="70" value="${board.title}"/>
<!-- 					</div> -->
						<form:errors cssClass="error" path="title"/>

				</td>
				
			</tr>
			<tr class="author">
				<th><label>작성자</label></th>
				<td class="normal-mode" colspan="8"><b>${board.username}</b>(${board.userNum})</td>


			</tr>
			<tr class="content">
				<th><label>내용</label></th>
				<td class="" colspan="8">
					<form:textarea path="content" id="" cols="70" rows="40" />							

					<form:errors path="content" cssClass="error"/>
				</td>
			</tr>
			
			<tr>
				<td>
					<form:label path="files">첨부</form:label>
				</td>
				<td colspan="9">
				<input type="file" name="files" multiple/>
                    <form:errors path="files"/>
				</td>
			</tr>
			<tr class=" tools">
				<td class="no-border"></td>
				<td class="no-border"></td>
				<td class="no-border"></td>
				<td class="no-border"></td>
				<td class="no-border"></td>
				<td class="no-border"></td>
				<td class="no-border"></td>
				<td class="no-border"></td>
				<td class="tools" colspan="3">
					<button type="button" id="submit-btn" data-returnPage="${returnPage }"
						data-bnum="${board.postNum}">수정</button>
					<button type="button" id="cancel-btn" data-returnPage="${returnPage }"
						data-bnum="${board.postNum}">취소</button>
					<button type="button" data-returnPage="${returnPage }"
						id="list-btn">목록</button>
				</td>
			</tr>

			<tr class="attachments">
				<th><label>첨부목록</label></th>
				<td colspan="8">
					<ul id="file-list">
						<c:if test="${!empty files}">
							<c:forEach var="file" items="${files}">
								<li class="file-item" data-fid="${file.attachmentNum}">
									${file.name } || ${file.fileSize } || ${file.mimetype }</li>
							</c:forEach>
						</c:if>
					</ul>
				</td>
			</tr>
		</table>
	</form:form>

</body>
</html>
