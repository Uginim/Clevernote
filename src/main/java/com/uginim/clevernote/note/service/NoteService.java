package com.uginim.clevernote.note.service;

import java.util.List;

import com.uginim.clevernote.note.vo.CategoryVO;
import com.uginim.clevernote.note.vo.NoteVO;
import com.uginim.clevernote.note.vo.TagVO;
import com.uginim.clevernote.note.vo.TaggingVO;

/**
 * 
 * @author Hyeonuk
 *
 */
public interface NoteService {
	// 노트 쓰기	
	/**
	 * 노트쓰기
	 * @param title 새 노트 제목
	 * @param content 새 노트 컨텐츠
	 * @param categoryNum 카데고리 번호
	 * @return 새로 생성한 노트의 noteVO 객체
	 */
	NoteVO writeNote(String title, String content, long categoryNum);
	/**
	 * 노트쓰기 (태그도 추가)
	 * @param title 새 노트 제목
	 * @param content 새 노트 컨텐츠
	 * @param categoryNum 카데고리 번호
	 * @param tags 노트의 태그들
	 * @return 새로 생성한 노트의 noteVO 객체
	 */
	NoteVO writeNote(String title, String content, long categoryNum, String[] tags);
	/**
	 * 노트쓰기
	 * @param noteVO 새로 생성한 노트의 noteVO 객체
	 * @return noteVO 객체(argument객체에서 noteNum필드에 값이 생김)
	 */
	NoteVO writeNote(NoteVO noteVO);
	/**
	 * 노트쓰기 (태그도 추가)
	 * @param noteVO 새로 생성한 노트의 noteVO 객체
	 * @param tags 노트의 태그들
	 * @return 객체(argument객체에서 noteNum필드에 값이 생김)
	 */
	NoteVO writeNote(NoteVO noteVO, String[] tags);
	// 노트 내용 변경
	/**
	 * 노트 내용 변경하기 
	 * @param noteVO 변경된 내용의 noteVO객체
	 * @return 성공 시 1 반환 
	 */
	int modifyNote(NoteVO noteVO);
	// 노트 카데고리 옮기기
	/**
	 * 노트를 다른 카데고리로 옮김
	 * @param noteNum
	 * @param anotherCategoryNum
	 * @return 성공 시 1 반환
	 */
	int moveNoteToAnotherCategory(long noteNum,long destCategoryNum);
	
	/**
	 * 노트를 다른 카데고리로 옮김
	 * @param noteVO 바뀐위치의 categoryNum이 담긴 noteVO
	 * @return 성공 시 1 반환
	 */
	int moveNoteToAnotherCategory(NoteVO noteVO);
	/**
	 * 노트를 다른 카데고리로 옮김
	 * @param noteVO 원본 noteVO
	 * @param anotherCategoryNum 옮길 Cateogry num
	 * @return 성공 시 1 반환
	 */
	int moveNoteToAnotherCategory(NoteVO noteVO,long destCategoryNum);
	
	// 노트 카데고리 목록 불러오기
	/**
	 * 사용자의 카데고리 모든 목록을 가져온다.
	 * @param userNum 카데고리를 조회할 사용자의 번호
	 * @return 사용자의 리스트 
	 */
	List<CategoryVO> getAllCategories(long userNum);
	// 카데고리 이름 변경
	/**
	 * 카데고리 이름변경
	 * @param categoryNum 바꿀 대상 카데고리
	 * @param newTitle 새로운 타이틀
	 * @return 성공 시 1 반환
	 */
	int changeCategoryTitle(long categoryNum, String newTitle); 
	// 카데고리 추가
	/**
	 * 새 카데고리를 추가함
	 * @param userNum user번호 
	 * @param title 제목
	 * @return 새로 생성한 카데고리 
	 */
	CategoryVO createCategory(long userNum, String title);
	// 카데고리 내 모든 노트 불러오기
	/**
	 * 카데고리에 모든 노트를 가져옴
	 * @param CategoryNum 노트를 가져올 카데고리
	 * @param hasContent 내용 포함 여부 
	 * @return 카데고리 내부의 모든 노트
	 */
	List<NoteVO> getAllNoteFromCategory(long categoryNum, boolean hasContent);
	
