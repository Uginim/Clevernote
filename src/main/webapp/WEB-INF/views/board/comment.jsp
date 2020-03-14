<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<section id="comment" data-postnum="${board.postNum}" data-usernum="${sessionUser.userNum}">
    <!-- 댓글 작성 폼 -->
    <hr>
    <h5 class="total-count">댓글 <span class="item-count">0</span> 개 </h5>
    <section class="comment-frm">        
        <form>
            <textarea name="content" id="" cols="30" rows="10" placeholder="댓글 추가하기"></textarea>
            <button class="cancel" type="button">취소</button>
            <button class="submit" type="button">등록</button>
        </form>        
    </section>
    <hr>
    <!-- 댓글 리스트  -->
    <section class="list">
        <ul class="root-list">
            댓글이 존재하지 않습니다.                       
        </ul>
        <article class="rest-comments">
            <h6>남은댓글 <span class="count"><small class="content"></small></span>개 </h6>
            <button class="refresh" type="button" data-lastcommenttime="${board.createdAt.getTime()}">불러오기</button>
        </article>
    </section>
</section>

