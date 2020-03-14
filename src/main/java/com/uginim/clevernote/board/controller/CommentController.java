package com.uginim.clevernote.board.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uginim.clevernote.board.service.CommentService;
import com.uginim.clevernote.board.vo.BoardCommentVO;
import com.uginim.clevernote.board.vo.VoteVO;
import com.uginim.clevernote.user.service.LoginManager;
import com.uginim.clevernote.user.vo.UserVO;

@RestController
@RequestMapping("/comment")
public class CommentController {
	private final static Logger logger = LoggerFactory.getLogger(CommentController.class);
	
	@Inject
	LoginManager loginManager;
	
	@Inject
	CommentService commentManager;
	
	
	
	
	// 처음 댓글을 호출 할 때 
	@GetMapping(path="/{postNum}",produces = "application/json" )
	public ResponseEntity<Map<String,Object>> initCommentView(
			@PathVariable(name = "postNum") long postNum,
			HttpSession session
		){
		ResponseEntity<Map<String,Object>> res =null;
		Map<String,Object> datas =null;
		// 세션 사용자가 누군지 가져오기
		// 게시글 번호가 뭔지
		try {
			if(loginManager.isLoggedIn(session)) {
				UserVO user= loginManager.getLoggedInUserInfo(session);		
				datas = commentManager.getInitData(postNum, user.getUserNum());
			}else {
				datas = commentManager.getInitData(postNum);
			}
			res = new ResponseEntity<Map<String,Object>> (datas,HttpStatus.OK);			
		}catch(Exception e) {
			e.printStackTrace();
			res = new ResponseEntity<Map<String,Object>> (datas,HttpStatus.BAD_REQUEST);
		}
		return res;
	}
	
