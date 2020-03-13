package com.uginim.clevernote.board.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.uginim.clevernote.board.dao.BoardDAO;
import com.uginim.clevernote.board.dao.CommentDAO;
import com.uginim.clevernote.board.vo.BoardCommentVO;
import com.uginim.clevernote.board.vo.BoardPostVO;
import com.uginim.clevernote.board.vo.VoteVO;

@Service
public class CommentManager implements CommentService {
	private static final Logger logger = LoggerFactory.getLogger(CommentManager.class);
	public static final String KEY_COMMENT_LIST = "commentList";
	public static final String KEY_VOTE_LIST = "voteList";
	public static final String KEY_TOTAL_COUNT = "totalCommentCount";
	public static final String KEY_REST_COMMENTS_COUNT = "restCommentCount"; 
	public static final String KEY_RESULT_STATE= "result_state";
	public static final String KEY_NEW_COMMENT_NUM= "resultState";
	public static final String KEY_LAST_COMMENT_TIME= "lastCommentTime";
	private static final String KEY_PARENT_NUM = "parentKey";
	private final static long MAX_REQEUST_ROW = 10;
	@Inject
	CommentDAO commentDAO;
	@Inject
	BoardDAO boardDAO;
	
	/**
	 * 게시글 처음 열람시 필요한 데이터
	 * @param postNum 대상 게시글
	 * @return 1.루트 댓글 리스트 가져오기   2. 총 댓글 개수  3. 로드하지 않은 댓글 개수 
	 */
	@Override
	public 	Map<String,Object> getInitData(long postNum){
		logger.info("getInitData(long postNum)");
		Map<String, Object> datas= new HashMap<>();
		BoardPostVO post =  boardDAO.selectOne(postNum);
		// 1.루트 댓글 리스트 가져오기 
		List<BoardCommentVO> commentList =  
		commentDAO.selectRootCommentList(postNum, post.getCreatedAt(), MAX_REQEUST_ROW);
		logger.info("KEY_COMMENT_LIST : "+ commentList.size());
		datas.putIfAbsent(KEY_COMMENT_LIST, commentList);
		// 2. 총 댓글 개수
		long count = commentDAO.countTotalComments(postNum);
		logger.info("KEY_TOTAL_COUNT : "+ count);
		datas.putIfAbsent(KEY_TOTAL_COUNT, count);
		// 3. 로드하지 않은 댓글 개수
		// 마지막 댓글 가져오기
		Date lastTime = null; 
		if(commentList.size()>0) {
			BoardCommentVO comment =  commentList.get(commentList.size()-1);
			lastTime = comment.getCreatedAt();
		}else {
			lastTime = post.getCreatedAt();
		}
		count = commentDAO.countRestComment(postNum,lastTime );			
		logger.info("KEY_REST_COMMENTS_COUNT : "+ count);
		datas.put(KEY_REST_COMMENTS_COUNT, count);
		return datas;
	}
	/**
	 * 게시글 처음 열람시 필요한 데이터
	 * @param postNum 대상 게시글
	 * @param userNum 게시글 열람하는 사용자의 번호
	 * @return Map객체 (1.루트 댓글 리스트 가져오기   2. 총 댓글 개수  3. 로드하지 않은 댓글 개수   4. 사용자의 투표리스트)
	 */
	@Override
	public Map<String, Object> getInitData(long postNum, long userNum) {
		logger.info("getInitData(long postNum, long userNum)");
		Map<String, Object> datas= new HashMap<>();
		BoardPostVO post =  boardDAO.selectOne(postNum);
		// 1.루트 댓글 리스트 가져오기 
		List<BoardCommentVO> commentList =  
		commentDAO.selectRootCommentList(postNum, post.getCreatedAt(), MAX_REQEUST_ROW);
		logger.info("KEY_COMMENT_LIST : "+ commentList.size());
		datas.putIfAbsent(KEY_COMMENT_LIST, commentList);
		// 2. 총 댓글 개수
		long count = commentDAO.countTotalComments(postNum);
		logger.info("KEY_TOTAL_COUNT : "+ count);
		datas.putIfAbsent(KEY_TOTAL_COUNT, count);
		// 3. 로드하지 않은 댓글 개수
		// 마지막 댓글 가져오기
		Date lastTime = null; 
		if(commentList.size()>0) {
			BoardCommentVO comment =  commentList.get(commentList.size()-1);
			lastTime = comment.getCreatedAt();
		}else {
			lastTime = post.getCreatedAt();
		}
		count = commentDAO.countRestComment(postNum,lastTime );			
		logger.info("KEY_REST_COMMENTS_COUNT : "+ count);
		datas.put(KEY_REST_COMMENTS_COUNT, count);
		
		// 4. 사용자의 투표리스트 
		List<VoteVO> voteList = commentDAO.selectMyAllVotes(userNum, postNum);
		logger.info("KEY_VOTE_LIST : "+ voteList.size());
		datas.putIfAbsent(KEY_VOTE_LIST, voteList);
		return datas;
	}
	
