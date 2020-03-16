<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<section id="comment" data-postnum="${board.postNum}" data-usernum="${sessionUser.userNum}" data-posttime="${board.createdAt.time}" data-viewtime="${requestTime.time}">
    <!-- 댓글 작성 폼 -->
    <hr>
    <h5 class="total-count">댓글 <span class="item-count">0</span> 개 </h5>
    <section class="comment-frm">        
        <form> 
            
            <div class="row mb-2">
                <textarea class="form-control " name="content" id="" cols="30" rows="5" placeholder="댓글 추가하기"></textarea>
            </div>
            <div class="row justify-content-end">
                <div class="btn-group">
                    <button class="btn btn-light cancel" type="button">취소</button>
                    <button class="btn btn-light submit" type="button">등록</button>
                </div>
            </div>
        </form>        
    </section> 
    <hr> 
    <!-- 댓글 리스트  -->
    <section class="list">
        <ul class="root-list list-unstyled">
            댓글이 존재하지 않습니다.                       
        </ul>
        <article class="rest-comments">
            <h6>남은댓글 <small class="content"><span class="count">0</span></small>개 </h6>
            <button class="refresh btn btn-link" type="button" data-lastcommenttime="${board.createdAt.getTime()}">불러오기</button>
        </article>
    </section>
</section>

