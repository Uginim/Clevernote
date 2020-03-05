package com.uginim.clevernote.note.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.uginim.clevernote.note.dao.CategoryDAO;
import com.uginim.clevernote.note.dao.NoteDAO;
import com.uginim.clevernote.note.dao.TagDAO;
import com.uginim.clevernote.note.vo.CategoryVO;
import com.uginim.clevernote.note.vo.NoteVO;
import com.uginim.clevernote.note.vo.TagVO;
import com.uginim.clevernote.note.vo.TaggingVO;
import com.uginim.clevernote.user.dao.UserDAO;

@Service
public class NoteManager implements NoteService {
	public static final Logger logger = LoggerFactory.getLogger(NoteService.class);
	@Inject 
	NoteDAO noteDAO;
	
	@Inject
	TagDAO tagDAO;
	
	@Inject 
	CategoryDAO categoryDAO;
	
	@Inject 
	UserDAO userDAO;
	
	
	// 노트 쓰기
	/**
	 * 노트쓰기
	 * @param title       새 노트 제목
	 * @param content     새 노트 컨텐츠
	 * @param categoryNum 카데고리 번호
	 * @return 새로 생성한 노트의 noteVO 객체 실패 시 null
	 */
	@Override
	public NoteVO writeNote(String title, String content, long categoryNum) {
		NoteVO newNoteVO = new NoteVO();
		newNoteVO.setTitle(title);
		newNoteVO.setContent(content);
		CategoryVO category = new CategoryVO();
		category.setCategoryNum(categoryNum);		
		newNoteVO.setCategory(category);
		return writeNote(newNoteVO);
	}

	/**
	 * 노트쓰기 (태그도 추가)
	 * @param title       새 노트 제목
	 * @param content     새 노트 컨텐츠
	 * @param categoryNum 카데고리 번호
	 * @param tags        노트의 태그들
	 * @return 새로 생성한 노트의 noteVO 객체
	 */
	@Override
	public NoteVO writeNote(String title, String content, long categoryNum, String[] tags) {
		NoteVO newNoteVO = new NoteVO();
		newNoteVO.setTitle(title);
		newNoteVO.setContent(content);
		CategoryVO category = new CategoryVO();
		category.setCategoryNum(categoryNum);		
		newNoteVO.setCategory(category);
		return writeNote(newNoteVO, tags);
	}

	/**
	 * 노트쓰기
	 * @param noteVO 새로 생성한 노트의 noteVO 객체
	 * @return noteVO 객체(argument객체에서 noteNum필드에 값이 생김)
	 */
	@Override
	public NoteVO writeNote(NoteVO noteVO) {
		int noteInsertionResult = noteDAO.insert(noteVO);
		logger.info("note insertion result: "+ noteInsertionResult + " " + noteVO);
		if(noteInsertionResult==1) 
			return noteVO;
		else 
			return null;
	}

	/**
	 * 노트쓰기 (태그도 추가)
	 * @param noteVO 새로 생성한 노트의 noteVO 객체
	 * @param tags   노트의 태그들
	 * @return 객체(argument객체에서 noteNum필드에 값이 생김)
	 */
	@Override
	public NoteVO writeNote(NoteVO noteVO, String[] tags) {
		int noteInsertionState = noteDAO.insert(noteVO);
		logger.info("note insertion result: "+ noteInsertionState + " " + noteVO);
		// 태그 등록
		
		if(noteInsertionState==1) {
			int tagsInsertionState = addTagsToNote(tags,noteVO.getNoteNum());
			logger.info("tagsInsertionState:"+tagsInsertionState );
			return noteVO;
		}
		else 
			return null;
	}

	// 노트 내용 변경
	/**
	 * 노트 내용 변경하기
	 * noteNum값을 기준으로 변경 대상을 결정함
	 * @param noteVO 변경된 내용의 noteVO객체
	 * @return 성공 시 1 반환
	 */
	@Override
	public int modifyNote(NoteVO noteVO) {		
		return noteDAO.modify(noteVO);
	}

	// 노트 카데고리 옮기기
	/**
	 * 노트를 다른 카데고리로 옮김
	 * @param noteNum
	 * @param anotherCategoryNum
	 * @return 성공 시 1 반환
	 */
	@Override
	public int moveNoteToAnotherCategory(long noteNum, long destCategoryNum) {
		NoteVO noteVO = new NoteVO();
		noteVO.setNoteNum(noteNum);
		// 카데고리 VO 객체 생성
		noteVO.setCategory(new CategoryVO());
		noteVO.getCategory().setCategoryNum(destCategoryNum);		
		return noteDAO.modify(noteVO);
	}

