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


        
        var listBtns = document.getElementsByClassName('listBtn');
        var deleteBtn = document.getElementById('deleteBtn');
        var replyBtn = document.getElementById('replyBtn');
        var deleteFileBtns = document.getElementsByClassName('delete-file-btn');
        
        var saveBtn = document.getElementById("saveBtn");
        var readModeElements = document.getElementsByClassName("");
        
        
        
        deleteBtn.addEventListener("click",function(e){
        	e.preventDefault();
        	if(confirm("삭제하시겠습니까?")){
 						console.log("삭제"+e.target.getAttribute('data-bnum'));
 						var returnPage = e.target.getAttribute('data-returnPage');
 						var bnum = e.target.getAttribute('data-bnum');
 						location.href = getContextPath()+"/board/delete/"+returnPage+"/"+bnum;          }	
        });
        replyBtn.addEventListener("click",function(e){
        	e.preventDefault();	
					console.log("답글달기"+e.target.getAttribute('data-bnum')); 
					var returnPage = e.target.getAttribute('data-returnPage');

					var bnum = e.target.getAttribute('data-bnum');
// 					location.href = getContextPath()+"/board/replyForm/"+returnPage+"/"+bnum;
					location.href = "replyForm/"+returnPage+"/"+bnum;
        });
        deleteBtn.addEventListener("click",function(e){
            
        });
       
        // 삭제
        console.log("deleteFileBtns",deleteFileBtns);
        Array.prototype.forEach.call(deleteFileBtns, element => {
        		element.addEventListener("click",function(e){
            		var fid = e.target.getAttribute('data-fid');
            		var xhr = new XMLHttpRequest();
            		xhr.addEventListener("load",deleteFileItem);
            		xhr.open("DELETE",getContextPath()+"/board/file/"+fid,true);
            		xhr.send(); 
            		
            });    
				}); 
     		// 목록 버튼 클릭시		
     		console.log("목록 버튼 이벤트리스너 추가");
        Array.prototype.forEach.call(listBtns, element => {
            console.log("목록 버튼 이벤트리스너 추가"+element);
        		element.addEventListener("click",function(e){            		
        			e.preventDefault();
        			console.log("목록");
        				var returnPage = e.target.getAttribute('data-returnPage');
        			location.href="/board/list/"+returnPage;
            });    
				}); 
				
    }
    function deleteFileItem(e){
    		//var deleteFileBtns = document.getElementsByClassName('file-item');
    		var list = document.querySelector('#file-list');
    		console.log(e.target.responseText,e.target.readyState, e.target.status);    		
    		if (e.target.readyState===4 && e.target.status == 200){
	    		var fid = e.target.responseText.split(':')[1];
	    		var deleteTarget = document.querySelector('.file-item[data-fid="'+fid+'"]');
	    		list.removeChild(deleteTarget);
	        
       	}
    }

</script>
</head>
<body>
<h3>게시글 보기</h3>
<form:form modelAttribute="board" action='${pageContext.request.contextPath}/board/modify/${returnPage }'
		method="POST" id="modify-form" enctype="multipart/form-data">
		<form:input type="hidden" path="boardNum"/>
		<form:input type="hidden" path="username"/>
		<form:input type="hidden" path="userNum"/>
		<form:textarea style="display:none;" path="content"></form:textarea>
    <table>
        <tr class="category">
            <th><label>분류</label></th>
            <td colspan="6" class="">${board.type.name}</td>
            <th class=""><label>조회수</label></th>
            <td  class="">${board.hit}</td>
        
        </tr> 
        <tr class="title">
            <th><label>제목</label></th>
            <td class=""  colspan="8">${board.title}</td>
            
        </tr>
        <tr class="author">
            <th><label>작성자</label></th>
            <td class="normal-mode"  colspan="8"><b>${board.username}</b>(${board.userNum})</td>
            

        </tr> 
        <tr class="content">
            <th><label>내용</label></th>
            <td class=""  colspan="8">${board.content}</td>
            
            
             
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
                <button type="button" id="replyBtn" data-returnPage="${returnPage }" data-bnum="${board.boardNum}">답글</button>                
                <button type="button" id="deleteBtn" data-returnPage="${returnPage }" data-bnum="${board.boardNum}">삭제</button>
                <button type="button" data-returnPage="${returnPage }" class="listBtn" >목록</button>
            </td>
        </tr>
        
        <tr class="attachments">
            <th><label>첨부목록</label></th>
            <td  colspan="8">
            		<ul id="file-list">
            		<c:if test="${!empty files}">
	                <c:forEach var="file" items="${files}">
	                		<li class="file-item" data-fid="${file.fid}">
	                    ${file.fname } || ${file.fsize } || ${file.ftype } 
	                    </li>
	                </c:forEach>
								</c:if>
								</ul>
            </td>
            
        </tr> 
    </table>
</form:form>

</body>
</html>
