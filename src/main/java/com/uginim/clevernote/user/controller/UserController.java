package com.uginim.clevernote.user.controller;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uginim.clevernote.user.service.UserService;
import com.uginim.clevernote.user.vo.UserVO;

@Controller
@RequestMapping("/user")
public class UserController {
	public static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Inject
	UserService userManager;
	
	@ModelAttribute
	public void init(Model model){
		model.addAttribute("newUserInfo", new UserVO());
	}
	

	
	// 회원가입 양식
	@GetMapping("/signup")
	public String getSignUpForm() {
		return "user/sign-up-form";
	}
	
	// 회원가입 처리
	@PostMapping("/signup")
	public String SignUp(
		@ModelAttribute(name = "newUserInfo") UserVO newUserInfo,
		Model model
			) {
		logger.info("newUserInfo:" +newUserInfo);
		int resultState = userManager.signUp(newUserInfo);		
		return "redirect:/";
	}
}
