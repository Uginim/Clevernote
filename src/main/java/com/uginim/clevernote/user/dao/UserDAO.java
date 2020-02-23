package com.uginim.clevernote.user.dao;

import java.util.List;
/**
 * @author Hyeonuk 
 */
import com.uginim.clevernote.user.vo.UserVO;
/**
 * @
 * @author Hyeonuk
 *
 */
public interface UserDAO {
	
	/* 
	 * Create 
	 */
	/** 
	 * app_user 테이블에 레코드를 추가한다.
	 * @param userVO 새 레코드(email, username, pwHash) 의 정보가 담긴 UserVO 객체 
	 * @return 성공하면 1을 반환 
	 */
	public int insertNewUser(UserVO userVO); // Insert new user.
	
	
	/* Read */
	/**
	 * email로 계정의 정보를 검색함
	 * @param email 검색할 email(계정)
	 * @return 검색결과가 담긴 VO객체. 없으면 null
	 */
	public UserVO selectOneUser(String email); // Select an user By email
	/**
	 * email와 pwHash가 일치하는 계정을 검색함
	 * @param email 검색할 email(계정)
	 * @param pwHahs 검색할 계정의 pwHash
	 * @return 검색결과가 담긴 VO객체
	 */	
	public UserVO selectOneUser(String email, String pwHash); // Select an user By email
	/**
	 * table의 usernum(키값)으로 계정을 검색
	 * @param usernum app_user 테이블의 키값
	 * @return 검색결과가 담긴 VO객체
	 */	
	public UserVO selectOneUser(long usernum); // Select an user By email
	
	/**
	 * 해당 username으로 검색함 (username은 중복가능한 필드)	 *
	 * @param username 검색할 username 
	 * @param includeAsPart false면 완전일치 true면 부분으로 포함
	 * @return List 검색결과(중복될 수 있으므로)
	 */
	public List<UserVO> selectAllUsersByUsername(String username, boolean includeAsPart); // Select all users by username


//	public List<UserVO> selectAllUsers() ; // Select all user
	
		
	/* Delete */	
	/**
	 * email이 일치하는 레코드 삭제
	 * @param email 삭제할 계정의 email
	 * @return 성공시 1 반환
	 */
	public int deleteUserByEmail(String email); // Delete an user using the email
	/**
	 * email과 pw가 모두 일치하는 레코드 삭제
	 * @param email 삭제할 계정의 email
	 * @param pwHash 삭제할 계정의 password의 hash값
	 * @return 성공시 1 반환
	 */
	public int deleteUserByEmailAndPw(String email, String pw); // Delete an user using the email
	
	
	/* Update */
	/**
	 * userVO매개변수의 id에 해당하는 user의 값들 중 id와 pwHash를 제외한 나머지 필드를 업데이트함 
	 * @param userVO id값은 필수
	 * @return 성공시 1 반환
	 */
	public int updateUserInfoExceptPW(UserVO userVO); // Update datas of an user except password
	/**
	 * user의 pwHash를 변경함
	 * @param email pwHash를 변경할 email(계정)
	 * @param pwHash 바꿀 pwHash
	 * @return 성공 시 1 반환
	 */
	public int updateUserPwHash(String email, String pwHash); // Update an user's password
	/**
	 * user의 pwHash를 변경함 이전의 pw가 일치해야 변경가능
	 * @param email pwHash를 변경할 email(계정)
	 * @param oldPwHash 이전의 pwHash값(일치해야 변경가능)
	 * @param newPwHash 바꿀 pwHash
	 * @return 성공 시 1 반환
	 */
	public int updateUserPwHash(String email, String oldPwHash, String newPwHash); // Update an user password
	
	
}
