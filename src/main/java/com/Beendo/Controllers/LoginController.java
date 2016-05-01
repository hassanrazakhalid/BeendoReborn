package com.Beendo.Controllers;

import java.io.Serializable;

import javax.faces.application.FacesMessage;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.User;
import com.Beendo.Services.IUserService;
import com.Beendo.Utils.SharedData;
import com.github.javaplugs.jsf.SpringScopeView;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
@SpringScopeView
public class LoginController implements DisposableBean,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4982548254116513730L;

	private final Logger slf4Logger = LoggerFactory.getLogger(this.getClass());
	
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

			
//			sharedData.sendMail("Hassan.raza@sypore.com", "Hassan.raza@sypore.com", "sample mail", "salam");
			
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

		userService.saveOrUpdate(user);

	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
//		System.out.println("Login bean destroyed");
		slf4Logger.debug("Login bean destroyed");
	}
}
