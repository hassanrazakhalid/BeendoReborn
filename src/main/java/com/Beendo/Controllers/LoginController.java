package com.Beendo.Controllers;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.User;
//import com.Beendo.Security.UserSecurityService;
import com.Beendo.Services.UserService;
import com.Beendo.Utils.SharedData;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
public class LoginController {

	private String userName;
	private String password;

	@Autowired
	private SharedData sharedData;

	@Autowired
	private UserService userService;

	@Autowired
	private UserDetailsService securityService;

	public String getName() {

		return "Hello world123";
	}

	public String loginPressed() {

//		userName = "admin";
//		password = "password";

//		 userName = "pk1@hotmail.com";
//		 password ="12434";

		User user = userService.isUserValid(userName, password);

		SharedData sharedData = SharedData.getSharedInstace();

		sharedData.currentUser = user;
		return sharedData.checkForSecurity(userName, password);

		/*		List auth = (List) SecurityContextHolder.getContext().getAuthentication().getAuthorities();

		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		SecurityContextHolderAwareRequestWrapper sc = new SecurityContextHolderAwareRequestWrapper(req, "Admin");
		boolean res = sc.isUserInRole("Admin");
		return "Dashboard";*/
	}
	
	public String logout(){
		
		return SharedData.getSharedInstace().logoutLogic();
	}

	public void addUser() {

		User user = new User();
		user.setEmail("pk@hotmail.com");
		user.setPassword("12345");

		userService.save(user);

	}
}