	// 노트 검색
	
//	List<NoteVO> searchNote(String keyword);
//	List<NoteVO> searchNote(String keyword, long CategoryNum);
	/**
	 * 노트를 검색함
	 * @param userNum 노트를 검색할 유저
	 * @param keyword 검색 키워드
	 * @return 검색된 noteVO 객체의 리스트
	 */
	List<NoteVO> searchNotes(long userNum, String keyword);
	/**
	 * 노트를 검색함(카데고리 내에서)
	 * @param userNum 노트를 검색할 유저
	 * @param keyword 검색 키워드
	 * @param categoryNum 검색할 카데고리
	 * @return 검색된 noteVO 객체의 리스트
	 */
	List<NoteVO> searchNotes(long userNum, String keyword, long categoryNum);
	
	/**
	 * 노트를 검색함(카데고리 내에서)
	 * @param userNum 노트를 검색할 유저
	 * @param keyword 검색 키워드
	 * @param categoryNum 검색할 카데고리
	 * @param hasContent 콘텐츠 포함 여부
	 * @return 검색된 noteVO 객체의 리스트
	 */
	List<NoteVO> searchNotes(long userNum, String keyword, long categoryNum, boolean hasContent);
	
	
	// 노트 태그 추가
	/**
	 * 노트에 태그를 추가함
	 * @param tag 추가할 태그
	 * @param noteVO 추가할 대상 노트
	 * @return 성공 시 1
	 */
	int addTagToNote(String tag,NoteVO noteVO) ;
	/**
	 * 노트에 태그를 추가함
	 * @param tag 추가할 태그
	 * @param noteNum 추가할 대상 노트 번호
	 * @return 성공 시 1
	 */
	int addTagToNote(String tag, long noteNum) ;
	/**
	 * 노트에 복수개의 태그를 추가함
	 * @param tags 추가할 태그
	 * @param noteVO 추가할 대상 노트
	 * @return 성공 시 1
	 */
	int addTagsToNote(String[] tags,NoteVO noteVO) ;
	/**
	 * 노트에 태그를 추가함
	 * @param tags 추가할 태그
	 * @param noteNum 추가할 대상 노트 번호
	 * @return 성공 시 1
	 */
	int addTagsToNote(String[] tags, long noteNum) ;
	// 노트 태그 없애기
	/**
	 * 노트에서 태그 지우기
	 * @param targetTag 지울 대상 태그
	 * @param noteVO 노트 객체
	 * @return 성공 시 1
	 */
	int removeTagFromNote(String targetTag,NoteVO noteVO);
	/**
	 * 노트에서 태그 지우기
	 * @param targetTag 지울 대상 태그
	 * @param noteNum 노트 번호
	 * @return 성공 시 1
	 */
	int removeTagFromNote(String targetTag,long noteNum);
	/**
	 * 노트에서 태그 지움(태그번호 사용)
	 * @param tagNum 지울 대상 태그번호
	 * @param noteVO 태그가 지워질 노트 번호
	 * @return 성공 시 1
	 */
	int removeTagFromNote(long tagNum,NoteVO noteVO);
	/**
	 * 노트에서 태그 지움(태그번호 사용)
	 * @param tagNum 지울 대상 태그 번호
	 * @param noteNum 태그가 지워질 노트 번호
	 * @return 성공 시 1
	 */
	int removeTagFromNote(long tagNum,long noteNum);

	// 노트의 태그 불러오기
	/**
	 * 노트에 태깅된 모든 태그들을 불러온다.  
	 * @param noteVO 대상 노트 VO객체
	 * @return TaggingVO 리스트
	 */
	List<TaggingVO> getAllTaggingsFromNote(NoteVO noteVO);
	/**
	 * 노트에 태깅한 모든 태그들을 불러옴
	 * @param noteNum 대상 노트 번호
	 * @return TaggingVO 객체 리스트
	 */
	List<TaggingVO> getAllTaggingsFromNote(long noteNum);
	
	// 유저의 태그 불러오기
	/**
	 * 해당 유저가 사용하는 모든 태그를 불러옴
	 * @param userNum 
	 * @return 태그 리스트 
	 */
	List<TagVO> getAllTagsOfUser(long userNum);
}
