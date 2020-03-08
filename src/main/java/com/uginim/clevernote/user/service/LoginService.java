package com.uginim.clevernote.user.service;

import javax.servlet.http.HttpSession;

public interface LoginService {
//	static final String KEY_LOGGED_IN_USERINFO = "sessionUser";
	static final String KEY_LOGGED_IN_USERINFO= "sessionUser";
	static final int LOGIN_SUCCESS = 1;
	static final int LOGIN_FAILED= 1;
	
	// 사용자 로그인
	/**
	 * 로그인 (sign in) 
	 * @param email 접속할 계정의 이메일
	 * @param pw 비밀번호
	 * @return 성공 시 1
	 */
	int signIn(String email, String pw, HttpSession session);
	
	// 사용자 로그아웃
//	/**
//	 * 로그아웃
//	 * @param userNum 로그아웃 할 user의 번호 
//	 * @return 성공 시 1
//	 */	
	/**
	 * 로그아웃
	 * @param session 세션 
	 * @return 성공 시 1
	 */
	int signOut(HttpSession session);
	
	// 로그인 여부 확인
	/**
	 * 로그인 여부를 확인함
	 * @param session 세션 확인
	 * @return
	 */
	boolean isLoggedIn(HttpSession session);
}
