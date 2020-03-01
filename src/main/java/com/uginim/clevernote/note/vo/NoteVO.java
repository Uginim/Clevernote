package com.uginim.clevernote.note.vo;

import java.util.Date;

import lombok.Data;

@Data
public class NoteVO {
	
	
	Date createdAt; // 노트 생성 시간
	Date updatedAt; // 업데이트 시간
	String content;  // 노트 내용
	String title; // 노트 제목
	long noteNum; // 노트 번호
//	long categorynum; // 카데고리 번호
	CategoryVO category;
}
