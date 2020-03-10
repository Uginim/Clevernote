package com.uginim.clevernote.board.vo;

import java.util.Date;

import lombok.Data;

@Data
public class VoteVO {
	private long voteNum; 
	private long userNum;
	private long commentNum;
	private char type;
	private Date updatedAt; 
	private Date createdAt; 
}
