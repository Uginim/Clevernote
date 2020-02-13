package com.uginim.clevernote.user.vo;

import java.util.Date;

import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class UserVO {
	@Pattern(regexp = "", message = "")
	String email;
	@Pattern(regexp = "", message = "")
	String username;
	String pwHash;
	
	Date createdTime;
	
	Date updatedTime;
	
	
}
