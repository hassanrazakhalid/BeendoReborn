package com.Beendo.Configuration;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;

@WebFilter(urlPatterns="/Views/Unsecured/Login/index.xhtml")
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
	 System.out.println("In login call \n"+ request.getServletContext().getContextPath());
		if (user != null)
		{
			httpResponse.sendRedirect(request.getServletContext().getContextPath() + "/Views/Secured/Dashboard/Dashboard.xhtml"); // Redirect to home page.
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

}
