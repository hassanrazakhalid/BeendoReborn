package com.Beendo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.User;
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
	private UserService userService;
	
	public String getName(){
		
		return "Hello world123";
	}
	
	public String loginPressed(){
		
		User user = userService.isUserValid(userName, password);
		
		SharedData sharedData = SharedData.getSharedInstace();
		
		SharedData.getSharedInstace().currentUser = user;
		
		return "Dashboard";
		//System.out.println("InDell");
		
	}
	
	public void addUser(){
		
		User user = new User();
		user.setEmail("pk@hotmail.com");
		user.setPassword("12345");
		
		userService.save(user);
		
	}
}