	/**
	 * 노트를 다른 카데고리로 옮김
	 * @param noteVO 바뀐위치의 categoryNum이 담긴 noteVO
	 * @return 성공 시 1 반환
	 */
	@Override
	public int moveNoteToAnotherCategory(NoteVO noteVO) {
		return noteDAO.modify(noteVO);
	}

	/**
	 * 노트를 다른 카데고리로 옮김
	 * @param noteVO             원본 noteVO
	 * @param anotherCategoryNum 옮길 Cateogry num
	 * @return 성공 시 1 반환
	 */
	@Override
	public int moveNoteToAnotherCategory(NoteVO noteVO, long destCategoryNum) {
		noteVO.getCategory().setCategoryNum(destCategoryNum);
		return noteDAO.modify(noteVO);
	}

	// 노트 카데고리 목록 불러오기
	/**
	 * 사용자의 카데고리 모든 목록을 가져온다.
	 * @param userNum 카데고리를 조회할 사용자의 번호
	 * @return 사용자의 리스트
	 */
	@Override
	public List<CategoryVO> getAllCategories(long userNum) {		
		return categoryDAO.loadUsersAllCateogries(userNum);
	}

	// 카데고리 이름 변경
	/**
	 * 카데고리 이름변경
	 * @param categoryNum 바꿀 대상 카데고리
	 * @param newTitle    새로운 타이틀
	 * @return 성공 시 1 반환
	 */
	@Override
	public int changeCategoryTitle(long categoryNum, String newTitle) {
		CategoryVO categoryVO  = categoryDAO.selectOneCateogry(categoryNum);
		categoryVO.setTitle(newTitle);
		return categoryDAO.modify(categoryVO);
	}

	// 카데고리 추가
	/**
	 * 새 카데고리를 추가함
	 * @param userNum user번호
	 * @param title   제목
	 * @return 새로 생성한 카데고리
	 */
	@Override
	public CategoryVO createCategory(long userNum, String title) {
		CategoryVO newCategory = new CategoryVO();
		newCategory.setOwnerNum(userNum);
		newCategory.setTitle(title);		
		int state = categoryDAO.insert(newCategory);		
		if(state == 1) {
			return newCategory;
		}else {			
			return null;
		}
	}

	// 카데고리 내 모든 노트 불러오기
	/**
	 * 카데고리에 모든 노트를 가져옴
	 * @param CategoryNum 노트를 가져올 카데고리
	 * @param hasContents 내용 포함 여부 
	 * @return 카데고리 내부의 모든 노트
	 */
	@Override
	public List<NoteVO> getAllNoteFromCategory(long categoryNum, boolean hasContents) {		
		return noteDAO.getAllNoteFromCategory(categoryNum, hasContents);
	}

	// 노트 검색
	/**
	 * 노트를 검색함
	 * @param userNum 노트를 검색할 유저
	 * @param keyword 검색 키워드
	 * @return 검색된 noteVO 객체의 리스트
	 */
	@Override
	public List<NoteVO> searchNotes(long userNum, String keyword) {		
		return noteDAO.searchNotes(keyword,userNum);
	}

	/**
	 * 노트를 검색함(카데고리 내에서)
	 * @param userNum 노트를 검색할 유저
	 * @param keyword 검색 키워드
	 * @param categoryNum 검색할 카데고리
	 * @return 검색된 noteVO 객체의 리스트
	 */
	@Override
	public List<NoteVO> searchNotes(long userNum, String keyword, long categoryNum) { 
		return noteDAO.searchNotes(keyword, userNum, categoryNum);
	}

	/**
	 * 노트를 검색함(카데고리 내에서)
	 * @param userNum 노트를 검색할 유저
	 * @param keyword 검색 키워드
	 * @param categoryNum 검색할 카데고리
	 * @param hasContent 콘텐츠 포함 여부
	 * @return 검색된 noteVO 객체의 리스트
	 */
	public List<NoteVO> searchNotes(long userNum, String keyword, long categoryNum, boolean hasContent){
		return noteDAO.searchNotes(keyword, userNum, categoryNum, hasContent);
	}
	
	// 노트 태그 추가
	/**
	 * 노트에 태그를 추가함
	 * @param tag 추가할 태그
	 * @param noteVO 추가할 대상 노트
	 * @return 성공 시 1
	 */
	@Override
	public int addTagToNote(String tag, NoteVO noteVO) {		
		return addTagToNote(tag, noteVO.getNoteNum()) ;
	}

