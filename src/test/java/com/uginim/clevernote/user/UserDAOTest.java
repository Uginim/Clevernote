package com.uginim.clevernote.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.inject.Inject;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.uginim.clevernote.common.PasswordDigest;
import com.uginim.clevernote.user.dao.UserDAO;
import com.uginim.clevernote.user.vo.UserVO;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class UserDAOTest {
	public static final Logger logger = LoggerFactory.getLogger(UserDAOTest.class);
	@Inject
	UserDAO userDAO;

	@Test
//	@Disabled
	public void insertNewUser() {
		logger.info("insertNewUser(): start"); 
		String email = "test@test2.te.st";
		UserVO newUserVO = new UserVO();
		newUserVO.setEmail(email);
		newUserVO.setPwHash(PasswordDigest.getSha512("password"));
		newUserVO.setUsername("username");
		
		int cnt=0;
		cnt = userDAO.deleteUserByEmail(email);
		cnt = userDAO.insertNewUser(newUserVO);
		assertEquals(1, cnt);
	}
	
	@Test
	public void selectOne() {
		logger.info("selectOne()");
		UserVO userVO = userDAO.selectOneUserByEmail("test@test.test");
		assertNotNull(userVO);
		assertEquals("test@test.test", userVO.getEmail());
	}
	@Test
	public void deleteOne() {
		
	}
}

