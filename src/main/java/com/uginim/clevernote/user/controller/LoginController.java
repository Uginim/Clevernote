package com.uginim.clevernote.user.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uginim.clevernote.user.service.LoginService;
import com.uginim.clevernote.user.vo.UserVO;

@Controller
@RequestMapping
public class LoginController {
	public final static Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Inject
	LoginService loginManager;
	
	// 회원 로그인	
	@GetMapping("/signin")
	public String getLoginForm(
			@RequestParam(name =  "next", required = false) String next,
			Model model
			) {
		model.addAttribute("next",next);
		return "user/sign-in-form";
	}
	
	@PostMapping("/signin")
	public String login(
			@ModelAttribute UserVO user,
			@RequestParam(name = "next", required = false) String next,
			Model model,
			HttpSession session			
			) {
		logger.info("session for signing in :"+ session.toString());
		if(loginManager.isLoggedIn(session)) {
			logger.info("session이 이미 존재");
			return "redirect:"+ ((next!=null)?next : "/");
		}else {
			int state = loginManager.signIn(user.getEmail(), user.getPw(), session);
			logger.info("login state :"+state);
			if(state == LoginService.LOGIN_SUCCESS) {				
				return "redirect:"+ ((next!=null)?next : "/");
			}else {
				model.addAttribute("errMsg", "비밀번호가 맞지 않습니다.");
				return "user/sign-in-form";
			}			
		}	
	}
	
	@RequestMapping("/signout")
	public String signOut(HttpSession session) {
		loginManager.signOut(session);
		return "redirect:/";
	}
}
