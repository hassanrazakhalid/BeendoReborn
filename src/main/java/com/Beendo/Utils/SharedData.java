package com.Beendo.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Permission;
import com.Beendo.Entities.User;


@Service
public class SharedData {

//    private static AtomicReference<SharedData> INSTANCE = new AtomicReference<SharedData>();

	@Autowired
	private AuthenticationManager authenticationManager;
	
	private static SharedData instance = null;
	
    private List<Practice> listPractise;
    
    private Authentication authentication;
    
//    private User currentUser;
    
    private void init(){
    	
    	listPractise = new ArrayList<Practice>();
    	
    }
    
    public SharedData() {
//        final SharedData previous = INSTANCE.getAndSet(this);
//        if(previous != null)
//            throw new IllegalStateException("Second singleton " + this + " created after " + previous);
        if(instance == null)
        	instance = this;
        this.init();
    }
    
    public String getFullName()
    {
    	String nam = getCurrentUser().getName() + " in " + getCurrentUser().getEntity().getName();
    	return nam;
    }

    public static SharedData getSharedInstace() {

    	/*if(instance == null)
    		instance = new SharedData();*/
        return instance;
    }
	
	public ApplicationContext getSpringContext(){
		
		 ApplicationContext ctx = WebApplicationContextUtils
               .getRequiredWebApplicationContext((ServletContext) FacesContext
                       .getCurrentInstance().getExternalContext()
                       .getContext());
		 return ctx;
	}
    
	// Security Code
	public String checkForSecurity(String userName, String password){
		
		String isOK = null;

		try {
            Authentication request = new UsernamePasswordAuthenticationToken(userName, password);
            authentication = authenticationManager.authenticate(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
           
            
            isOK = "correct";
        } catch (AuthenticationException e) {
            e.printStackTrace();
            isOK = "incorrect";
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Login Failed",
//    				"Wrong Credincials");
//    		RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
		
//	Collection roles =	 SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		 Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return isOK;
	}
	
	public boolean shouldReturnFullList(){
		
		boolean isOK = false;
		if(SharedData.getSharedInstace().getCurrentUser().getRoleName().equalsIgnoreCase(Role.ROOT_USER.toString()) ||
				SharedData.getSharedInstace().getCurrentUser().getRoleName().equalsIgnoreCase(Role.ROOT_ADMIN.toString())	)
		{
			isOK = true;
		}
		
		return isOK;
	}
	
	// Getters Annd Setters
	
	public String logoutLogic(){
		
		SecurityContextHolder.clearContext();
	
		HttpSession session =  session();
		if(session != null)
			session.invalidate();
		return "logout";
	}

	public static HttpSession session() {
	    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    return attr.getRequest().getSession(false); // true == allow create
	}
	
	public User getCurrentUser() {
		Object obj =	SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		if(obj instanceof User)
			return (User)obj;
		
//		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
//		try {
////			context.getRequestContextPath() + 
//			String s1 = context.getApplicationContextPath();
//			String s2 = context.getRequestContextPath();
//			String loginURL = context.getRequestContextPath() +"/Views/Unsecured/Login/index.xhtml";
//			context.redirect(loginURL);
//		} catch (IOException e) {
//			// TODO Auto-generated caRtch block
//			e.printStackTrace();
//		}
		
//	 	return null;
		
	}

	public static String getInString(List<Integer> ids) {

		String str = "in (";

		for (int i = 0; i < ids.size(); i++) {

			Integer integer = ids.get(i);
			str += " :arg" + i;

			if (i != (ids.size() - 1)) {
				str += ",";
			}
		}
		str += ")";
		return str;
	}
//	public void setCurrentUser(User currentUser) {
//		this.currentUser = currentUser;
//	}
}