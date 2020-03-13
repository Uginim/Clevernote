package com.uginim.clevernote.board.controller;

import java.util.Date;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uginim.clevernote.board.service.CommentService;
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
		// 세션 사용자가 누군지 가져오기

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
		// 세션 사용자가 누군지 가져오기
		try {
			datas = commentManager.getChildComments(parentNum, time);
			res = new ResponseEntity<Map<String,Object>> (datas,HttpStatus.OK);			
		}catch(Exception e) {
			e.printStackTrace();
			res = new ResponseEntity<Map<String,Object>> (datas,HttpStatus.BAD_REQUEST);
		}
		return res;
	}
	
	
	
}
