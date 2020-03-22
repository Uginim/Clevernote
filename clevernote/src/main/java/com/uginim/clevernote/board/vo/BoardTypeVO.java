package com.uginim.clevernote.board.vo;

import javax.validation.constraints.Positive;

import lombok.Data;

@Data
public class BoardTypeVO {
	@Positive(message="분류를 선택하세요!")
	private long typeNum; // 타입 번호	
	private String name; // 타입 이름
}
