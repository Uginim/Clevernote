package com.uginim.clevernote.note.dao;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.uginim.clevernote.note.vo.NoteVO;

@Repository
public class NoteDAOImpl implements NoteDAO{

	@Inject
	SqlSession sqlSession;
	/* Create */
	/**
	 * 새 노트 추가
	 * @param noteVO 
	 * 			categorynum: not null
	 * 		 	title: (default "No title")
	 * 			content: (default "" blank) 			  
	 * @return 성공시 1
	 */
	@Override
	public int insert(NoteVO noteVO) {
		return sqlSession.insert("mappers.NoteDAO-mapper.insert",noteVO);
	}

	
	
	/* Read */
	/**
	 * 노트를 검색한다.
	 * @param keyword
	 * @return 검색결과 노트 리스트
	 */
	@Override
	public List<NoteVO> searchNotes(String keyword) {
		return sqlSession.selectList("mappers.NoteDAO-mapper.searchNotes", keyword);
	}

	/** 
	 * 특정 카데고리의 노트를 리스트로 가져온다
	 * @param categorynum 노트가 속한 카데고리
	 * @return 노트 리스트
	 */
	@Override
	public List<NoteVO> getAllNoteFromCategory(long categorynum) {
		return sqlSession.selectList("mappers.NoteDAO-mapper.getAllNoteFromCategory", categorynum);
	}

	/**
	 * 특정 노트를 가져온다.
	 * @param notenum 가져올 노트의 노트번호
	 * @return 노트객체
	 */
	@Override
	public NoteVO getNote(long notenum) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne("mappers.NoteDAO-mapper.getNote", notenum);
	}
	
	/* Update */
	/**
	 * 노트 수정 
	 * @param noteVO notenum이 가르키는 record의 데이터를 대체함
	 * @return 성공시 1
	 */
	@Override
	public int modify(NoteVO noteVO) {
		return sqlSession.update("mappers.NoteDAO-mapper.modify", noteVO);
	}

	
	
	/* Delete */
	/**
	 * 노트 삭제
	 * @param notenum 삭제할 노트의 노트번호
	 * @return 성공시 1
	 */
	@Override
	public int delete(long notenum) {

		return sqlSession.delete("mappers.NoteDAO-mapper.delete", notenum);
	}




}
