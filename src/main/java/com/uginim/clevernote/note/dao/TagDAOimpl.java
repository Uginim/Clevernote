package com.uginim.clevernote.note.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.uginim.clevernote.note.vo.NoteVO;
import com.uginim.clevernote.note.vo.TagVO;
import com.uginim.clevernote.note.vo.TaggingVO;

@Repository
public class TagDAOimpl implements TagDAO {

	@Inject
	SqlSessionTemplate sqlSession;

	
	/* Create */
	/**
	 * 새로운 태그 생성
	 * @param word
	 * @return
	 */
	@Override
	public int insertNewTag(String word) {
		TagVO tagVO = new TagVO(); 
		tagVO.setWord(word);
		return sqlSession.insert("mappers.Tagging-mapper.insertNewTag",tagVO );
	}
	
	/**
	 * 새로운 태그 생성(VO)사용
	 * @param newTagVO
	 * @return
	 */
	@Override
	public int insertNewTag(TagVO newTagVO) {
		return sqlSession.insert("mappers.Tagging-mapper.insertNewTag",newTagVO);
	}

	/**
	 * 특정 노트에대한 새로운 태깅 생성
	 * @param tagNum
	 * @param userNum
	 * @param noteNum
	 * @return
	 */
	@Override
	public int insertNewTagging(long tagNum, long noteNum) {
		TaggingVO tagging = new TaggingVO();
		NoteVO note = new NoteVO();
		note.setNoteNum(noteNum);
		tagging.setNote(note);
		TagVO tag = new TagVO();
		tag.setTagNum(tagNum);
		tagging.setTag(tag);		
		return sqlSession.insert("mappers.Tagging-mapper.insertNewTagging",tagging);
	}

	/**
	 * 특정 노트에대한 새로운 태깅 생성(VO사용)
	 * @param newTaggingVO
	 * @return
	 */
	@Override
	public int insertNewTagging(TaggingVO newTaggingVO) {
		return sqlSession.insert("mappers.Tagging-mapper.insertNewTagging",newTaggingVO);
	}

	/* Read */
	/**
	 * select tags by keyword
	 * @param keyword
	 * @return
	 */
	@Override
	public List<TagVO> selectTags(String keyword) {
		Map map = new HashMap<>();
		map.put("keyword", keyword);
		return sqlSession.selectList("mappers.Tagging-mapper.selectTags", map);
	}

	/**
	 * userNum이 일치하고 keyword에 검색되는 태그
	 * @param keyword
	 * @param userNum
	 * @return
	 */
	@Override
	public List<TagVO> selectTags(String keyword, long userNum) {
		Map map = new HashMap<>();
		map.put("keyword", keyword);
		map.put("userNum", userNum);
		return sqlSession.selectList("mappers.Tagging-mapper.selectTags", map);
	}
//
//	@Override
//	public List<TagVO> selectTags(long userNum) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	/**
	 * 태그 가져오기
	 * @param tagNum 태그번호
	 * @return
	 */
	@Override
	public List<TaggingVO> selectAllTaggings(long noteNum) {
		return sqlSession.selectList("mappers.Tagging-mapper.selectAllTaggings", noteNum);
	}
	
	
	/**
	 * 유저의 모든 태그 가져오기, user가 사용한 태그들 다 불러오기 
	 * @param userNum 유저 번호
	 * @return
	 */
	@Override
	public List<TaggingVO> selectAllUsersTaggings(long userNum) {
		return sqlSession.selectList("mappers.Tagging-mapper.selectAllUsersTaggings", userNum);
	}
	
	
	/**
	 * 노트에 태깅된 것들 가져오기
	 * @param noteNum 노트 번호
	 * @return
	 */
	@Override
	public TagVO selectOneTag(long tagNum) {
		Map map = new HashMap<>();
		map.put("tagNum", tagNum);
		return sqlSession.selectOne("mappers.Tagging-mapper.selectOneTag", tagNum);
	}

	/**
	 * 태그 가져오기
	 * @param word 태그  
	 * @return
	 */
	@Override
	public TagVO selectOneTag(String word) {
		Map map = new HashMap<>();
		map.put("word", word);
		return sqlSession.selectOne("mappers.Tagging-mapper.selectOneTag", word);
	}

	

	
	/* Delete */
	/**
	 * 노트의 태깅을 삭제
	 * @param noteNum 노트 번호
	 * @param tagNum 태그 번호
	 * @return
	 */
	@Override
	public int deleteTagging(long noteNum, long tagNum) {
		Map map = new HashMap<>();
		map.put("noteNum", noteNum);
		map.put("tagNum", tagNum);
		return sqlSession.delete("mappers.Tagging-mapper.deleteTagging", map);
	}

	/**
	 * 태그 삭제
	 * @param tagNum 태그 번호
	 * @return
	 */
	@Override
	public int deleteTag(long tagNum) {
		Map map = new HashMap<>();
		map.put("tagNum", tagNum);
		return sqlSession.delete("mappers.Tagging-mapper.deleteTag", map);
	}

	/**
	 * 태그 삭제(태그로 검색해서 삭제)
	 * @param word 태그 단어
	 * @return
	 */
	@Override
	public int deleteTag(String word) {
		Map map = new HashMap<>();
		map.put("word", word);
		return sqlSession.delete("mappers.Tagging-mapper.deleteTag", map);
	}

}
