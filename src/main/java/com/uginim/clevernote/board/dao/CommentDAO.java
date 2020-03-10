package com.uginim.clevernote.board.dao;

import java.util.Date;
import java.util.List;

import com.uginim.clevernote.board.vo.BoardCommentVO;
import com.uginim.clevernote.board.vo.VoteVO;

public interface CommentDAO {
	/* Comment */
	/* Create */
	/**
	 * 댓글 추가
	 * @param comment 추가할 댓글 vo
	 * @return 성공 시 1
	 */
	int insert(BoardCommentVO comment);
	/* Read */	
	/**
	 * 게시글의 모든 댓글을 가져옴(대신 일부만) 
	 * @param postNum 게시글 번호
	 * @param lastCommentTime 마지막 댓글
	 * @param max 가져올 댓글 수 최대값
	 * @return 댓글 리스트
	 */
	List<BoardCommentVO> selectCommentList(long postNum, Date lastCommentTime, int max);
	/**
	 * 게시글의 모든 댓글을 가져옴(대신 일부만) 
	 * @param postNum 게시글 번호
	 * @param lastCommentTime 마지막 댓글
	 * @param max 가져올 댓글 수 최대값
	 * @param orderType 순서
	 * @return 댓글 리스트
	 */
	List<BoardCommentVO> selectCommentList(long postNum, Date lastCommentTime, int max, String orderType);
	/* Update */
	/**
	 * 댓글 수정하기
	 * @param comment 수정할 댓글 vo
	 * @return 성공 시 1
	 */
	int update(BoardCommentVO comment);
	/* Delete */	
	/**
	 * 댓글 삭제하기
	 * @param commentNum 댓글번호
	 * @return 성공 시 1
	 */
	int delete(long commentNum);
	
	/* Vote */
	/* Create */
	/**
	 * 새 vote 데이터를 넣는다. 중복되면 넣지 않는다.
	 * @param vote
	 * @return 성공 시 1 중복 시 2
	 */
	int insertNewVote(VoteVO vote);
	
	/* Read */
	/**
	 * 해당 유저가 좋아요 표시한 댓글들의 vote 객체
	 * @param userNum 사용자 번호
	 * @return vote 객체 리스트
	 */
	List<VoteVO> selectMyAllVotes(long userNum);
	/**
	 * 해당 포스트에서 좋아요 표시한 댓글들의 vote 객체
	 * @param postNum 게시글 번호
	 * @param userNum 사용자 번호
	 * @return vote 객체 리스트
	 */
	List<VoteVO> selectMyAllVotes(long postNum, long userNum);
	
	
	
	/* Delete */	
	/**
	 * vote를 삭제한다.
	 * @param voteNum 삭제할 vote의 번호 
	 * @return 성공 시 1
	 */
	int deleteVote(long voteNum);
	/**
	 * vote를 삭제
	 * @param commentNum 댓글 번호
	 * @param userNum 사용자 번호(투표자)
	 * @return 성공 시 1
	 */
	int deleteVote(long commentNum, long userNum);
}
