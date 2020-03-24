package com.uginim.clevernote.board.vo;

import java.util.Date;

import lombok.Data;

@Data
public class AttachmentFileVO {
	private long attachmentNum; // 첨부파일 번호
	private long postNum; // 게시글 번호
	private String name;  // 파일명
	private long fileSize; // 파일 사이즈
	private String mimetype; // 첨부파일 타입
	private byte[] data; // 파일 데이터
	private Date createdAt; // 생성 일시
	private Date updatedAt; // 수정 일시
}
