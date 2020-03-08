package com.uginim.clevernote.user.service;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.uginim.clevernote.common.PasswordDigest;
import com.uginim.clevernote.user.dao.UserDAO;
import com.uginim.clevernote.user.vo.UserVO;

@Service
public class LoginManager implements LoginService {

	@Inject
	UserDAO userDAO;
	// 사용자 로그인
	/**
	 * 로그인 (sign in) 
	 * @param email 접속할 계정의 이메일
	 * @param pw 비밀번호
	 * @return 성공 시 1
	 */
	@Override
	public int signIn(String email, String pw, HttpSession session) {
		String pwHash = PasswordDigest.getSha512(pw);
		UserVO userInfo = userDAO.selectOneUser(email, pwHash);
		if(userInfo !=null) {			
			session.setAttribute(KEY_LOGGED_IN_USERINFO , userInfo);
			return 1;
		}else {			
			return 0;
		}
	}

	// 사용자 로그아웃
	/**
	 * 로그아웃
	 * @param session 세션 
	 * @return 성공 시 1
	 */
	@Override
	public int signOut(HttpSession session) {
//		session.getAttribute(KEY_LOGGED_IN_USERINFO);
		session.removeAttribute(KEY_LOGGED_IN_USERINFO);
		session.invalidate(); 
		return 1;
	}

	// 로그인 여부 확인
	/**
	 * 로그인 여부를 확인함
	 * @param session 세션 확인
	 * @return
	 */
	public boolean isLoggedIn(HttpSession session) {		
		return session.getAttribute(KEY_LOGGED_IN_USERINFO) !=null; 
	}
}
