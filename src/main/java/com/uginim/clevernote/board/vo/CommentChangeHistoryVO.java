package com.uginim.clevernote.board.vo;

import java.sql.Date;

public class CommentChangeHistoryVO {
		long postNum; // post별로 조회하기 쉽도록 
		long historyNum; // 변경이력 번호
		long commentNum; // 댓글 번호		
		Date createdAt;	// 생성일시
		String type; // 유형
}
