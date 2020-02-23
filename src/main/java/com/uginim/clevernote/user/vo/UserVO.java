package com.uginim.clevernote.user.vo;

import java.util.Date;

import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class UserVO {
	@Pattern(regexp = "", message = "")
	String email; // e-mail address. 계정을 식별할 수 있음   
	@Pattern(regexp = "", message = "")
	String username; // username. 계정명 외에 앱인터페이스에서 표현되는 이름(중복가능) 
	String pwHash; // 패스워드 다이제스트
	
	long usernum;
	
	Date createdAt; // 생성날짜	
	Date updatedAt; // 수정날짜
	
	
}
