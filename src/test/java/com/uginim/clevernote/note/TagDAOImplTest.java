package com.uginim.clevernote.note;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.uginim.clevernote.note.dao.CategoryDAO;
import com.uginim.clevernote.note.dao.NoteDAO;
import com.uginim.clevernote.note.dao.TagDAO;
import com.uginim.clevernote.note.vo.CategoryVO;
import com.uginim.clevernote.note.vo.NoteVO;
import com.uginim.clevernote.note.vo.TagVO;
import com.uginim.clevernote.note.vo.TaggingVO;
import com.uginim.clevernote.user.dao.UserDAO;
import com.uginim.clevernote.user.vo.UserVO;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class TagDAOImplTest {
	public static final Logger logger = LoggerFactory.getLogger(TagDAOImplTest.class);
	
	@Inject
	NoteDAO noteDAO;

	@Inject
	CategoryDAO categoryDAO;
	
	@Inject
	UserDAO userDAO;
	
	@Inject
	TagDAO tagDAO;
	
	
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
//
	}
	
	public NoteVO insertNote(String title,String content,CategoryVO category) {		
		NoteVO noteVO = new NoteVO();		
		noteVO.setCategory(category);
		noteVO.setTitle(title);
		noteVO.setContent(content);
		noteDAO.insert(noteVO);
		return noteVO;
	}
	
	@Test
	@DisplayName(value = "insert")
	@Transactional
	public void insertTagAndTagging() {
		int cnt =0;
		logger.info("insertTagAndTagging()");
		String newTag = "새로운 태그~";
		String newTag2 = "두번째 태그";
		cnt = tagDAO.insertNewTag(newTag);
		assertEquals(1, cnt);
		TagVO tag = new TagVO();
		tag.setWord(newTag2);
		cnt = tagDAO.insertNewTag(tag);
		assertEquals(1, cnt);
		logger.info("tag.getTagNum(): "+tag.getTagNum());
		NoteVO noteVO = insertNote("newNote","내용",category);		
		TaggingVO tagging = new TaggingVO();
		tagging.setNote(noteVO);
		tagging.setTag(tag);
		tagDAO.insertNewTagging(tagging);
		
		cnt = tagDAO.deleteTag(newTag);		
		assertEquals(1, cnt);
		
		cnt = tagDAO.deleteTagging(noteVO.getNoteNum(), tagging.getTag().getTagNum());
		assertEquals(1, cnt);
		
		noteDAO.delete(noteVO.getNoteNum());
		assertEquals(1, cnt);
		cnt = tagDAO.deleteTag(newTag2);
		assertEquals(1, cnt);
	}
	
	@Test
	@DisplayName(value="select")
	@Transactional
	public void testSelect() {
		logger.info("testSelect()");
		
		String tagName = "태그입니다";
		List<NoteVO> noteList = new LinkedList<NoteVO>();
		List<TagVO> tagList = new LinkedList<TagVO>();
		String noteTitle = "제목";
		String noteContent = "내용";
		int cnt=0;
		for(int i=0;i<5;i++ ) {
			TagVO tag = new TagVO();
			tag.setWord(tagName +i);
			cnt = tagDAO.insertNewTag(tag);
			tagList.add(tag);
			assertEquals(1, cnt);
			noteList.add(insertNote(noteTitle+i,noteContent+i,category));
		}
		TaggingVO tagging = new TaggingVO();
		for(int i=0;i<5;i++) {
			for(int j=0;j<i;j++) {
				tagging.setNote(noteList.get(i));
				tagging.setTag(tagList.get(j));
				tagDAO.insertNewTagging(tagging);
			}
		}
		
		List<NoteVO>list = noteDAO.selectNotesByTag(tagList.get(2).getTagNum());
		logger.info("태그로 노트검색 결과: "+list);
		
		list = noteDAO.selectNotesByTag(tagList.get(3).getTagNum(), user.getUserNum());
		logger.info("태그로 노트 검색 결과: "+list);
		
		List<TaggingVO> taggings = tagDAO.selectAllTaggings(noteList.get(3).getNoteNum());
		logger.info("노트의 태깅 정보: "+taggings.toString());
		
		List<TagVO> tags = tagDAO.selectTags("태그입니다");
		List<TagVO> userstags = tagDAO.selectTags("태그입니다",user.getUserNum());
		logger.info("태그 검색 결과:"+tags);
		logger.info("user의 태그 검색 결과:"+userstags);
		
		
		for(int i=0;i<5;i++) {
			for(int j=0;j<5;j++) {
				tagDAO.deleteTagging(noteList.get(i).getNoteNum(), tagList.get(j).getTagNum());
			}
		}
		for(int i=0;i<5;i++ ) {			
			cnt = tagDAO.deleteTag(tagName+i);
			assertEquals(1, cnt);
			noteDAO.delete(noteList.get(0).getNoteNum());
			noteList.remove(0);
		}
	}
	
	@AfterEach
	@Transactional
	public void afterTest() {
		userDAO.deleteUserByEmail(user.getEmail());
		
	}
	
	
}
