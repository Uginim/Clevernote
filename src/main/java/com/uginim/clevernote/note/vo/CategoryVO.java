package com.uginim.clevernote.note.vo;

import java.util.Date;

import lombok.Data;
@Data
public class CategoryVO {
	String title;
	long categoryNum;
	long ownerNum;
	Date createdAt; // 생성 시간
	Date updatedAt; // 업데이트 시간
}
