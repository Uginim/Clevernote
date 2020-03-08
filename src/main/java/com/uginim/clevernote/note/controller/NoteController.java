package com.uginim.clevernote.note.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uginim.clevernote.note.service.NoteService;
import com.uginim.clevernote.note.vo.CategoryVO;
import com.uginim.clevernote.note.vo.NoteVO;
import com.uginim.clevernote.user.service.LoginService;
import com.uginim.clevernote.user.vo.UserVO;

@RequestMapping("/note")
@Controller
public class NoteController {

	public static final Logger logger = LoggerFactory.getLogger(NoteController.class);
	@Inject
	NoteService noteManager;
	
	
	
	//노트 메인페이지 받아오기
	@GetMapping()
	public String getNotePage() { 
		return "note/note-main";
	}
	
	// 카데고리 목록 가져오기
	@ResponseBody
	@GetMapping(path="/categories",produces = "application/json" )
	public List<CategoryVO> getCategories(			
			HttpSession session
			){

		List<CategoryVO> list = null;
		UserVO user = (UserVO) session.getAttribute(LoginService.KEY_LOGGED_IN_USERINFO);
		logger.info(""+user);
		list = noteManager.getAllCategories(user.getUserNum());
		logger.info(list.toString());
		
		return list;
	}
	
	// 카데고리내 노트 리스트 받아오기
	@ResponseBody
	@GetMapping(path="/notes/{categoryNum}",produces="application/json")
	public List<NoteVO> getNotes(
			@PathVariable(name = "categoryNum") long categoryNum
			){		
		List<NoteVO> list =null;
		list = noteManager.getAllNoteFromCategory(categoryNum, false);
		return list; 
	}
	
	// 카데고리 생성
	@ResponseBody
	@PostMapping(path="/category",consumes = "application/json",produces = "application/json")
	public Map<String,Object> createCategory(
			HttpServletResponse response,			
			@RequestBody Map<String,Object> body,
			HttpSession session
			){
		
		Map<String,Object> result = new HashMap<String, Object>();
//		logger.info("this is body: "+body.toString());
		UserVO user= (UserVO)session.getAttribute(LoginService.KEY_LOGGED_IN_USERINFO);
//		logger.info("state: "+response.getStatus());
		CategoryVO newCategory = noteManager.createCategory(user.getUserNum(), (String)body.get("title"));
		result.putIfAbsent("newCategory", newCategory);
		String stateMsg = null;
		if(newCategory!=null) {
			stateMsg= "OK";
		}else {
			stateMsg="Failed";
		}
		
		result.put("stateMsg", stateMsg);
		return result ; 
	}
}	