	/**
	 * 다음 댓글 가져옴
	 * @param postNum 
	 * @param baseTime
	 * @return 1.'새 루트댓글' 리스트 가져오기 2. 총 댓글 개수 3. 로드하지 않은 댓글 개수      
	 */
	@Override
	public Map<String, Object> getNextComment(long postNum, long baseTime) {
		Map<String, Object> datas= new HashMap<>();
		// 1.루트 댓글 리스트 가져오기 
		Date requestTime = new Date(baseTime);
		List<BoardCommentVO> commentList =  
		commentDAO.selectRootCommentList(postNum, requestTime, MAX_REQEUST_ROW);
		logger.info("KEY_COMMENT_LIST : "+ commentList.size());
		datas.putIfAbsent(KEY_COMMENT_LIST, commentList);
		// 2. 총 댓글 개수
		long count = commentDAO.countTotalComments(postNum);
		logger.info("KEY_TOTAL_COUNT : "+ count);
		datas.putIfAbsent(KEY_TOTAL_COUNT, count);
		// 3. 로드하지 않은 댓글 개수
		// 마지막 댓글 가져오기
		Date lastTime = null; 
		if(commentList.size()>0) {
			BoardCommentVO comment =  commentList.get(commentList.size()-1);
			lastTime = comment.getCreatedAt();
		}else {
			lastTime = requestTime;
		}
		count = commentDAO.countRestComment(postNum,lastTime );			
		logger.info("KEY_REST_COMMENTS_COUNT : "+ count);
		datas.put(KEY_REST_COMMENTS_COUNT, count);
		return datas;
	}
	
	
	/**
	 * 자식댓글을 가져옴
	 * @param parentNum 부모 댓글
	 * @param baseTime 기준 시간
	 * @return 1. 자식댓글 리스트, 2. 남은 댓글 수, 3. 마지막 시간
	 */
	@Override
	public Map<String,Object> getChildComments(long parentNum, long baseTime){
		Map<String, Object> datas= new HashMap<>();
		// 1.자식 댓글 리스트 가져오기
		Date requestTime = new Date(baseTime);
		List<BoardCommentVO> commentList =  
		commentDAO.selectChildCommentList(parentNum, requestTime, MAX_REQEUST_ROW);
		logger.info("KEY_COMMENT_LIST : "+ commentList.size());
		datas.putIfAbsent(KEY_COMMENT_LIST, commentList);
;
		// 2. 로드하지 않은 남은 댓글 개수
		// 마지막 댓글 가져오기
		Date lastTime = null; 
		if(commentList.size()>0) {
			BoardCommentVO comment =  commentList.get(commentList.size()-1);
			lastTime = comment.getCreatedAt();
		}else {
			lastTime = requestTime;
		}
		long count = commentDAO.countRestChildComments(parentNum, requestTime);
		logger.info("KEY_REST_COMMENTS_COUNT : "+ count);
		datas.put(KEY_REST_COMMENTS_COUNT, count);
		
		// 3. 부모 댓글 번호
		datas.put(KEY_PARENT_NUM, parentNum);
		return datas;
	}
	
	/**
	 * 
	 * @param postNum 게시글 번호
	 * @param userNum 사용자 번호
	 * @param commentContent 내용
	 * @param username 사용자 이름
	 * @return (RESULT_STATE:성공 시 1, NEW_COMMENT_NUM:생성된 댓글 번호)
	 */
	@Override
	public Map<String,Object> writeNewRootComment(long postNum, long userNum, String commentContent, String username) {
		
		Map<String,Object> result = new HashMap<>();
		BoardCommentVO comment = new BoardCommentVO();
		comment.setContent(commentContent);
		comment.setPostNum(postNum);
		comment.setUsername(username);
		comment.setUserNum(userNum);
		result.put(KEY_RESULT_STATE,commentDAO.insert(comment) );
		result.put(KEY_NEW_COMMENT_NUM,comment.getCommentNum());
		return result;
	}
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
	@Override
	public Map<String,Object> writeNewReplyComment(
			long postNum, 
			long userNum,
			long parentNum,
			long targetNum, 
			String commentContent, 
			String username, 
			String targetUsername) {
		
		Map<String,Object> result = new HashMap<>();
		BoardCommentVO comment = new BoardCommentVO();
		comment.setContent(commentContent);
		comment.setPostNum(postNum);
		comment.setUsername(username);
		comment.setUserNum(userNum);
		comment.setTargetUsername(targetUsername);
		comment.setTargetNum(targetNum);
		comment.setParentNum(parentNum);
		result.put(KEY_RESULT_STATE,commentDAO.insert(comment) );
		result.put(KEY_NEW_COMMENT_NUM,comment.getCommentNum());
		return result;
	}
	


}
