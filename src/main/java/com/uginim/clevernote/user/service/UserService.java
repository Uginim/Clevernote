package com.uginim.clevernote.user.service;

import java.util.List;

import com.uginim.clevernote.user.vo.UserVO;

public interface UserService {
	// 계정 생성
	/**
	 * 계정생성
	 * @param userVO 새로운 userVO객체
	 * @return 성공 시 1 
	 */
	int signUp(UserVO userVO);
	
	// 계정 삭제
	/**
	 * 계정 삭제
	 * @param email 탈퇴할 계정의 이메일 
	 * @param pw 탈퇴할 계정의 비밀번호
	 * @return 성공 시 1
	 */
	int leave(String email, String pw);
	// 회원 정보 수정
	/**
	 * 회원 정보를 수정함(비밀번호 제외)
	 * @param userVO 회원정보를 수정할 VO객체
	 * @param pw 수정권한을 위한 비밀번호 확인
	 * @return 성공 시 1 반환
	 */
	int modify(UserVO userVO, String pw);
	// 비밀번호 수정
	/**
	 * 계정의 비밀번호를 수정함
	 * @param userNum 비밀번호를 변경할 사용자의 사용자번호
	 * @param olderPw 이전 패스워드
	 * @param newPw 새 패스워드
	 * @return
	 */
	int changePassword(long userNum, String olderPw, String newPw);
	
	// 회원 이름 검색
	/**
	 * 이름으로 회원을 검색함
	 * @param username 검색할이름
	 * @return 검색결과 
	 */
	List<UserVO> searchNickname(String username);
	// 친구 추가
	// 
}
