package com.Beendo.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.Beendo.Entities.Document;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.User;
import com.Beendo.Services.IProviderService;


@Service
public class SharedData implements ApplicationContextAware {

//    private static AtomicReference<SharedData> INSTANCE = new AtomicReference<SharedData>();

	@Autowired
	private AuthenticationManager authenticationManager;
	private final Logger slf4Logger = LoggerFactory.getLogger(this.getClass());
	private static SharedData instance = null;
	
    
    private Timer timer;
    private Authentication authentication;
    private JavaMailSenderImpl mail;
    
	@Autowired
	private IProviderService providerService;
//    private User currentUser;
    
    private void init(){
    	
    	
//    	addTimerForDocumentExpire();
    	
    }
    
    private void addTimerForDocumentExpire(){
    	
    	this.timer = new Timer();
    	Properties map = null;
    	try {
			map = readFile("EmailSettings.properties");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	if(map != null)
    	{
    		int min = Integer.valueOf(map.getProperty("timerDuration")) ;
           	int totalDelay = min * 1000 * 60;
        	timer.schedule(new TimerTask() {
    			
    			@Override
    			public void run() {
    				// TODO Auto-generated method stub
    				
    				List<Document> docList = providerService.getDocumentByEmail();
    				
    				for (Document document : docList) {
						
    					//send emails to these documents , they are already filtered
    					String msg = document.getOrignalName() + "is going to expired on" + document.getExpireDate();
    					sendMail("Partracker", "Hassan.raza@sypore.com", "Document Reminder", msg);
    					document.setReminderStatus(1);
					}
    				
    				if(!docList.isEmpty())
    					providerService.updateDocuments(docList);
    				
    				System.out.println(new Date());
    				
    			}
    		}, 0,totalDelay);
        	
    	}

    }
    
	public Properties readFile(String propFileName) throws IOException {
		 
		InputStream inputStream = null;
		Properties prop = null;
		try {
			 prop = new Properties();
 
			 inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			} 
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		return prop;
	}
    
	public void sendMail(String from, String to, String subject, String msg) {
		// creating message
		
		try {

			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(from);
			message.setTo(to);
			message.setSubject(subject);
			message.setText(msg);
			// sending message
			mail.send(message);

		} catch (Exception e) {
			
			slf4Logger.debug(e.toString());
			// TODO: handle exception
		}
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
	@Transactional(readOnly=true)
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
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub

		mail = (JavaMailSenderImpl) applicationContext.getBean("mailSender");
	}
//	public void setCurrentUser(User currentUser) {
//		this.currentUser = currentUser;
//	}
}