package com.uginim.clevernote.board.vo;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class BoardPostVO {
	private long postNum;  // 게시글 번호
	
	@NotNull
	@Size(min=3, max=200, message="제목은 3자 이상 200자 이하")
	private String title; // 게시글 제목
	@Valid
	private BoardTypeVO type;
	private long userNum; // 사용자 번호
	private String username; // 사용자 이름
	@NotNull(message="내용을 입력바랍니다")
	@Size(min=4, message="내용은 최소 4자 이상 입력 바랍니다!")
	private String content; // 게시글 내용
	private Date createdAt; // 생성일자
	private Date updatedAt; // 수정일자
	private long hit; // 조회수
	private long postGroup; // 게시글 그룹
	private int step; // 게시글 단계
	private int indent; // 게시글 깊이
//	private char hasPicture;
	private boolean hasPicture;
	public void setHasPicture(boolean hasPicture) {
		this.hasPicture=hasPicture;
	}
	public void setHasPicture(char c) {
		if(c == 't' || c =='T')
			this.hasPicture = true; 
		else if(c == 'f' || c =='F')
			this.hasPicture = false;
	}
	// 첨부파일
	private List<MultipartFile> files;
}
