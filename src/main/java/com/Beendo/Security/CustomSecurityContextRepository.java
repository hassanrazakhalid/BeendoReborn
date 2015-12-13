package com.Beendo.Security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
// use less
public class CustomSecurityContextRepository implements SecurityContextRepository {

	@Override
	public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean containsContext(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return false;
	}

}
