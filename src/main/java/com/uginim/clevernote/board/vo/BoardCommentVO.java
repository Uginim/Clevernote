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
	private long targetNum; // 대상댓글 번호
	private long parentNum; // 부모 번호
	private long childrenCount; // 자식 댓글 개수
	private long like; // 좋음
	private long dislike; // 싫음
	
}
	