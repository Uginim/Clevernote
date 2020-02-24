package com.uginim.clevernote.note;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.uginim.clevernote.common.PasswordDigest;
import com.uginim.clevernote.note.dao.CategoryDAO;
import com.uginim.clevernote.note.vo.CategoryVO;
import com.uginim.clevernote.user.dao.UserDAO;
import com.uginim.clevernote.user.vo.UserVO;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class CategoryDAOImplTest {
	static final Logger logger = LoggerFactory.getLogger(CategoryDAOImplTest.class);
	@Inject
	CategoryDAO categoryDAO;
	
	@Inject
	UserDAO userDAO;
	
	static String userEmail = "category@test.test";
	static long userNum;
	
	@BeforeEach
	public void doBeforeAll() {
		logger.info("doBeforeAll(): start");
		String email = userEmail;
		UserVO newUserVO = new UserVO();
		newUserVO.setEmail(email);
		newUserVO.setPwHash(PasswordDigest.getSha512("password"));
		newUserVO.setUsername("username");
		int cnt = 0;
		cnt = userDAO.insertNewUser(newUserVO);
		userNum = newUserVO.getUserNum();
		logger.info("newUsers userNum : " +userNum );
	}
	
	@Test
	@Transactional
	public void insertCategory() {
		logger.info("insertCategory()");
		CategoryVO newCategory = new CategoryVO();
		newCategory.setTitle("새 타이틀");
		newCategory.setOwnerNum(userNum);;
		int cnt = categoryDAO.insert(newCategory);
		assertEquals(1, cnt);
		cnt = categoryDAO.delete(newCategory.getCategoryNum());
		assertEquals(1, cnt);		
	}
	
	@AfterEach
	public void doAfterAll() {
		logger.info("doAfterAll(): start");
		userDAO.deleteUserByEmail(userEmail);
	}
	
}
