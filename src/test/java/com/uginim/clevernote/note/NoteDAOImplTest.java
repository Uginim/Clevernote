package com.uginim.clevernote.note;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.uginim.clevernote.note.dao.CategoryDAO;
import com.uginim.clevernote.note.dao.NoteDAO;
import com.uginim.clevernote.note.vo.CategoryVO;
import com.uginim.clevernote.note.vo.NoteVO;
import com.uginim.clevernote.user.dao.UserDAO;
import com.uginim.clevernote.user.vo.UserVO;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations="file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class NoteDAOImplTest {
	public final static Logger logger = LoggerFactory.getLogger(NoteDAOImplTest.class);
	
	@Inject
	NoteDAO noteDAO;

	@Inject
	CategoryDAO categoryDAO;
	
	@Inject
	UserDAO userDAO;
	
	CategoryVO category;
	UserVO user;
	
	
	
	
	@BeforeEach
	@Transactional
	public void beforeTest(){
		user = new UserVO();
		user.setEmail("note@tester.com");
		user.setPwHash("tempHash");
		user.setUsername("noteTester");
		userDAO.insertNewUser(user);
		category = categoryDAO.loadUsersAllCateogries(user.getUserNum()).get(0);

	}
	
	@AfterEach
	@Transactional
	public void afterTest() {
		userDAO.deleteUserByEmail(user.getEmail());
	}
	
	/* Create */
	@Test
	@Transactional
	public void testInsert() {
		insertNote("새 타이틀","새 콘텐츠",category);	
	}
	public NoteVO insertNote(String title,String content,CategoryVO category) {		
		NoteVO noteVO = new NoteVO();		
		noteVO.setCategory(category);
		noteVO.setTitle(title);
		noteVO.setContent(content);
		noteDAO.insert(noteVO);
		return noteVO;
	}
	
	/* Read */
	/**
	 * 노트를 검색한다.
	 * @param keyword
	 * @return 검색결과 노트 리스트
	 */
	@Test
	@Transactional
	public void readNotes() {
		List<NoteVO> list = new ArrayList<NoteVO>();
		for(int i=0;i<25;i++) {
			list.add(insertNote("제목"+i,"내용"+i,category));
			logger.info("new note's num"+list.get(i).getNoteNum());
		}
		
		List<NoteVO> searchingResult = noteDAO.searchNotes("1");
		logger.info(searchingResult.toString());
		List<NoteVO> notesInCate = noteDAO.getAllNoteFromCategory(category.getCategoryNum());
		logger.info(notesInCate.toString());
		NoteVO note = noteDAO.getNote(list.get(5).getNoteNum());
		logger.info(note.toString());
//		searchNotes(keyword);
	}
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
	@Test
	@Transactional
	public void modifyNotes() {
		List<NoteVO> list = new ArrayList<NoteVO>();
		for(int i=0;i<5;i++) {
			list.add(insertNote("제목"+i,"내용"+i,category));
			logger.info("new note's num"+list.get(i).getNoteNum());
		}
		for(int i=0;i<5;i++) {
			NoteVO note = list.get(i);
			note.setContent("바뀜"+i*14+note.getContent());
			note.setTitle("다른제목"+i*4);
			int result = noteDAO.modify(note);
			assertEquals(1,result);
		}
		
	}
	
	
	/* Delete */
	/**
	 * 노트 삭제
	 * @param notenum 삭제할 노트의 노트번호
	 * @return 성공시 1
	 */
//	int delete(long notenum);
	
	@Test
	@Transactional
	public void deleteNotes() {
		List<NoteVO> list = new ArrayList<NoteVO>();
		for(int i=0;i<25;i++) {
			list.add(insertNote("제목"+i,"내용"+i,category));
			logger.info("new note's num"+list.get(i).getNoteNum());
		}
		for(int i=0;i<25;i++) {
			int result = noteDAO.delete(list.get(i).getNoteNum());
			assertEquals(1,result);
		}
		
	}
}
