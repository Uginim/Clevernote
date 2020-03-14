package com.uginim.clevernote.board.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.uginim.clevernote.board.vo.BoardCommentVO;
import com.uginim.clevernote.board.vo.CommentChangeHistoryVO;
import com.uginim.clevernote.board.vo.VoteVO;

@Repository
public class CommentDAOImpl implements CommentDAO {
	
	@Inject
	SqlSessionTemplate session;
	
	/* Comment */
	/* Create */
	/**
	 * 댓글 추가
	 * @param comment 추가할 댓글 vo
	 * @return 성공 시 1
	 */
	@Override
	public int insert(BoardCommentVO comment) {		
		return session.insert("mappers.CommentDAO-mapper.insert",comment);
	}
	/* Read */	
	/**
	 * 게시글의 댓글들을 가져옴(답글 아님) 
	 * @param postNum 게시글 번호
	 * @param lastCommentTime 마지막 댓글의 시간
	 * @param rowNum 가져올 댓글 수
	 * @return 댓글 리스트
	 */
	@Override
	public List<BoardCommentVO> selectRootCommentList(long postNum, Date lastCommentTime, long countOfRow) {		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("postNum", postNum);
		map.put("lastCommentTime", lastCommentTime);
		map.put("countOfRow", countOfRow);		
		return session.selectList("mappers.CommentDAO-mapper.selectRootCommentList",map);
	}
	/**
	 * 게시글의 댓글들을 가져옴(답글 아님)
	 * @param postNum 게시글 번호
	 * @param baseValue 대상 기준 (이것보다 큰 값)
	 * @param rowNum 가져올 댓글 수
	 * @return 댓글 리스트
	 */
	@Override
	public List<BoardCommentVO> selectRootCommentList(long postNum, long baseValue, long countOfRow) {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("postNum", postNum);
		map.put("baseValue", baseValue);
		map.put("countOfRow", countOfRow);		
		return session.selectList("mappers.CommentDAO-mapper.selectRootCommentList",map);
	}
	/**
	 * 게시글의 댓글들을 가져옴(답글 아님,순서 포함) 
	 * @param postNum 게시글 번호
	 * @param lastCommentTime 마지막 댓글
	 * @param rowNum 가져올 댓글 수
	 * @param orderType 순서
	 * @return 댓글 리스트
	 */
	@Override
	public List<BoardCommentVO> selectRootCommentList(long postNum, long baseValue, long countOfRow, String order) {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("postNum", postNum);
		map.put("baseValue", baseValue);
		map.put("countOfRow", countOfRow);		
		map.put("order", order);		
		return session.selectList("mappers.CommentDAO-mapper.selectRootCommentList",map);
	}
	// 자식 댓글을 가져옴
	/**
	 * 자식 댓글을 가져옴
	 * @param parentNum 부모번호
	 * @param lastCommentTime 기준 시간(이시간 이후)
	 * @param countOfRow  요청
	 * @return 댓글 리스트 
	 */
	@Override
//	public List<BoardCommentVO> selectChildCommentList(long postNum, long parentNum, Date lastCommentTime, long countOfRow){
	public List<BoardCommentVO> selectChildCommentList(long parentNum, Date lastCommentTime, long countOfRow){
		Map<String,Object> map = new HashMap<String, Object>();
//		map.put("postNum", postNum);
		map.put("parentNum", parentNum);
		map.put("lastCommentTime", lastCommentTime);
		map.put("countOfRow", countOfRow);
		return session.selectList("mappers.CommentDAO-mapper.selectChildCommentList", map);
	}
	/**
	 * 자식 댓글을 가져옴
	 * @param parentNum 부모번호
	 * @param baseValue 대상 기준(이거보다 큰 값)
	 * @param rowNum  요청
	 * @return 댓글 리스트 
	 */
	@Override
//	public List<BoardCommentVO> selectChildCommentList(long postNum, long parentNum,long baseValue, long countOfRow){
	public List<BoardCommentVO> selectChildCommentList(long parentNum,long baseValue, long countOfRow){
		Map<String,Object> map = new HashMap<String, Object>();
//		map.put("postNum", postNum);
		map.put("parentNum", parentNum);
		map.put("baseValue", baseValue);
		map.put("countOfRow", countOfRow);
		return session.selectList("mappers.CommentDAO-mapper.selectChildCommentList", map);
	}
	