	// 댓글 쓰기 
	@PostMapping(path="/{postNum}", produces="application/json")
	public ResponseEntity<Map<String,Object>> writeNewComment(
			@RequestParam(name = "content") 
				String content,
			@RequestParam(name = "targetCommentNum", required = false, defaultValue = "0")			
				long targetCommentNum,
			@RequestParam(name = "parentNum", required = false, defaultValue = "0")
				long parentNum,
			@RequestParam(name = "targetUsername", required = false)
				String targetUsername,
			@PathVariable(name = "postNum") long postNum,
			HttpSession session
			){
		logger.info("Write New Comment");
		logger.info("content: "+content);
		logger.info("targetCommentNum: "+targetCommentNum);
		ResponseEntity<Map<String,Object>> res = null;
		Map<String,Object> result = null;
		try {			
			if(loginManager.isLoggedIn(session)) {
				UserVO user = loginManager.getLoggedInUserInfo(session);
				if(targetUsername!=null && 
						parentNum !=0 && 
						targetCommentNum != 0) {
					result = commentManager.writeNewReplyComment(postNum, 
							user.getUserNum(), 
							parentNum, 
							targetCommentNum, 
							content, 
							user.getUsername(), 
							targetUsername);
				}else {
					result = commentManager.writeNewRootComment(postNum,user.getUserNum(), content, user.getUsername());				
				}			
				res = new ResponseEntity<Map<String,Object>> (result,HttpStatus.OK);
			}else {
				res = new ResponseEntity<Map<String,Object>> (result,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e) {
			e.printStackTrace();
			res = new ResponseEntity<Map<String,Object>> (result,HttpStatus.BAD_REQUEST);
		}
		return res;
	}
	
	// 다음 댓글 불러오기
	@GetMapping(path="/{postNum}/next", produces = "application/json" )
	public ResponseEntity<Map<String,Object>> getNextComments(
			@PathVariable(name = "postNum") long postNum,
			@RequestParam(name = "basetime") long time,
			HttpSession session
		){
		ResponseEntity<Map<String,Object>> res =null;
		Map<String,Object> datas = null;
		try {

			datas = commentManager.getNextComment(postNum, time);
			res = new ResponseEntity<Map<String,Object>> (datas,HttpStatus.OK);			
		}catch(Exception e) {
			e.printStackTrace();
			res = new ResponseEntity<Map<String,Object>> (datas,HttpStatus.BAD_REQUEST);
		}
		return res;
	}
	
	
	// 자식댓글들 가져오기 
	@GetMapping(path="/child", produces = "application/json" )
	public ResponseEntity<Map<String,Object>> getChildComments(			
			@RequestParam(name = "parentNum") long parentNum,
			@RequestParam(name = "basetime") long time,
			HttpSession session
		){
		ResponseEntity<Map<String,Object>> res =null;
		Map<String,Object> datas = null;
		try {
			datas = commentManager.getChildComments(parentNum, time);
			res = new ResponseEntity<Map<String,Object>> (datas,HttpStatus.OK);			
		}catch(Exception e) {
			e.printStackTrace();
			res = new ResponseEntity<Map<String,Object>> (datas,HttpStatus.BAD_REQUEST);
		}
		return res;
	}
	
	
	// 수정
	// 내용만 수정할 것
	@PutMapping(path="/{postNum}", produces="application/json" )
	public ResponseEntity<Map<String,Object>> updateComment(
			HttpSession session,
			@RequestParam(name = "commenttNum", defaultValue = "0") long commentNum,			
			@RequestParam(name = "content") String content
			){
		ResponseEntity<Map<String,Object>> res =null;
		Map<String,Object> datas = new HashMap<String,Object>();
		datas.put("state", 0);
		// 세션 확인
		if(loginManager.isLoggedIn(session)) {						
			// 댓글 주인과 수정하는 세션이 일치하는지 확인
			long userNum = loginManager.getLoggedInUserInfo(session).getUserNum();
			long ownerNum = commentManager.getCommentUserNum(commentNum);
			if(userNum == ownerNum ) { // 일치
				if(commentNum!=0) {
					try {			
						int state = commentManager.updateContent(commentNum, content);
						datas.put("state", state);
						return new ResponseEntity<Map<String,Object>> (datas,HttpStatus.OK);			
					}catch(Exception e) {
						e.printStackTrace();
						
					}						
				}
				return new ResponseEntity<Map<String,Object>> (datas,HttpStatus.BAD_REQUEST);
			}
		}	
		return new ResponseEntity<Map<String,Object>> (datas,HttpStatus.UNAUTHORIZED);
	}
	
	
	// 삭제
	// 세션확인
	@DeleteMapping(path="/{postNum}/{commentNum}",  produces="application/json")
	public ResponseEntity<Map<String,Object>> deleteComment(
			HttpSession session,
//			@RequestParam(name = "commentNum", defaultValue = "0") long commentNum
			@PathVariable(name = "commentNum" ) long commentNum
			){
		Map<String,Object> datas  = new HashMap<String,Object>();
		datas.put("state", 0);
		logger.info("commentNum:"+commentNum);
		// 세션 확인
		if(loginManager.isLoggedIn(session)) {						
			// 댓글 주인과 수정하는 세션이 일치하는지 확인
			long userNum = loginManager.getLoggedInUserInfo(session).getUserNum();
			long ownerNum = commentManager.getCommentUserNum(commentNum);
			if(userNum == ownerNum) { // 일치
				if(commentNum!=0) {
					try {			
						int state = commentManager.deleteComment(commentNum);
						datas.put("state", state);
						return new ResponseEntity<Map<String,Object>> (datas,HttpStatus.OK);						
					}catch(Exception e) {
						e.printStackTrace();						
					}	
				}
				return new ResponseEntity<Map<String,Object>> (datas,HttpStatus.BAD_REQUEST);
			}
		}		
		return new ResponseEntity<Map<String,Object>> (datas,HttpStatus.UNAUTHORIZED);
	}	
	
	
	// 댓글 가져오기 
	@GetMapping(path= {"/one/root/{commentNum}","/one/child/{commentNum}"},produces = "application/json")
	public ResponseEntity<Map<String,Object>> getOneComment(
		HttpServletRequest request, 
		@PathVariable(name = "commentNum" ) long commentNum
			){		
		ResponseEntity<Map<String,Object>> res =null;
		Map<String,Object> map = new HashMap<String, Object>();
//		long commentNum = 0;
//		try {
//			commentNum = Long.parseLong(commentNumStr);			
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
		String uri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String resourcePath = uri.substring(contextPath.length());		
		String key= resourcePath.split("/")[3];		
		BoardCommentVO comment =null;
		if(key.equals("root")) {
			comment = commentManager.getRootComment(commentNum);			
			map.put("comment",comment);
		}else if(key.equals("child")) {
			comment = commentManager.getChildComment(commentNum);
			map.put("comment",comment);
		}
		map.put("requestType",key);
		res = new ResponseEntity<Map<String,Object>> (map,HttpStatus.OK);
		return res;
	}
	
	
	// 투표
	// 이 글애서 내 투표 가져오기
	@GetMapping("/vote/{postNum}")
	public ResponseEntity<Map<String,Object>> getMyVoteInfo(
		@PathVariable(name = "postNum") long postNum,
		HttpSession session	
			){
//		res =null;
		// 로그인 여부 확인
		Map<String,Object> datas = new HashMap<String, Object>();
		if(loginManager.isLoggedIn(session)) {
			long userNum = loginManager.getLoggedInUserInfo(session).getUserNum();
			try {
				List<VoteVO> list = commentManager.getAllMyPreferenceAtPost(postNum, userNum);
				datas.put(commentManager.KEY_VOTE_LIST,list);
				return new ResponseEntity<Map<String,Object>> (datas,HttpStatus.OK);				
			}catch (Exception e) {
				e.printStackTrace();				
			}
		}		
		return new ResponseEntity<Map<String,Object>> (datas,HttpStatus.UNAUTHORIZED);
	}
	
	// 투표하기 
	@PostMapping(path="/vote") 
	public ResponseEntity<String> postVote(
			@RequestParam(name = "commentNum") long commentNum,
			@RequestParam(name = "type") char voteType,
			HttpSession session	
			){
		if(loginManager.isLoggedIn(session)) {
			long userNum = loginManager.getLoggedInUserInfo(session).getUserNum();
			try {				
				int state = commentManager.voteToComment(commentNum, userNum, voteType);
				if(state ==1) {
					return new ResponseEntity<String> ("success",HttpStatus.OK);					
				}
			}catch (Exception e) {
				e.printStackTrace();				
			}
		}		
		return new ResponseEntity<String> ("failed",HttpStatus.UNAUTHORIZED);
	}
	
	// 투표 취소
	@DeleteMapping(path="/vote")
	public ResponseEntity<String> cancelVote(
		@RequestParam(name = "commentNum") long commentNum,
		HttpSession session
			){
		if(loginManager.isLoggedIn(session)) {
			long userNum = loginManager.getLoggedInUserInfo(session).getUserNum();
			try {				
				int state = commentManager.cancelVote(commentNum, userNum);
				if(state ==1) {
					return new ResponseEntity<String> ("success",HttpStatus.OK);					
				}				
			}catch (Exception e) {
				e.printStackTrace();				
			}
		}		
		return new ResponseEntity<String> ("failed",HttpStatus.UNAUTHORIZED);
	}
	
	// 변경 이력 가져오기
	@GetMapping(path="/history/{postNum}")
	public ResponseEntity<Map<String,Object>> getCommentHistory(			
		@PathVariable(name = "postNum") long postNum,
		@RequestParam(name = "basetime") Date basetime
			){
		Map<String,Object> datas = null;
		ResponseEntity<Map<String,Object>> res=null;
		
		try {
			datas = commentManager.getChangeHistory(postNum, basetime);		
			res = new ResponseEntity<Map<String,Object>> (datas ,HttpStatus.OK);					
			
		}catch (Exception e) {
			e.printStackTrace();
			res = new ResponseEntity<Map<String,Object>> (datas,HttpStatus.UNAUTHORIZED);
		}				
		return res;
	}
	
}
