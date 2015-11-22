package com.Beendo.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.faces.application.FacesMessage;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.Beendo.Entities.Practice;
import com.Beendo.Entities.User;


@Service
public class SharedData {

//    private static AtomicReference<SharedData> INSTANCE = new AtomicReference<SharedData>();

	@Autowired
	private AuthenticationManager authenticationManager;
	
	private static SharedData instance = null;
	
    private List<Practice> listPractise;
    

    private Authentication authentication;
    
    public User currentUser;
    
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

    public static SharedData getSharedInstace() {

    	/*if(instance == null)
    		instance = new SharedData();*/
        return instance;
    }

    // Practise List Code
    
    public void addPactiseList(List<Practice> sender){
    	
    	listPractise.addAll(sender);
    }
    
    public void addPactise(Practice sender){
    	
    	listPractise.add(sender);
    }
    
    public void removePactise(Practice sender){
    	
    	listPractise.remove(sender);
    }

	public List<Practice> getListPractise() {
		// TODO Auto-generated method stub
		return listPractise;
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
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Login Failed",
    				"Wrong Credincials");
    		RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
		
        return isOK;
	}
	
	public String logoutLogic(){
		
		SecurityContextHolder.clearContext();
		return "logout";
	}
	
}