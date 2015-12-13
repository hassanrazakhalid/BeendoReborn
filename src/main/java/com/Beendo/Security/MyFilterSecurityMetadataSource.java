package com.Beendo.Security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.Beendo.Utils.Role;

public class MyFilterSecurityMetadataSource implements FilterInvocationSecurityMetadataSource,ApplicationContextAware	 {

	private ApplicationContext context;
	
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		FilterInvocation fi = (FilterInvocation) object;
        String url = fi.getRequestUrl();
        String httpMethod = fi.getRequest().getMethod();
        List<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();
        
        if(url.contains("Secured"))
        	attributes.addAll(popoulateConfigAttributes());
        // Lookup your database (or other source) using this information and populate the
        // list of attributes
 
        return attributes;
	}
	
	private List<ConfigAttribute> popoulateConfigAttributes(){
		
		List<ConfigAttribute> strList = new ArrayList<>();
		for (Role role : Role.values()) {
			  
			ConfigAttributeImpl config = (ConfigAttributeImpl)context.getBean("configAttributeImpl");
			config.setName(role.toString());
			strList.add(config);
			}
		return strList;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		this.context = applicationContext;
	}
}
