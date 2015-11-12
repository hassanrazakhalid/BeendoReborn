package com.Beendo.Controllers;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
public class LoginController {

	private String userName;
	private String password;
	
	public String getName(){
		
		return "Hello world123";
	}
	
	public String loginPressed(){
		
		
		return "Dashboard";
		//System.out.println("InDell");
		
	}
}
