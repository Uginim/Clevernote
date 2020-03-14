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
    <script src="<c:url value='/resources/js/common.js'/>"> </script>
	<link rel="stylesheet" href="${pageContext.request.contextPath }/webjars/bootstrap/4.4.1-1/css/bootstrap.min.css" />
	<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/comment.css" />
	<script type="text/javascript" src="${pageContext.request.contextPath }/webjars/jquery/3.4.1/jquery.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/webjars/popper.js/2.0.2/umd/popper.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/webjars/bootstrap/4.4.1-1/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/resources/js/comment.js" ></script>
<script>
    window.addEventListener("load",init,false);
    
    function init(e){        
        var listBtns = document.getElementsByClassName('listBtn');
        var deleteBtn = document.getElementById('deleteBtn');
        var replyBtn = document.getElementById('replyBtn');
        var modifyBtn = document.getElementById('modifyBtn');
        var deleteFileBtns = document.getElementsByClassName('delete-file-btn');        
        var saveBtn = document.getElementById("saveBtn");
        var readModeElements = document.getElementsByClassName("");        
        if(deleteBtn){
	        deleteBtn.addEventListener("click",function(e){
	        	e.preventDefault();
	        	if(confirm("삭제하시겠습니까?")){
	 						console.log("삭제"+e.target.getAttribute('data-bnum'));
	 						var returnPage = e.target.getAttribute('data-returnPage');
	 						var bnum = e.target.getAttribute('data-bnum');
	 						location.href = "/board/delete/"+returnPage+"/"+bnum;          }	
	        });
        }
        if(replyBtn){
	        replyBtn.addEventListener("click",function(e){
	        	e.preventDefault();	
						console.log("답글달기"+e.target.getAttribute('data-bnum')); 
						var returnPage = e.target.getAttribute('data-returnPage');
	
						var bnum = e.target.getAttribute('data-bnum');
	// 					location.href = getContextPath()+"/board/replyForm/"+returnPage+"/"+bnum;
						location.href = "/board/reply/page/"+returnPage+"/"+bnum;
	        });            
		}
		if(modifyBtn){
	        modifyBtn.addEventListener("click",function(e){
	        	if(confirm("수정하시겠습니까?")){
					console.log("수정"+e.target.getAttribute('data-bnum'));
					var returnPage = e.target.getAttribute('data-returnPage');
					var bnum = e.target.getAttribute('data-bnum');
					location.href = "/board/modify/"+bnum+"/"+returnPage;          
				}	
		    });
		}
       
        // 삭제
        console.log("deleteFileBtns",deleteFileBtns);
        Array.prototype.forEach.call(deleteFileBtns, element => {
        		element.addEventListener("click",function(e){
            		var fid = e.target.getAttribute('data-fid');
            		var xhr = new XMLHttpRequest();
            		xhr.addEventListener("load",deleteFileItem);
            		xhr.open("DELETE","/board/file/"+fid,true);
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
<h3>${board.title}</h3>    
    <div class="category">
        <span><label>분류</label></span>
        <span>${board.type.name}</span>
        <span><label>조회수</label></span>
        <span>${board.hit}</span>    
    </div>
    <div class="author">
        <span><label>작성자</label></span>
        <td class="normal-mode"  colspan="8"><b>${board.username}</b>(${board.userNum})</span>
        

    </div> 
    <div class="content">
        <span><label>내용</label></span>
        <td class=""  colspan="8">${board.content}</span>
    </div> 
    
    <div class=" tools">
        <span class="tools">
            <button type="button" id="replyBtn" data-returnPage="${returnPage }" data-bnum="${board.postNum}">답글</button> 
            <c:if test="${sessionUser.userNum == board.userNum}">               
                <button type="button" id="deleteBtn" data-returnPage="${returnPage }" data-bnum="${board.postNum}">삭제</button>
                <button type="button" id="modifyBtn" data-returnPage="${returnPage }" data-bnum="${board.postNum}">수정</button>
            </c:if>
            <button type="button" data-returnPage="${returnPage }" class="listBtn" >목록</button>
        </span>
    </div>
    
    <div class="attachments">
        <span><label>첨부목록</label></span>
        <span>
       		<ul id="file-list">
        		<c:if test="${!empty files}">
	                <c:forEach var="file" items="${files}">
	                		<li class="file-item" data-fid="${file.attachmentNum}">
	                    ${file.name } || ${file.fileSize } || ${file.mimetype } 
	                    </li>
	                </c:forEach>
				</c:if>
			</ul>
        </span>        
    </div> 
	<%@ include file="comment.jsp"  %>
	
    
</body>
</html>
