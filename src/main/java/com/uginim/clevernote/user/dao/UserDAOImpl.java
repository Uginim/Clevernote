package com.uginim.clevernote.user.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.uginim.clevernote.user.vo.UserVO;
/**
 * 
 * @author Hyeonuk
 *
 */
@Repository 
public class UserDAOImpl implements UserDAO {
	private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);
	@Inject
	private SqlSession sqlSession;

	/* Create */
	/** 
	 * app_user 테이블에 레코드를 추가한다.
	 * @param userVO 새 레코드(email, username, pwHash) 의 정보가 담긴 UserVO 객체 
	 * @return 성공하면 1을 반환 
	 */
	public int insertNewUser(UserVO userVO) {
		logger.info("UserDAOImpl.insertNewUser(UserVO userVO)");
		return sqlSession.insert("mappers.UserDAO-mapper.insertNewUser", userVO);
	}

	
	
	/* 
	 * Read 
	 */
	/**
	 * email로 계정의 정보를 검색함
	 * @param email 검색할 email(계정)
	 * @return 검색결과가 담긴 VO객체. 없으면 null
	 */
	@Override
	public UserVO selectOneUser(String email) {
		logger.info("UserDAOImpl.selectOneUser(String email)");
		return (UserVO)sqlSession.selectOne("mappers.UserDAO-mapper.selectOneUserByEmail", email);
	}
	
	/**
	 * email와 pwHash가 일치하는 계정을 검색함
	 * @param email 검색할 email(계정)
	 * @param pwHahs 검색할 계정의 pwHash
	 * @return 검색결과가 담긴 VO객체
	 */
	@Override
	public UserVO selectOneUser(String email, String pwHash) {
		logger.info("UserDAOImpl.deleteUserById(String email)");
		Map<String,String> map = new HashMap<>();
		map.put("email", email);
		map.put("pwHash",pwHash);	
		return sqlSession.selectOne("mappers.UserDAO-mapper.selectOneUserByEmailAndPw", map);
		
	}
	
	/**
	 * table의 usernum(키값)으로 계정을 검색
	 * @param usernum app_user 테이블의 키값
	 * @return 검색결과가 담긴 VO객체
	 */
	@Override
	public UserVO selectOneUser(long usernum) {
		logger.info("UserDAOImpl.selectOneUser(int idx)");
		return sqlSession.selectOne("mappers.UserDAO-mapper.selectOneUserByUserNum",usernum);
	}
	
	/**
	 * 해당 username으로 검색함 (username은 중복가능한 필드)	 *
	 * @param username 검색할 username 
	 * @param includeAsPart false면 완전일치 true면 부분으로 포함
	 * @return List 검색결과(중복될 수 있으므로)
	 */
	@Override
	public List<UserVO> selectAllUsersByUsername(String username, boolean includeAsPart) {
		logger.info("UserDAOImpl.selectAllUsersByUsername(String username, boolean includeAsPart)");
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("username",username);
		if(includeAsPart)
			return sqlSession.selectList("mappers.UserDAO-mapper.selectAllUsersIncludekeywordOracle",map);
		else 
			return sqlSession.selectList("mappers.UserDAO-mapper.selectAllUsersByUsername",map);
		
	}
	
	
	/* Delete */
	/**
	 * email이 일치하는 레코드 삭제
	 * @param email 삭제할 계정의 email
	 * @return 성공시 1 반환
	 */
	@Override
	public int deleteUserByEmail(String email) {
		logger.info("UserDAOImpl.deleteUserByEmail(String email)");
		return sqlSession.delete("mappers.UserDAO-mapper.deleteUserByEmail", email);
	}

		
	/**
	 * email과 pw가 모두 일치하는 레코드 삭제
	 * @param email 삭제할 계정의 email
	 * @param pwHash 삭제할 계정의 password의 hash값
	 * @return 성공시 1 반환
	 */
	@Override
	public int deleteUserByEmailAndPw(String email, String pw) {
		logger.info("UserDAOImpl.deleteUserByEmailAndPw(String email, String pw)");
		Map<String,Object> map = new HashMap<>();
		map.put("email",email);
		map.put("pw",pw);
		return sqlSession.delete("mappers.UserDAO-mapper.deleteUserByEmailAndPw",map);
	}

	
	/* Update */
	/**
	 * userVO매개변수의 id에 해당하는 user의 값들 중 id와 pwHash를 제외한 나머지 필드를 업데이트함 
	 * @param userVO id값은 필수
	 * @return 성공시 1 반환
	 */
	@Override
	public int updateUserInfoExceptPW(UserVO userVO) {
		logger.info("UserDAOImpl.updateUserInfoExceptPW(UserVO userVO)");
		return sqlSession.update("mappers.UserDAO-mapper.updateUserInfoExceptPW",userVO);
	}

	/**
	 * user의 pwHash를 변경함
	 * @param email pwHash를 변경할 email(계정)
	 * @param pwHash 바꿀 pwHash
	 * @return 성공 시 1 반환
	 */
	@Override
	public int updateUserPwHash(String email, String pwHash) {
		logger.info("UserDAOImpl.updateUserPw(String email, String pwHash)");
		Map<String,Object> map = new HashMap<>();
		map.put("email",email);
		map.put("pwHash",pwHash);
		return sqlSession.update("mappers.UserDAO-mapper.updateUserPwHash",map);
	}

	/**
	 * user의 pwHash를 변경함 이전의 pw가 일치해야 변경가능
	 * @param email pwHash를 변경할 email(계정)
	 * @param oldPwHash 이전의 pwHash값(일치해야 변경가능)
	 * @param newPwHash 바꿀 pwHash
	 * @return 성공 시 1 반환
	 */
	@Override
	public int updateUserPwHash(String email, String oldPwHash, String newPwHash) {
		logger.info("UserDAOImpl.updateUserPw(String email, String oldPwHash, String newPwHash)");
		Map<String, Object> map = new HashMap<>();
		map.put("email",email);
		map.put("oldPwHash",oldPwHash);
		map.put("newPwHash",newPwHash);
		return sqlSession.update("mappers.UserDAO-mapper.updateUserPwHashWithOldPw",map);
	}


	
}
