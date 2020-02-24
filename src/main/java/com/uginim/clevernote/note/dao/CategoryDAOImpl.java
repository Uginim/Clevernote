package com.uginim.clevernote.note.dao;

import java.util.List;

import javax.inject.Inject;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.uginim.clevernote.note.vo.CategoryVO;
@Repository
public class CategoryDAOImpl implements CategoryDAO {

	@Inject
	SqlSessionTemplate sqlSession;
	
	/* Create */
	/** 	
	 * 새 category를 추가함
	 * @param newCategory
	 * @return 성공시 1
	 */
	@Override
	public int insert(CategoryVO newCategory) {		
		return sqlSession.insert("mappers.NoteDAO-mapper.insert",newCategory);
	}

	/* Read */
	/**
	 * 전체 카데고리 불러오기
	 * @param usernum 검색할 user의 usernum
	 * @return 전체 category 리스트
	 */
	@Override
	public List<CategoryVO> loadUsersAllCateogries(long usernum) {
		return sqlSession.selectList("mappers.NoteDAO-mapper.loadUsersAllCateogries",usernum);
	}

	/* Update */
	/**
	 * category내용을 변경함
	 * @param categoryVO
	 * @return 성공 시 1
	 */
	@Override
	public int modify(CategoryVO categoryVO) {
		return sqlSession.update("mappers.NoteDAO-mapper.modify",categoryVO);
	}

	/* Delete */
	/**
	 * 카데고리를 삭제함
	 * @param categorynum
	 * @return 성공 시 1
	 */
	@Override
	public int delete(long categorynum) {
		return sqlSession.update("mappers.NoteDAO-mapper.delete",categorynum);
	}

}
