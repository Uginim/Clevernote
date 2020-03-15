package com.uginim.clevernote.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
//	DAO 계층에서 SQLException 예외 발생시 처리
	@ExceptionHandler(DataAccessException.class)
	public ModelAndView handleSQLException(
			HttpServletRequest request, 
			Exception ex) {
			logger.error("Request: " + request.getRequestURL() + " called " + ex);

		ModelAndView mav = new ModelAndView();
		mav.addObject("url",request.getRequestURL());
		mav.addObject("exception",ex);
		mav.setViewName("exception/exception");
		return mav;
	}
	
}
