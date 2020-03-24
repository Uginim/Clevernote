package com.uginim.clevernote.note.vo;

import java.util.Date;

import lombok.Data;
@Data
public class CategoryVO {
	String title; // 제목
	long categoryNum; // 카데고리 번호
	long ownerNum; // 유저 번호
	Date createdAt; // 생성 시간
	Date updatedAt; // 업데이트 시간
	int noteCount; // 노트 개수
}
