package com.uginim.clevernote.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
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
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml" })
public class UserDAOTest {
	public static final Logger logger = LoggerFactory.getLogger(UserDAOTest.class);
	@Inject
	UserDAO userDAO;
	
	
	@Test
//	@Disabled
	@DisplayName("Insert new user")
	public void insertNewUser() {
		logger.info("insertNewUser(): start");
		String email = "test@test2.te.st";
		UserVO newUserVO = new UserVO();
		newUserVO.setEmail(email);
		newUserVO.setPwHash(PasswordDigest.getSha512("password"));
		newUserVO.setUsername("username");
		int cnt = 0;

		cnt = userDAO.insertNewUser(newUserVO);

		cnt = userDAO.deleteUserByEmail(email);
		assertEquals(1, cnt);
	}

	@Test
	@Transactional
	@DisplayName("Select a user by email")
//	@Disabled
	public void selectOneUserByEmail() {
		logger.info("selectOneUserByEmail()");
		String email = "test@test.test";
		String username = "username";
		String pwHash = PasswordDigest.getSha512("password");
		UserVO newUserVO = new UserVO();
		newUserVO.setEmail(email);
		newUserVO.setPwHash(pwHash);
		newUserVO.setUsername(username);
		userDAO.insertNewUser(newUserVO);
		UserVO userVO = userDAO.selectOneUser(email);
		assertNotNull(userVO);
		assertEquals(email, userVO.getEmail());
		logger.info("result=" + userVO);
		int cnt = userDAO.deleteUserByEmail(email);
	}

	@Test
	@Transactional
	@DisplayName("Select a user having the email and the pwHash ")
//	@Disabled
	public void selectOneUserByEmailAndPw() {
		logger.info("selectOneUserByEmailAndPw()");
		String email = "test2@test.test";
		String username = "username";
		String pwHash = PasswordDigest.getSha512("password");
		try {
			UserVO newUserVO = new UserVO();
			newUserVO.setEmail(email);
			newUserVO.setPwHash(pwHash);
			newUserVO.setUsername(username);
			userDAO.insertNewUser(newUserVO);
			UserVO userVO = userDAO.selectOneUser(email, pwHash);
			assertNotNull(userVO);
			assertEquals(email, userVO.getEmail());
			logger.info("result=" + userVO);
			int cnt = userDAO.deleteUserByEmail(email);
		} catch (Exception e) {
			e.printStackTrace();
			assertNotNull(null);
		} finally {
			int cnt = userDAO.deleteUserByEmail(email);
		}

	}

	@Test
	@Transactional
	@DisplayName("Select a user by usernum")
//	@Disabled
	synchronized public void selectOneUserByUserNum() {
		logger.info("selectOneUserByEmailAndPw()");
		String email = "test3@test.test";
		String username = "username";
		String pwHash = PasswordDigest.getSha512("password");
		try {
			UserVO newUserVO = new UserVO();
			newUserVO.setEmail(email);
			newUserVO.setPwHash(pwHash);
			newUserVO.setUsername(username);
			userDAO.insertNewUser(newUserVO);
			long usernum = userDAO.selectOneUser(email).getUsernum();
			UserVO userVO = userDAO.selectOneUser(usernum);
			assertNotNull(userVO);
			assertEquals(email, userVO.getEmail());
			logger.info("result=" + userVO);
		} catch (Exception e) {
			e.printStackTrace();
			assertNotNull(null);
		} finally {
			int cnt = userDAO.deleteUserByEmail(email);
		}
	}

	@Test
	@Transactional
	@DisplayName("Select all user")
//	@Disabled
	synchronized public void selectAllUserByUsername() {
		logger.info("selectOneUserByEmailAndPw()");
		String[] emails = { "test1.te.st", "test2.te.st", "test3.te.st", "test4.te.st", "test5.te.st" };
		String[] usernames = { "username1", "username2", "username3", "username4", "username1" };
		String[] pwHashs = { PasswordDigest.getSha512("password1"), PasswordDigest.getSha512("password2"),
				PasswordDigest.getSha512("password3"), PasswordDigest.getSha512("password4"),
				PasswordDigest.getSha512("password5") };
		UserVO newUserVO = new UserVO();
		try {
			for (int i = 0; i < emails.length; i++) {
				newUserVO.setEmail(emails[i]);
				newUserVO.setPwHash(pwHashs[i]);
				newUserVO.setUsername(usernames[i]);
				userDAO.insertNewUser(newUserVO);
			}
			String keyword1 = "username1";
			String keyword2 = "rname";
			List<UserVO> list = userDAO.selectAllUsersByUsername(keyword1, false);
			assertNotNull(list);
			logger.info("result=" + list);
			list = userDAO.selectAllUsersByUsername(keyword2, true);
			assertNotNull(list);
			logger.info("result=" + list);
		} catch (Exception e) {
			e.printStackTrace();
			assertNotNull(null);
		} finally {
			for (String email : emails) {
				userDAO.deleteUserByEmail(email);
			}
		}
	}

	@Test
	public void deleteOne() {

	}
}
