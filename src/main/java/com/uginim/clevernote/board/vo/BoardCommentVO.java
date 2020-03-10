package com.uginim.clevernote.board.vo;

import java.util.Date;

import lombok.Data;

@Data
public class BoardCommentVO {
	private long commentNum; // 댓글 번호
	private long postNum; // 개시글 번호
	private long userNum; // 작성자 번호
	private String username; // 작성자 이름
	private Date createdAt; // 생성일시
	private Date updatedAt; // 수정 일시
	private String content; // 내용
	private long commentGroup; // 댓글 그룹 
	private int step; // 댓글 단계
	private int indent; // 댓글 
	private long parentNum; // 부모 번호
	private long like; // 좋음
	private long dislike; // 싫음
	
}
