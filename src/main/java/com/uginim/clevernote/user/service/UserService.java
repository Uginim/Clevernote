package com.uginim.clevernote.user.service;

import com.uginim.clevernote.user.vo.UserVO;

public interface UserService {
	// 회원 가입
	public void signIn(UserVO userVO);
	// 회원 탈퇴
	public void signOut(String email, String pw);
	// 회원 정보 수정
	public void modify(UserVO userVO);
	// 회원 별명 검색
	
}
