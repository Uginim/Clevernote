<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Note Editor</title>
<script src="<c:url value="/resources/js/note/note.js"/>"> </script>
</head>
<body>
	<div id="root"></div>
	<nav id="global-tools">
		
	</nav>
	<aside id="list">
		<section id="category-list">
			
		</section>
		<section >
			<ul id="note-list">			
			</ul>
		</section>
	</aside>	
	<main id="note-main">
		<header id="note-header" class="note" >
		</header>
		<section id="note-tools" class="note" >
		</section>
		<section id="note-contents" class="note" >		
		</section>
		<footer id="note-footer" class="note" >
		</footer>	
	</main>
</body>
</html>