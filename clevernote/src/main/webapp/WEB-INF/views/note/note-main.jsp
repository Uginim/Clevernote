<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Note</title>
<link
	href="https://cdn.jsdelivr.net/npm/suneditor@latest/dist/css/suneditor.min.css"
	rel="stylesheet">
<!-- <link href="https://cdn.jsdelivr.net/npm/suneditor@latest/assets/css/suneditor.css" rel="stylesheet"> -->
<!-- <link href="https://cdn.jsdelivr.net/npm/suneditor@latest/assets/css/suneditor-contents.css" rel="stylesheet"> -->
<script
	src="https://cdn.jsdelivr.net/npm/suneditor@latest/dist/suneditor.min.js"></script>
<!-- languages (Basic Language: English/en) -->
<script
	src="https://cdn.jsdelivr.net/npm/suneditor@latest/src/lang/ko.js"></script>


<link rel="stylesheet" href="<c:url value="/resources/css/note-main.css"/>" />

<script src="<c:url value="/resources/js/memo.js"/>"> </script>
<script src="<c:url value="/resources/js/note-main.js"/>">

</script>
<script>
window.addEventListener('load',init);
function init(){
	var categorySection = document.querySelector('section#category'); // 카데고리 리스트
	var categoryList = document.querySelector("section#category .list")
	var noteList = document.querySelector('#note ul.list'); // 노트 리스트
	var addNewNoteBtn = document.querySelector('#add-new-note-btn'); // 새 노트 
// 	var noteContent = document.querySelector('#main-content .content-body'); // 노트 컨텐트
	var noteEditor= document.querySelector('#main-content .editor'); // 에디터
	var noteCounterNum = document.querySelector("#note .counter>.number"); // 노트 개수
	var categoryCounterNum = document.querySelector("#category  .counter>.number"); // 노트 개수
	var categoryBtn = document.querySelector('#category-btn');

	var aside = document.querySelector('body>aside');
    var toolbox = document.querySelector('body>aside>#toolbox');        
    var mainContent = document.querySelector('#main-content');
    var mainHeader = document.querySelector('header#main');
    console.log("categorySection:"+categorySection);
    console.log("categoryList:"+categoryList);
    console.log("noteList:"+noteList);
    console.log("addNewNoteBtn:"+addNewNoteBtn);
//     console.log("noteContent:"+noteContent);
    console.log("noteEditor:"+noteEditor);
    console.log("noteCounterNum:"+noteCounterNum);
    console.log("categoryCounterNum:"+categoryCounterNum);
    console.log("categoryBtn:"+categoryBtn);
    console.log("aside:"+aside);
    console.log("toolbox:"+toolbox);
    console.log("mainContent:"+mainContent);
    console.log("mainHeader:"+mainHeader);
    
	
	
	// category 갱신하기
	requestCategories( categories => {
		console.log("카데고리 추가");
		resetCategoryList(categoryList, categories);
		noteCounterNum.innerText = categories.length;
	});

	// 카데고리 버튼 클릭
	categoryBtn.addEventListener("click", (e)=>{
		console.log("categoryBtn was clicked")
	});
	
	// 카데고리 리스트 클릭 시
	categoryList.addEventListener('click',(e)=>{	
		var list = e.composedPath();
		var i = 0;
		console.log("카데고리 리스트 클릭");
		while(i++<list.length){
			// 리스트 태그가 클릭되었을 시 
			if(list[i].tagName==='li' && 
					list[i].classList.contains("category-list-item")){
				console.log(list[i]);
// 				requestJsonDataByAjax('GET',"/notes/"+item.getAttribute('date-num'),(data)=>{					
// 				});		
			}
			break;
		}
				
		
	},true);
	
	// 에디터 추가
	// const suneditor = SUNEDITOR.create(noteEditor,{
	//     // All of the plugins are loaded in the "window.SUNEDITOR" object in dist/suneditor.min.js file
	//     // Insert options
	//     // Language global object (default: en)
	//     lang: SUNEDITOR_LANG['ko'],
	//     placeholder     : "내용을 채워주세요" 
	// });

	// 1. 카데고리 목록 가져오기
	// 2. 선택된 카데고리에서 노트목록 가져오기
	// 3. 노트가 없을 시 노트가 없음을 알림(새노트 추가여부 물어보기)
	// 
// 	suneditor.setContents(noteContent.innerHTML);
// 	noteContent.style.display="none";
}


