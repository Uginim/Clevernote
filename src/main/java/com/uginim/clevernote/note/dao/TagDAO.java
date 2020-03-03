package com.uginim.clevernote.note.dao;

import java.util.List;

import com.uginim.clevernote.note.vo.TagVO;
import com.uginim.clevernote.note.vo.TaggingVO;

public interface TagDAO {
	/* Create */
	/**
	 * 새로운 태그 생성
	 * @param word
	 * @return
	 */
	int insertNewTag (String word);
	/**
	 * 새로운 태그 생성(VO)사용
	 * @param newTagVO
	 * @return
	 */
	int insertNewTag (TagVO newTagVO);
	/**
	 * 특정 노트에대한 새로운 태깅 생성
	 * @param tagNum
	 * @param userNum
	 * @param noteNum
	 * @return
	 */
	int insertNewTagging(long tagNum, long noteNum);
	/**
	 * 특정 노트에대한 새로운 태깅 생성(VO사용)
	 * @param newTaggingVO
	 * @return
	 */
	int insertNewTagging(TaggingVO newTaggingVO);
	
	/* Read */
	/**
	 * select tags by keyword
	 * @param keyword
	 * @return
	 */
	List<TagVO> selectTags(String keyword);
	/**
	 * userNum이 일치하고 keyword에 검색되는 태그
	 * @param keyword
	 * @param userNum
	 * @return
	 */
	List<TagVO> selectTags(String keyword,long userNum);
	/**
	 * user가 사용한 태그들 다 불러오기 //user의 태그들 불러오기
	 * @param userNum 유저 번호
	 * @return
	 */
//	List<TagVO> selectTags(long userNum);  
	 	
	/**
	 * 태그 가져오기
	 * @param tagNum 태그번호
	 * @return
	 */
	TagVO selectOneTag(long tagNum);
	/**
	 * 태그 가져오기
	 * @param word 태그  
	 * @return
	 */
	TagVO selectOneTag(String word);
	/**
	 * 유저의 모든 태그 가져오기, user가 사용한 태그들 다 불러오기 
	 * @param userNum 유저 번호
	 * @return
	 */
	List<TaggingVO> selectAllUsersTaggings(long userNum);
	/**
	 * 노트에 태깅된 것들 가져오기
	 * @param noteNum 노트 번호
	 * @return
	 */
	List<TaggingVO> selectAllTaggings(long noteNum);
	
	/* Update */
	
	
	/* Delete */
	/**
	 * 노트의 태깅을 삭제
	 * @param noteNum 노트 번호
	 * @param tagNum 태그 번호
	 * @return
	 */
	int deleteTagging(long noteNum,long tagNum);
	/**
	 * 태그 삭제
	 * @param tagNum 태그 번호
	 * @return
	 */
	int deleteTag(long tagNum);
	/**
	 * 태그 삭제(태그로 검색해서 삭제)
	 * @param word 태그 단어
	 * @return
	 */
	int deleteTag(String word);
}
