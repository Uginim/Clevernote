package com.uginim.clevernote.note;

import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.uginim.clevernote.note.dao.NoteDAO;
import com.uginim.clevernote.note.vo.NoteVO;
import com.uginim.clevernote.user.dao.UserDAO;
import com.uginim.clevernote.user.vo.UserVO;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations="file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class NoteDAOImplTest {
	
	@Inject
	NoteDAO noteDAO;
	
	@Inject
	UserDAO userDAO;
	UserVO user;
	
	@BeforeAll
	@Transactional
	public void beforeTest(){
		user = new UserVO();
		user.setEmail("note@tester.com");
		user.setPwHash("tempHash");
		user.setUsername("noteTester");
		userDAO.insertNewUser(user);
	}
	
	@AfterAll
	@Transactional
	public void afterTest() {
		userDAO.deleteUserByEmail(user.getEmail());
	}
	
	/* Create */
	@Test
	@Transactional
	public void testInsert() {
		NoteVO noteVO = new NoteVO();
		
		noteVO.setContent("");
		noteDAO.insert(noteVO);
	}
	
	/* Read */
	/**
	 * 노트를 검색한다.
	 * @param keyword
	 * @return 검색결과 노트 리스트
	 */
//	List<NoteVO> searchNotes(String keyword);
	
	/** 
	 * 특정 카데고리의 노트를 리스트로 가져온다
	 * @param categorynum 노트가 속한 카데고리
	 * @return 노트 리스트
	 */
//	List<NoteVO> getAllNoteFromCategory(long categorynum);
	
	/**
	 * 특정 노트를 가져온다.
	 * @param notenum 가져올 노트의 노트번호
	 * @return 노트객체
	 */
//	NoteVO getNote(long notenum);
	
	/* Update */
	/**
	 * 노트 수정 
	 * @param noteVO notenum이 가르키는 record의 데이터를 대체함
	 * @return 성공시 1
	 */
//	int modify(NoteVO noteVO);
	
	
	
	/* Delete */
	/**
	 * 노트 삭제
	 * @param notenum 삭제할 노트의 노트번호
	 * @return 성공시 1
	 */
//	int delete(long notenum);
}
