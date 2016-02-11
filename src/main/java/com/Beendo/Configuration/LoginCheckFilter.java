package com.Beendo.Configuration;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;

//@WebFilter(urlPatterns="/Views/Unsecured/Login/index.xhtml")
@WebFilter(urlPatterns="/Views/*")
public class LoginCheckFilter implements Filter {
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
		System.out.println("init Filter");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		HttpServletResponse httpResponse = (HttpServletResponse) response;
	 SecurityContextHolderAwareRequestWrapper contextHolder = (SecurityContextHolderAwareRequestWrapper) request;
		
	 String user = contextHolder.getRemoteUser();
	 HttpServletRequest req = (HttpServletRequest)request;
//	 System.out.println("In login call \n"+ req.getServletContext().getContextPath());
	 System.out.println("In login call \n"+ req.getRequestURL());
	 String p = req.getPathInfo();
	 String I = req.getRequestURI();
	 String I1 = req.getContextPath();
	 String I2 = req.getServletPath();
	 
//	 String session = req.getSession(false).getId();
		if (user == null &&
				!I2.contains("Login"))
		{
			httpResponse.sendRedirect(req.getContextPath() + "/Views/Unsecured/Login/index.xhtml"); // Redirect to home page.

//			httpResponse.sendRedirect(req.getServletContext().getContextPath() + "/Views/Secured/Dashboard/Dashboard.xhtml"); // Redirect to home page.
		} 
			else
			{
            chain.doFilter(request, response); // User is not logged-in, so just continue request.
        }
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
		System.out.println("destroy Filter");
	}

	private boolean isUserLoggedIn(){
		
		Object obj =	SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(obj != null)
		{
			return true;
		}
		else
			
			return false;
	}

/*	@Override
	public void sessionCreated(HttpSessionEvent se) {
		// TODO Auto-generated method stub
		
		int i=0;
		i = 1;
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		// TODO Auto-generated method stub
		
		int i=0;
		i = 1;
	}*/

}
