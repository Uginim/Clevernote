package com.uginim.clevernote.board.dao;

import java.util.Date;
import java.util.List;

import com.uginim.clevernote.board.vo.BoardCommentVO;
import com.uginim.clevernote.board.vo.CommentChangeHistoryVO;
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
	 * 게시글의 댓글들을 가져옴(답글 아님) 
	 * @param postNum 게시글 번호
	 * @param lastCommentTime 마지막 댓글의 시간
	 * @param countOfRow 가져올 댓글 수
	 * @return 댓글 리스트
	 */
	List<BoardCommentVO> selectRootCommentList(long postNum, Date lastCommentTime, long countOfRow);
	/**
	 * 게시글의 댓글들을 가져옴(답글 아님)
	 * @param postNum 게시글 번호
	 * @param baseValue 대상 기준 (이것보다 큰 값)
	 * @param countOfRow 가져올 댓글 수
	 * @return 댓글 리스트
	 */
	List<BoardCommentVO> selectRootCommentList(long postNum, long baseValue, long countOfRow);
	/**
	 * 게시글의 댓글들을 가져옴(답글 아님,순서 포함) 
	 * @param postNum 게시글 번호
	 * @param lastCommentTime 기준 시간(이시간 이후)
	 * @param countOfRow 가져올 댓글 수
	 * @param orderType 순서
	 * @return 댓글 리스트
	 */
	List<BoardCommentVO> selectRootCommentList(long postNum, long baseValue, long countOfRow, String order);
	
	// 자식 댓글을 가져옴
	/**
	 * 자식 댓글을 가져옴
	 * @param parentNum 부모번호
	 * @param lastCommentTime 기준 시간(이시간 이후)
	 * @param countOfRow  요청
	 * @return 댓글 리스트 
	 */
//	List<BoardCommentVO> selectChildCommentList(long postNum, long parentNum, Date lastCommentTime, long countOfRow);
	List<BoardCommentVO> selectChildCommentList(long parentNum, Date lastCommentTime, long countOfRow);
	/**
	 * 자식 댓글을 가져옴
	 * @param parentNum 부모번호
	 * @param baseValue 대상 기준(이거보다 큰 값)
	 * @param countOfRow  요청
	 * @return 댓글 리스트 
	 */
//	List<BoardCommentVO> selectChildCommentList(long postNum, long parentNum,long baseValue, long countOfRow);
	List<BoardCommentVO> selectChildCommentList(long parentNum,long baseValue, long countOfRow);
	
	
	
	// 내 모든 댓글
	/**
	 * 내 모든 댓글을 가져옴
	 * @param userNum 사용자 번호
	 * @return 댓글 리스트
	 */
	List<BoardCommentVO> selectAllMyComments(long userNum);
	/**
	 * 게시글 내 나의 모든 댓글을 가져옴
	 * @param userNum 사용자 번호
	 * @param postNum 게시글 번호
	 * @return 댓글 리스트
	 */
	List<BoardCommentVO> selectAllMyComments(long userNum, long postNum);
	
	// 남은 댓글 개수
	/**
	 * 남은 댓글 개수를 가져온다.
	 * @param postNum 게시글 번호
	 * @param lastCommentTime 마지막 댓글 시간
	 * @return 남은 댓글 개수
	 */
	long countRestComment(long postNum, Date lastCommentTime);
	/**
	 * 남은 댓글 개수를 가져온다.
	 * @param postNum 게시글 번호
	 * @param lastCommentNum 마지막 댓글 시간
	 * @return 남은 댓글 개수
	 */
	long countRestComment(long postNum, long lastCommentNum);
	/**
	 * 게시글의 모든 댓글 개수를 가져옴
	 * @param postNum 게시글 번호
	 * @return 총 댓글 개수
	 */
	long countTotalComments(long postNum);
	
	/**
	 * 남은 자식 댓글 개수를 가져온다.
	 * @param parentNum 부모댓글 번호
	 * @param lastCommentTime 마지막 자식댓글 시간
	 * @return 남은 댓글 개수
	 */
	long countRestChildComments(long parentNum, Date lastCommentTime);
	
	// 이후 변경 내역 여부
	/**
	 * 댓글의 변경이력 불러오기 
	 * @param postNum 대상 게시글
	 * @param lastUpdatedTime 이후 변경내역 불러오기
	 * @return 변경내역 리스트
	 */
	List<CommentChangeHistoryVO> selectAllHistory(long postNum, Date lastUpdatedTime);
	// 댓글 하나 불러오기
	/**
	 * 댓글 가져오기
	 * @param commentNum 댓글번호
	 * @return 댓글 vo
	 */
	BoardCommentVO selectOneComment(long commentNum);
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
	int mergeNewVote(VoteVO vote);
	
	/* Read */
	/**
	 * 유저의 vote 객체
	 * @param userNum 사용자 번호
	 * @return vote 객체 리스트
	 */
	List<VoteVO> selectMyAllVotes(long userNum);
	/**
	 * 유저가 포스트에서 투표한 vote 객체
	 * @param postNum 게시글 번호
	 * @param userNum 사용자 번호
	 * @return vote 객체 리스트
	 */
	List<VoteVO> selectMyAllVotes(long userNum, long postNum);
	
	
	
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