</script>
</head>
<body>
	<header id="main">
        <!-- 로고 -->

        <div class="name">
            <img src="<c:url value="/resources/img/temp_.svg"/>" class="logo" alt="logo">
            <h1>CleverNote</h1>
        </div>
        <!--  -->
        <div class="hamburger">
            <button><img id="hamburger" src="<c:url value="/resources/img/hamburger.svg"/>" alt=""></button>
        </div>
    </header>
    <!-- <aside class="active"> -->
    <aside>
        <!-- <nav id="toolbox" class="active"> -->
        <nav id="toolbox">
            <div class="hidden-header">                
                <button id="sidebar-closebtn" class="closebtn"><img src="<c:url value="/resources/img/close.svg"/>" alt="close"></button>
            </div>
            <div class="header">
                <h1>Toolbox</h1>
                <hr>
            </div>
            <div class="button-list">
                <!-- 새 매모 -->
                <button id="listbtn">
                    <img src="<c:url value="/resources/img/list.svg"/>" alt="new memo">
                    <h4>노트 리스트</h4>
                </button>
                <button>
                    <img src="<c:url value="/resources/img/plus.svg"/>" alt="new memo">
                    <h4>새 메모</h4>
                </button>
                <!-- 검색 -->
                <button>
                    <img src="<c:url value="/resources/img/search.svg"/>" alt="search">
                    <h4>검색</h4>
                </button>
                <!-- 공유 -->
                <button>
                    <img src="<c:url value="/resources/img//share.svg"/>" alt="share">
                    <h4>공유</h4>
                </button>
                <!-- 바로가기 -->
                <button>
                    <img src="<c:url value="/resources/img/bookmark.svg"/>" alt="bookmark">
                    <h4>북마크</h4>
                </button>
                <!-- 카데고리 -->
                <button id="category-btn">
                    <img src="<c:url value="/resources/img/category.svg"/>" alt="category">
                    <h4>카데고리</h4>
                </button>
                <!-- 태그 -->
                <button>
                    <img src="<c:url value="/resources/img/tag1.svg"/>" alt="tag">
                    <h4>태그</h4>
                </button>
                <!-- 도움말 -->
                <button>
                    <img src="<c:url value="/resources/img/help.svg"/>" alt="help">
                    <h4>도움말</h4>
                </button>
                <!-- 설정 -->
                <button>
                    <img src="<c:url value="/resources/img/setting.svg"/>" alt="setting">
                    <h4>설정</h4>
                </button>
                <!-- 설정 -->
                <button id="logoutbtn">
                    <img src="<c:url value="/resources/img/logout.svg"/>" alt="setting">
                    <h4>로그아웃</h4>
                </button>
            </div>
        </nav>
        <section id="category">			
			<header>
				<h3 class="title">${sessionScope['sessionUser'].username} 님의 카데고리</h3>
				<h6 class="counter">
					<span class="number">14</span><span class="unit">개의 카데고리</span>
				</h6>
				<div class="toolbox">
					<button id="add-new-note-btn">
						<img src="<c:url value="/resources/img/plus.svg"/>" alt="">
					</button>
					<button>
						<img src="<c:url value="/resources/img/share.svg"/>" alt="">
					</button>
					<button class="closebtn">
						<img src="<c:url value="/resources/img/close.svg"/>" alt="">
					</button>
				</div>
			</header>			
			<ul class="list">
                <li class="category-list-item">
                    <h4>프로그래밍 관련 기사 스크랩</h4>
                    <p class="date">2019-12-22</p>
                    <div class="toolbox">
                        <button><img src="<c:url value="/resources/img/open.svg"/>" alt="open"></button>
                        <button><img src="<c:url value="/resources/img/share.svg"/>" alt=""></button>
                        <button><img src="<c:url value="/resources/img/trashcan.svg"/>" alt=""></button>
                    </div>
                </li>
              
            </ul>
		</section>
        <section id="note">
        <!-- <section id="note-list" class="active"> -->
            <header class="note-category-name">
                <h3 class="category-title">프로그래밍</h3>
                <h6 class="counter"><span class="number">14</span><span class="unit">개의 노트</span></h6>
                <div class="toolbox">
                    <button id="add-new-note-btn"><img src="<c:url value="/resources/img/plus.svg"/>" alt=""></button>
                    <button><img src="<c:url value="/resources/img/share.svg"/>" alt=""></button>
                    <button class="closebtn"><img src="<c:url value="/resources/img/close.svg"/>" alt=""></button>
                </div>
            </header>
            <hr>
            <ul class="list">
                <li class="note-list-item">
                    <h4>프로그래밍 관련 기사 스크랩</h4>
                    <p class="date">2019-12-22</p>
                    <p class="content-summary">summary</p>
                    <div class="toolbox">
                        <button><img src="<c:url value="/resources/img/open.svg"/>" alt="open"></button>
                        <button><img src="<c:url value="/resources/img/share.svg"/>" alt=""></button>
                        <button><img src="<c:url value="/resources/img/trashcan.svg"/>" alt=""></button>
                    </div>
                </li>
              
            </ul>
        </section>
    </aside>
    <section id="main-content">

        <header>
            <div class="note-control">
                <div class="bundle">
                    <button>북마크 추가</button>
                </div>
                <div class="bundle">
                    <button>삭제</button>
                    <button class="edit">수정</button>
                    <button class="">공유</button>
                </div>
            </div>
            <!-- <hr> -->
            <div class="tag-container">
                <span class="placeholder">머신러닝</span>
                <span class="placeholder">태그 추가..</span>
            </div>
            <hr>
            <div class="editor-control">
                
            </div>
        </header>
        <div class="content-block">
            <h1 class="title">머신러닝 자료</h1>
            <textarea class="editor"></textarea>
        </div>
    </section>
	<!-- 카데고리 리스트 -->
<!-- 	<ul id="category-list"> -->
<!-- 	</ul> -->
	
 	<!-- 노트 리스트 --> 
<!-- 	<section id="note-list"> -->
<!-- 		<header class="category"> -->
<!-- 			<h1 class="title"></h1> -->
<!-- 			<div class="tools"> -->
<!-- 				<button type="button">add</button> -->
<!-- 				<button type="button">share</button> -->
<!-- 			</div> -->
<!-- 			<p class="note-counter"></p> -->
<!-- 		</header> -->
<!-- 		<ul id="note-list"> -->
<!-- 			<li></li> -->
<!-- 		</ul> -->
<!-- 	</section> -->
	<!-- 노트 메인 -->
<!-- 	<section id="curr-note"> -->
<!-- 		<header> -->
<!-- 			<h1 class="title"></h1> -->
<!-- 			<nav class="tools"></nav> -->
<!-- 		</header> -->
<!-- 		<article class="content"> -->
<!-- 			<textarea id="sample"> -->
		
<!-- 		</textarea> -->

<!-- 		</article> -->
<!-- 	</section> -->
	
	
</body>
</html>