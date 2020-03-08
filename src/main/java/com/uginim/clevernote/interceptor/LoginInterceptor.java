package com.uginim.clevernote.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.uginim.clevernote.user.service.LoginService;
import com.uginim.clevernote.user.vo.UserVO;

public class LoginInterceptor implements HandlerInterceptor {
	public static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String uri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String reqResource = uri.substring(contextPath.length());
		
		logger.info("reqResource:"+reqResource);
		HttpSession session = request.getSession(false);
		if(redirectWhenNull(request,response,session)) {			
			return false;
		}
		UserVO user = (UserVO)session.getAttribute(LoginService.KEY_LOGGED_IN_USERINFO);
		logger.info(""+user);
		if(redirectWhenNull(request,response,user)) {		
			return false;			
		}		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
	private boolean redirectWhenNull(HttpServletRequest request,HttpServletResponse response, Object target) throws Exception{
		if(target==null) {	
			String uri = request.getRequestURI();
			String contextPath = request.getContextPath();
			String reqResource = uri.substring(contextPath.length());

			response.sendRedirect(contextPath+"/signin?next="+reqResource);
			return true;			
		}
		return false;
	}
	
}
