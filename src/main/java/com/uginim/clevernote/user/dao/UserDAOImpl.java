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

@Repository 
public class UserDAOImpl implements UserDAO {
	private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);
	@Inject
	private SqlSession sqlSession;

	public int insertNewUser(UserVO userVO) {
		logger.info("UserDAOImpl.insertNewUser(UserVO userVO)");
		return sqlSession.insert("mappers.UserDAO-mapper.insertNewUser", userVO);
	}

	@Override
	public UserVO selectOneUserByEmail(String email) {
		logger.info("UserDAOImpl.selectOneUserByEmail(String email)");
		return (UserVO)sqlSession.selectOne("mappers.UserDAO-mapper.selectOneUserByEmail", email);
	}




	@Override
	public int deleteUserByEmail(String email) {
		logger.info("UserDAOImpl.deleteUserById(String email)");
		return sqlSession.delete("mappers.UserDAO-mapper.deleteUserByEmail", email);
	}

	@Override
	public UserVO selectOneUserByEmailAndPw(String email, String pwHash) {
		logger.info("UserDAOImpl.deleteUserById(String email)");
		Map<String,String> map = new HashMap<>();
		map.put("email", email);
		map.put("pwHash",pwHash);
		return sqlSession.selectOne("mappers.UserDAO-mapper.selectOneUserByEmailAndPw", map);

	}

	@Override
	public UserVO selectOneUserByUserIdx(int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserVO> selectAllUsersByUsername(String username, boolean includeAsPart) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteUserByEmailAndPw(String email, String pw) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateUserInfoExceptPW(UserVO userVO) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateUserPw(String email, String pwHash) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateUserPw(String email, String oldPwHash, String newPwHash) {
		// TODO Auto-generated method stub
		return 0;
	}


	
}