	// 남은 댓글 개수
	/**
	 * 남은 댓글 개수를 가져온다.
	 * @param postNum 게시글 번호
	 * @param lastCommentTime 마지막 게시글 시간
	 * @return 남은 댓글 개수
	 */
	@Override
	public long countRestComment(long postNum, Date lastCommentTime) {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("postNum", postNum);
		map.put("lastCommentTime", lastCommentTime);
		return session.selectOne("mappers.CommentDAO-mapper.countRestComment", map);
	}
	/**
	 * 남은 댓글 개수를 가져온다.
	 * @param postNum 게시글 번호
	 * @param lastCommentNum 마지막 게시글 시간
	 * @return 남은 댓글 개수
	 */
	@Override
	public long countRestComment(long postNum, long lastCommentNum) {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("postNum", postNum);
		map.put("lastCommentNum", lastCommentNum);
		return session.selectOne("mappers.CommentDAO-mapper.countRestComment", map);
	}

	/**
	 * 게시글의 모든 댓글 개수를 가져옴
	 * @param postNum 게시글 번호
	 * @return 총 댓글 개수
	 */
	@Override
	public long countTotalComments(long postNum) {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("postNum", postNum);
		return session.selectOne("mappers.CommentDAO-mapper.countTotalComments", map);
	}
	
	
	/**
	 * 남은 자식 댓글 개수를 가져온다.
	 * @param parentNum 부모댓글 번호
	 * @param lastCommentTime 마지막 자식댓글 시간
	 * @return 남은 댓글 개수
	 */
	@Override
	public long countRestChildComments(long parentNum, Date lastCommentTime) {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("parentNum", parentNum);
		map.put("lastCommentTime", lastCommentTime);
		return session.selectOne("mappers.CommentDAO-mapper.countRestChildComments", map);
	}
	
	
	// 내 모든 댓글
	/**
	 * 내 모든 댓글을 가져옴
	 * @param userNum 사용자 번호
	 * @return 댓글 리스트
	 */
	@Override
	public List<BoardCommentVO> selectAllMyComments(long userNum) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userNum",userNum);
		return session.selectList("mappers.CommentDAO-mapper.selectAllMyComments",map);
	}
	/**
	 * 게시글 내 모든 댓글을 가져옴81
	 * @param userNum 사용자 번호
	 * @param postNum 게시글 번호
	 * @return 댓글 리스트
	 */
	@Override
	public List<BoardCommentVO> selectAllMyComments(long userNum, long postNum) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userNum",userNum);
		map.put("postNum",postNum);
		return session.selectList("mappers.CommentDAO-mapper.selectAllMyComments",map);
	}
	
	// 이후 변경 내역 여부
	/**
	 * 댓글의 변경이력 불러오기 
	 * @param postNum 대상 게시글
	 * @param lastUpdatedTime 이후 변경내역 불러오기
	 * @return 변경내역 리스트
	 */
	@Override
	public List<CommentChangeHistoryVO> selectAllHistory(long postNum, Date lastUpdatedTime) {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("postNum", postNum);
		map.put("lastUpdatedTime", lastUpdatedTime);
		return session.selectList("mappers.CommentDAO-mapper.selectAllHistory", map);
	}
	// 댓글 하나 불러오기
	/**
	 * 댓글 가져오기
	 * @param commentNum 댓글번호
	 * @return 댓글 vo
	 */
	@Override
	public BoardCommentVO selectOneComment(long commentNum) {		
		return session.selectOne("mappers.CommentDAO-mapper.selectOneComment",commentNum);
	}

	
	/**
	 * 루트 댓글
	 * @param commentNum 댓글 번호
	 * @return 루트 댓글 VO
	 */
	@Override
	public BoardCommentVO selectOneRootComment(long commentNum) {
		return session.selectOne("mappers.CommentDAO-mapper.selectOneRootComment",commentNum);
	}
	
	
	/**
	 * 자식 댓글
	 * @param commentNum 댓글 번호
	 * @return 자식 댓글 VO
	 */
	@Override
	public BoardCommentVO selectOneChildComment(long commentNum) {
		return session.selectOne("mappers.CommentDAO-mapper.selectOneChildComment",commentNum);
	}
	
	/* Update */
	/**
	 * 댓글 수정하기
	 * @param comment 수정할 댓글 vo
	 * @return 성공 시 1
	 */
	@Override
	public int update(BoardCommentVO comment) {		
		return session.update("mappers.CommentDAO-mapper.update", comment);
	}

	/**
	 * 특정 댓글의 내용만 수정
	 * @param commentNum 댓글번호
	 * @param content 바꿀 내용
	 * @return 성공 시 1
	 */
	public int updateContent(long commentNum, String content) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.putIfAbsent("commentNum", commentNum);
		map.putIfAbsent("content", content);
		return session.update("mappers.CommentDAO-mapper.updatedContent",map);
	}
	
	
	/* Delete */	
	/**
	 * 댓글 삭제하기
	 * @param commentNum 댓글번호
	 * @return 성공 시 1
	 */
	@Override
	public int delete(long commentNum) {
		return session.update("mappers.CommentDAO-mapper.delete", commentNum);
	}

	/* Vote */
	/* Create */
	/**
	 * 새 vote 데이터를 넣는다. 
	 * @param vote
	 * @return 성공 시 1 중복 시 2
	 */
	@Override
	public int insertNewVote(VoteVO vote) {		
		return session.update("mappers.CommentDAO-mapper.insertNewVote",vote);
	}
	/**
	 * 새 vote 데이터를 넣는다. 중복되면 넣지 않는다.
	 * @param vote
	 * @return 성공 시 1 중복 시 2
	 */
	@Override
	public int mergeNewVote(VoteVO vote) {		
		return session.update("mappers.CommentDAO-mapper.mergeNewVote",vote);
	}

	/* Read */
	/**
	 * 유저의 vote 객체
	 * @param userNum 사용자 번호
	 * @return vote 객체 리스트
	 */
	@Override
	public List<VoteVO> selectMyAllVotes(long userNum) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userNum", userNum);
		return session.selectList("mappers.CommentDAO-mapper.selectMyAllVotes",map);
	}

	/**
	 * 유저가 포스트에서 투표한 vote 객체
	 * @param postNum 게시글 번호
	 * @param userNum 사용자 번호
	 * @return vote 객체 리스트
	 */
	@Override
	public List<VoteVO> selectMyAllVotes(long userNum, long postNum) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userNum", userNum);
		map.put("postNum", postNum);
		return session.selectList("mappers.CommentDAO-mapper.selectMyAllVotes",map);
	}

	/* Delete */	
	/**
	 * vote를 삭제한다.
	 * @param voteNum 삭제할 vote의 번호 
	 * @return 성공 시 1
	 */
	@Override
	public int deleteVote(long voteNum) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("voteNum", voteNum);		
		return session.delete("mappers.CommentDAO-mapper.deleteVote", map);
	}

	/**
	 * vote를 삭제
	 * @param commentNum 댓글 번호
	 * @param userNum 사용자 번호(투표자)
	 * @return 성공 시 1
	 */
	@Override
	public int deleteVote(long commentNum, long userNum) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("commentNum", commentNum);
		map.put("userNum", userNum);
		return session.delete("mappers.CommentDAO-mapper.deleteVote",map);
	}
	

}
