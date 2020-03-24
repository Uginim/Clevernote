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
import com.uginim.clevernote.board.vo.CommentChangeHistoryVO;
import com.uginim.clevernote.board.vo.VoteVO;

@Service
public class CommentManager implements CommentService {
	private static final Logger logger = LoggerFactory.getLogger(CommentManager.class);
	
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
		
		// 4. 마지막 댓글 시간
		datas.put(KEY_LAST_COMMENT_TIME, lastTime);
		
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
		
		// 5. 마지막 댓글 시간
		datas.put(KEY_LAST_COMMENT_TIME, lastTime);
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
		long count = commentDAO.countRestChildComments(parentNum, lastTime);
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
	
	/**
	 * 댓글 하나의 사용자번호를 가져온다.
	 * @param commentNum 가져올 댓글 번호
	 * @return 댓글 의 사용자 번호 실패시 -1
	 */
	@Override
	public long getCommentUserNum(long commentNum) {
		long result = -1;
		BoardCommentVO comment =  commentDAO.selectOneComment(commentNum);
		try {
			result = comment.getUserNum();			
		}catch (Exception e) {
			e.printStackTrace();			
		}
		return result;
	}

	/**
	 * 댓글의 내용을 수정한다.
	 * @param commentNum 대상 댓글 번호
	 * @param Content 바뀔 내용
	 * @return 성공 시 1
	 */
	@Override
	public int updateContent(long commentNum, String content) {		
		return commentDAO.updateContent(commentNum, content);
	}
	
	/**
	 * 댓글을 삭제한다.
	 * @param commentNum 대상 댓글 번호
	 * @return 성공 시 1
	 */
	@Override
	public int deleteComment(long commentNum) {
		logger.info("deleteComment(long commentNum)");
		return commentDAO.delete(commentNum);
	}
	
	/**
	 * 루트 댓글을 가져온다
	 * @param commentNum 댓글 번호
	 * @return 루트댓글 객체
	 */
	@Override
	public BoardCommentVO getRootComment(long commentNum) {
		return commentDAO.selectOneRootComment(commentNum);
	}
	
	/**
	 * 자식 댓글을 가져온다
	 * @param commentNum 댓글 번호
	 * @return 자식댓글 객체
	 */
	@Override
	public BoardCommentVO getChildComment(long commentNum) {
		return commentDAO.selectOneChildComment(commentNum);
	}
	
	
	
	/**
	 * 사용자의 게시글 내 선호도이력 모두 가져온다.
	 * @param postNum 게시글 번호
	 * @param userNum 사용자 번호
	 * @return 투표 이력
	 */
	@Override
	public List<VoteVO> getAllMyPreferenceAtPost(long postNum, long userNum) {
		List<VoteVO> voteList= commentDAO.selectMyAllVotes(userNum, postNum);
		return voteList;
	}
	
	/**
	 * 변경 이력을 가져옴
	 * @param postNum 게시글 번호
	 * @param basetime 기준 시간
	 * @return 1.변경이력 2. 마지막 변경 시간
	 */
	@Override
	public Map<String, Object> getChangeHistory(long postNum, Date basetime) {
		Map<String, Object> datas = new HashMap<>();
		// 1. 변경 이력
		List<CommentChangeHistoryVO> historyList = commentDAO.selectAllHistory(postNum, basetime);
		datas.put(KEY_HISTORY_LIST, historyList);
		// 2. 마지막 변경 시간
		Date lastTime = null;
		if(historyList.size()>0) {
			CommentChangeHistoryVO history =  historyList.get(historyList.size()-1);
			lastTime = history.getCreatedAt();
		}else {
			lastTime = basetime;
		}
		datas.put(KEY_LAST_HITORY_TIME,lastTime);		
		return datas;
	}
	
	/**
	 * 투표하기 
	 * @param commentNum 댓글 번호
	 * @param userNum 사용자 번호
	 * @param type 투표 타입
	 * @return 성공 시 1
	 */
	@Override
	public int voteToComment(long commentNum, long userNum, char type) {
		logger.info("voteToComment(long commentNum, long userNum, char type)");
		VoteVO vote = new VoteVO();
		vote.setCommentNum(commentNum);
		vote.setUserNum(userNum);
		vote.setType(type);
//		return commentDAO.insertNewVote(vote);
		return commentDAO.mergeNewVote(vote);
	}
	
	/**
	 * 투표 취소
	 * @param voteNum 투표 번호
	 * @return 성공 시 1
	 */
	@Override
	public int cancelVote(long voteNum) {
		logger.info("cancelVote(long voteNum)");
		return commentDAO.deleteVote(voteNum);
	}
	
	/**
	 * 투표 취소
	 * @param commentNum 댓글 번호
	 * @param userNum 유저 번호
	 * @return
	 */
	@Override
	public int cancelVote(long commentNum, long userNum) {
		logger.info("cancelVote(long commentNum, long userNum)");
		return commentDAO.deleteVote(commentNum, userNum);
	}
	
	
	


}