	/**
	 * 노트에 태그를 추가함
	 * @param tag 추가할 태그
	 * @param noteNum 추가할 대상 노트 번호
	 * @return 성공 시 1
	 */
	@Override
	public int addTagToNote(String tag, long noteNum) {
		TagVO tagVO = tagDAO.selectOneTag(tag);
		int state = 0; 
		if (tagVO == null) { // 존재하지 않음
			// 새 태그 넣기
			tagVO = new TagVO();
			tagVO.setWord(tag);
			state = tagDAO.insertNewTag(tagVO);
			logger.info("tag insertion result: " + state + " " + tagVO);
			if(state!=1)
				return state;
		}		
		state = tagDAO.insertNewTagging(tagVO.getTagNum(), noteNum);
		logger.info("tagging insertion result: " + state);
		return state;
	}

	/**
	 * 노트에 복수개의 태그를 추가함
	 * @param tags 추가할 태그
	 * @param noteVO 추가할 대상 노트
	 * @return 성공 시 1
	 */
	@Override
	public int addTagsToNote(String[] tags, NoteVO noteVO) {
		
		return addTagsToNote(tags,noteVO.getNoteNum());
	}

	/**
	 * 노트에 태그를 추가함
	 * @param tags 추가할 태그
	 * @param noteNum 추가할 대상 노트 번호
	 * @return 성공 시 1
	 */
	@Override
	public int addTagsToNote(String[] tags, long noteNum) {
		int state = 0;
		for(String tag: tags) {
			state = addTagToNote(tag, noteNum);
			if(state!=1)
				return state;
		}
		return state;
	}

	// 노트 태그 없애기
	/**
	 * 노트에서 태그 지우기
	 * @param targetTag 지울 대상 태그
	 * @param noteVO 노트 객체
	 * @return 성공 시 1
	 */
	@Override
	public int removeTagFromNote(String targetTag, NoteVO noteVO) {
		return removeTagFromNote(targetTag, noteVO.getNoteNum());
	}
	
	/**
	 * 노트에서 태그 지우기
	 * @param targetTag 지울 대상 태그
	 * @param noteNum 노트 번호
	 * @return 성공 시 1
	 */
	@Override
	public int removeTagFromNote(String targetTag, long noteNum) {
		TagVO tagVO = tagDAO.selectOneTag(targetTag);
		return removeTagFromNote(tagVO.getTagNum(), noteNum);
	}

	/**
	 * 노트에서 태그 지움(태그번호 사용)
	 * @param tagNum 지울 대상 태그번호
	 * @param noteVO 태그가 지워질 노트 번호
	 * @return 성공 시 1
	 */
	@Override
	public int removeTagFromNote(long tagNum, NoteVO noteVO) {		
		return removeTagFromNote(tagNum, noteVO.getNoteNum());
	}

	/**
	 * 노트에서 태그 지움(태그번호 사용)
	 * @param tagNum 지울 대상 태그 번호
	 * @param noteNum 태그가 지워질 노트 번호
	 * @return 성공 시 1
	 */
	@Override
	public int removeTagFromNote(long tagNum, long noteNum) {
		return tagDAO.deleteTagging(noteNum, tagNum);
	}

	// 노트의 태그 불러오기
	/**
	 * 노트에 태깅된 모든 태그들을 불러온다.  
	 * @param noteVO 대상 노트 VO객체
	 * @return TaggingVO 리스트
	 */
	@Override
	public List<TaggingVO> getAllTaggingsFromNote(NoteVO noteVO) {		
		return getAllTaggingsFromNote(noteVO.getNoteNum());
	}

	/**
	 * 노트에 태깅한 모든 태그들을 불러옴
	 * @param noteNum 대상 노트 번호
	 * @return TaggingVO 객체 리스트
	 */
	@Override
	public List<TaggingVO> getAllTaggingsFromNote(long noteNum) {
		return tagDAO.selectAllTaggings(noteNum);
	}
	
	
	// 유저의 태그 불러오기
	/**
	 * 해당 유저가 사용하는 모든 태그를 불러옴
	 * @param userNum 
	 * @return 태그 리스트 
	 */
	@Override
	public List<TagVO> getAllTagsOfUser(long userNum) {		
		return tagDAO.selectAllTagsOfUser(userNum);
	}

}
