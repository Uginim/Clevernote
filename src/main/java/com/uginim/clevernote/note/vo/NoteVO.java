package com.uginim.clevernote.note.vo;

import java.util.Date;

import lombok.Data;

@Data
public class NoteVO {
	Date createdTime;
	Date updatedTime;
	String content; 
	String title;
}
