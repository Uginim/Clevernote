package com.uginim.clevernote.user.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.uginim.clevernote.common.PasswordDigest;
import com.uginim.clevernote.user.dao.UserDAO;
import com.uginim.clevernote.user.vo.UserVO;

@Service
public class UserManager implements UserService {

	@Inject
	UserDAO userDAO;
	
	// 회원 가입
	/**
	 * 회원가입
	 * @param userVO 새로운 userVO객체
	 * @return 성공 시 1 
	 */
	@Override
	public int signUp(UserVO userVO) {
		String pwHash = PasswordDigest.getSha512(userVO.getPw());
		userVO.setPwHash(pwHash);
		return userDAO.insertNewUser(userVO);
	}

	// 회원 탈퇴
	/**
	 * 회원탈퇴
	 * @param email 탈퇴할 계정의 이메일 
	 * @param pw 탈퇴할 계정의 비밀번호
	 * @return 성공 시 1
	 */
	@Override
	public int leave(String email, String pw) {
		String pwHash = PasswordDigest.getSha512(pw);
		return userDAO.deleteUserByEmailAndPw(email, pwHash);
	}

	// 회원 정보 수정
	/**
	 * 회원 정보를 수정함(비밀번호 제외)
	 * @param userVO 회원정보를 수정할 VO객체
	 * @param pw 수정권한을 위한 비밀번호 확인
	 * @return 성공 시 1 반환
	 */
	@Override
	public int modify(UserVO userVO, String pw) {
		return userDAO.updateUserInfoExceptPW(userVO);
	}

	// 비밀번호 수정
	/**
	 * 계정의 비밀번호를 수정함
	 * @param userNum 비밀번호를 변경할 사용자의 사용자번호
	 * @param olderPw 이전 패스워드
	 * @param newPw 새 패스워드
	 * @return
	 */
	@Override
	public int changePassword(long userNum, String olderPw, String newPw) {
		String oldPwHash = PasswordDigest.getSha512(olderPw);
		String newPwHash = PasswordDigest.getSha512(olderPw);
		return userDAO.updateUserPwHash(userNum, oldPwHash, newPwHash);
	}

	// 회원 이름 검색
	/**
	 * 이름으로 회원을 검색함
	 * @param username 검색할이름
	 * @return 검색결과 
	 */
	@Override
	public List<UserVO> searchNickname(String username) {		
		return userDAO.selectAllUsersByUsername(username, true);
	}


}
