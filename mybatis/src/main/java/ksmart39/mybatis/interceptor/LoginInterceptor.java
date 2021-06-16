package ksmart39.mybatis.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoginInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		HttpSession session = request.getSession();
		String sessionId = (String) session.getAttribute("SID");
		String sessionLevel = (String) session.getAttribute("SLEVEL");
		String requestUri = request.getRequestURI();
		

		if(sessionId == null) {
			response.sendRedirect("/login");
			return false;
		}else {
			if("2".equals(sessionLevel)) {
				if(requestUri.indexOf("memberList") > -1 || 
				   requestUri.indexOf("addMember")  > -1	) {
					
					response.sendRedirect("/");
					
					return false;
				}
			}
			
			if("3".equals(sessionLevel)) {
				
			}
			
		}
		
		
		/**
		 * 	상품등록, 상품리스트 까지 만들고 나서 합시다. 
		 * 	else {
			//1. 세션 회원레벨 (1,2,3)
			
			//2. request.getRequestURI() == "memberList" 관리자 제외한 나머지 등급은 response.sendRedirect("/login");
			
			
			}
		 */
		
		
		
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
