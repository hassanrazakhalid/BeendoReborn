package com.Beendo.Controllers;

import javax.faces.application.FacesMessage;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.User;
import com.Beendo.Services.IUserService;
//import com.Beendo.Security.UserSecurityService;
import com.Beendo.Services.UserService;
import com.Beendo.Utils.SharedData;
import com.github.javaplugs.jsf.SpringScopeView;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
@SpringScopeView
public class LoginController implements DisposableBean {

	private String userName;
	private String password;

	@Autowired
	private SharedData sharedData;

	@Autowired
	private IUserService userService;

//	@Autowired
//	private UserDetailsService securityService;
	
	public void onLoad(){
		
		userName = "";
		password = "";
		
//		HttpSession session = SharedData.session();
//		int i =0;
	}
	
	public String loginPressed() {

//		sendMail("hassanrazakhalid89@gmail.com", "hassanrazakhalid@yahoo.com", "From Java", "My first mail");
		{
			SharedData sharedData = SharedData.getSharedInstace();

			
			
//			sharedData.setCurrentUser(user);
			String result = sharedData.checkForSecurity(userName, password);
			if(result.equalsIgnoreCase("incorrect"))
			{
				RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong username or password", null));
				return null;
			}
			return result;
		}
	

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

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
//		System.out.println("Login bean destroyed");
	}
}
