<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>

<head>
    <meta charset="UTF-8">
    <title>새 게시글 쓰기</title>
    <style>
    	/* #write-form{*/
    	#board{
    		width:30em
    	}
    	.row {
    		min-height:1.5em;
    		margin-bottom:0.2em;
    		display:flex;
    	}
    	.row>span:first-child{
    		display:flex;
    		/*width:5em;*/
    		width:20%;
    	}
    	.row>span:last-child{
    		display:flex;
    		/*width:5em;*/
    		width:80%;
    	}
    	.row>span:last-child>input,
    	.row>span:last-child>select,
    	.row>span:last-child>textarea{
    		width:100%;
    	}
    </style>
    <script src="<c:url value="/resources/js/common.js"/>"> </script>
    <script>
    window.addEventListener("load",init,false);
    function init() {
        
        var writeBtn = document.getElementById("submit-btn");
        var cancelBtn = document.getElementById("cancel-btn");
        var listBtn = document.getElementById("list-btn");
        var boardForm= document.getElementById("board");
        // 등록 버튼 클릭시
        writeBtn.addEventListener("click", function(e) {
            e.preventDefault();

            boardForm.submit();
            
				},false);		
				// 취소 버튼 클릭시
				cancelBtn.addEventListener("click", function(e) {
						e.preventDefault();
				},false);		
				// 목록 버튼 클릭시
				
				listBtn.addEventListener("click",function(e){
					e.preventDefault();
					console.log("목록");
	 				var returnPage = e.target.getAttribute('data-returnPage');
					location.href=getContextPath()+"/board/list/"+returnPage;
				},false);   
    }
    </script>
</head>

<body>
    ${sessionUser}
    <h4>새 글 쓰기</h4>
<%--         <form:form  modelAttribute="board" id="write-form" --%>
        <form:form  modelAttribute="board" 
        action="${pageContext.request.contextPath}/board/write/${returnPage }" method="post" enctype="multipart/form-data">
            
                <div class="row">
                    <span><label>분류</label></span><span>
                        <form:select path="type.typeNum" id="type" cols="70">
                            <option value="0">== 선 택 ==</option>
                            <form:options path="type.typeNum" items="${boardTypes}" itemValue="typeNum" itemLabel="name" />
                        </form:select>                        
                    </span>                    
                </div>
                <form:errors cssClass="error" path="type.typeNum"/>
                <div class="row">
                    <span><label>제목</label></span><span><form:input path="title" type="text" cols="70"/></span>
                    
                </div>
                <form:errors cssClass="error" path="title"/>
                <div class="row">
                    	<span><label>작성자</label></span><span>
                    	<input value="${sessionUser.username}" readonly cols="70"/>
                    	<form:input path="userNum" type="hidden" value="${sessionUser.userNum}"/>
                    	<form:input path="username" type="hidden" value="${sessionUser.username}"/>
                    	</span>
                    	
                </div>
                <div class="row">
                    <span><label>내용</label></span>
                    <span><form:textarea path="content" id="" cols="70" rows="40"></form:textarea></span>
                </div>
                <form:errors path="content" cssClass="error"/>
                <div class="row">
                    <span><label>첨부</label></span><span><input type="file" name="files" multiple/></span>
                    <form:errors path="files"/>
                </div>
            
            <div>
		            <span>            	
		                <form:button class="button" id="submit-btn">등록</form:button>
		                <form:button class="button" id="cancel-btn">취소</form:button>
		                <form:button class="button" data-returnPage="${returnPage }" id="list-btn">목록</form:button>
                </span>
            </div>
        </form:form>
</body>
</html>
