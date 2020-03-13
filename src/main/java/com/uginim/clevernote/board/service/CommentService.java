package com.uginim.clevernote.board.service;

import java.util.Map;

public interface CommentService {
	// 
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
	
	
}
