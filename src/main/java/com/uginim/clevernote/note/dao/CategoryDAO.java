package com.uginim.clevernote.note.dao;

import java.util.List;

import com.uginim.clevernote.note.vo.CategoryVO;

/**
 * 
 * @author Hyeonuk
 *
 */
public interface CategoryDAO {
	/* Create */
	/** 	
	 * 새 category를 추가함
	 * @param newCategory
	 * @return 성공시 1
	 */
	int insertCategory(CategoryVO newCategory);	
	
	/* Read */
	/**
	 * 전체 카데고리 불러오기
	 * @param usernum 검색할 user의 usernum
	 * @return 전체 category 리스트
	 */
	List<CategoryVO> loadAllCateogries(long usernum);
	
	/* Update */
	/**
	 * category내용을 변경함
	 * @param categoryVO
	 * @return 성공 시 1
	 */
	int modifyCategory(CategoryVO categoryVO);
	
	/* Delete */
	/**
	 * 카데고리를 삭제함
	 * @param categorynum
	 * @return 성공 시 1
	 */
	int deleteCategory(long categorynum);
	
}
