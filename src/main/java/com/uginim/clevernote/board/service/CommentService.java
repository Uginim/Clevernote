package com.uginim.clevernote.board.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.uginim.clevernote.board.vo.BoardCommentVO;
import com.uginim.clevernote.board.vo.VoteVO;

public interface CommentService {
	public static final String KEY_COMMENT_LIST = "commentList";
	public static final String KEY_VOTE_LIST = "voteList";
	public static final String KEY_TOTAL_COUNT = "totalCommentCount";
	public static final String KEY_REST_COMMENTS_COUNT = "restCommentCount"; 
	public static final String KEY_RESULT_STATE= "result_state";
	public static final String KEY_NEW_COMMENT_NUM= "resultState";
	public static final String KEY_LAST_COMMENT_TIME= "lastCommentTime";
	public static final String KEY_LAST_HITORY_TIME= "lastHistoryTime";
	public static final String KEY_PARENT_NUM = "parentKey";
	public static final String KEY_HISTORY_LIST = "historyList";
	
	
	/**
	 * 게시글 처음 열람시 필요한 데이터
	 * @param postNum 대상 게시글
	 * @return 1.루트 댓글 리스트 가져오기   2. 총 댓글 개수  3. 로드하지 않은 댓글 개수 
	 */
	Map<String,Object> getInitData(long postNum);
	/**
	 * 게시글 처음 열람시 필요한 데이터
	 * @param postNum 대상 게시글
	 * @param userNum 게시글 열람하는 사용자의 번호
	 * @return 1.루트 댓글 리스트 가져오기   2. 총 댓글 개수  3. 로드하지 않은 댓글 개수 4. 사용자의 투표리스트
	 */
	Map<String,Object> getInitData(long postNum,long userNum);
	
	/**
	 * 다음 댓글 가져옴
	 * @param postNum 
	 * @param baseTime
	 * @return 1.'새 루트댓글' 리스트 가져오기 2. 총 댓글 개수 3. 로드하지 않은 댓글 개수      
	 */
	Map<String,Object> getNextComment(long postNum,long baseTime);
	
	

	

	/**
	 * 자식댓글을 가져옴
	 * @param parentNum 부모 댓글
	 * @param baseTime 기준 시간
	 * @return 1. 자식댓글 리스트, 2. 남은 댓글 수
	 */
	Map<String,Object> getChildComments(long parentNum, long baseTime);
	
	
	
	/**
	 * 댓글 달기
	 * @param postNum 게시글 번호
	 * @param userNum 사용자 번호
	 * @param commentContent 내용
	 * @param username 사용자 이름
	 * @return (RESULT_STATE:성공 시 1, NEW_COMMENT_NUM:생성된 댓글 번호) 
	 */
	Map<String,Object> writeNewRootComment(long postNum, long userNum,String commentContent, String username);
	
	/**
	 * 댓글 달기
	 * @param postNum 게시글 번호
	 * @param userNum 사용자 번호
	 * @param parentNum 부모댓글
	 * @param targetNum 답글 대상 번호
	 * @param commentContent 내용
	 * @param username 사용자 이름
	 * @param targetUsername 답글 대상의 이름
	 * @return (RESULT_STATE:성공 시 1, NEW_COMMENT_NUM:생성된 댓글 번호)
	 */
	Map<String,Object> writeNewReplyComment(long postNum, long userNum,long parentNum,long targetNum, String commentContent, String username, String targetUsername);
	
	/**
	 * 댓글 하나의 사용자번호를 가져온다.
	 * @param commentNum 가져올 댓글 번호
	 * @return 댓글의 사용자 번호
	 */	
	long getCommentUserNum(long commentNum);
	
	/**
	 * 댓글의 내용을 수정한다.
	 * @param commentNum 대상 댓글 번호
	 * @param Content 바뀔 내용
	 * @return 성공 시 1
	 */
	int updateContent(long commentNum, String content);
	
	/**
	 * 댓글을 삭제한다.
	 * @param commentNum 대상 댓글 번호
	 * @return 성공 시 1
	 */
	int deleteComment(long commentNum);
	
	/**
	 * 루트 댓글을 가져온다
	 * @param commentNum 댓글 번호
	 * @return 루트댓글 객체
	 */
	BoardCommentVO getRootComment(long commentNum);
	
	/**
	 * 자식 댓글을 가져온다
	 * @param commentNum 댓글 번호
	 * @return 자식댓글 객체
	 */
	BoardCommentVO getChildComment(long commentNum);
	
	/**
	 * 사용자의 게시글 내 선호도이력 모두 가져온다.
	 * @param postNum 게시글 번호
	 * @param userNum 사용자 번호
	 * @return 투표 이력
	 */
	List<VoteVO> getAllMyPreferenceAtPost(long postNum, long userNum);
	
	
	/**
	 * 변경 이력을 가져옴
	 * @param postNum 게시글 번호
	 * @param basetime 기준 시간
	 * @return 1.변경이력 2. 마지막 변경 시간
	 */
	Map<String,Object> getChangeHistory(long postNum, Date basetime);

	/**
	 * 투표하기 
	 * @param commentNum 댓글 번호
	 * @param userNum 사용자 번호
	 * @param type 투표 타입
	 * @return 성공 시 1
	 */
	int voteToComment(long commentNum, long userNum, char type) ;
	
	/**
	 * 투표 취소
	 * @param voteNum 투표 번호
	 * @return 성공 시 1
	 */
	int cancelVote(long voteNum);
	
	/**
	 * 투표 취소
	 * @param commentNum 댓글 번호
	 * @param userNum 유저 번호
	 * @return
	 */
	int cancelVote(long commentNum, long userNum);
}
