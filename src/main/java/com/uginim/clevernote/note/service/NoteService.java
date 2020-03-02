package com.uginim.clevernote.note.service;

import java.util.List;
import java.util.Locale.Category;

import com.uginim.clevernote.note.vo.CategoryVO;
import com.uginim.clevernote.note.vo.NoteVO;

/**
 * 
 * @author Hyeonuk
 *
 */
public interface NoteService {
	// 노트 쓰기	
	NoteVO writeNote(String title, String content, long categoryNum);
	NoteVO writeNote(NoteVO noteVO);
	// 노트 내용 변경
	void modifyNote(NoteVO noteVO);
	// 노트 카데고리 옮기기
	void moveNoteToAnotherCategory(long noteNum,long anotherCategoryNum);
	void moveNoteToAnotherCategory(NoteVO noteVO);
	// 노트 카데고리 목록 불러오기
	List<CategoryVO> getAllCategories(long userNum);
	// 카데고리 이름 변경
	int changeCategoryTitle(long categoryNum, String newTitle); 
	// 카데고리 추가
	CategoryVO createCategory(long userNum, String title);
	// 카데고리 내 모든 노트 불러오기
	List<NoteVO> getAllNoteFromCategory(long CategoryNum);
	// 노트 검색
	List<NoteVO> searchNote(String keyword);
	List<NoteVO> searchNote(String keyword, long CategoryNum);
	// 노트 태그 추가
	// 노트 태그 없애기
	// 노트 태그 
}
