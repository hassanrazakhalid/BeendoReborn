package com.Beendo.Services;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.Beendo.Controllers.RootController;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Permission;
import com.Beendo.Utils.SharedData;

//@Controller
@FacesConverter("roleConverter")
public class RoleThemeConverter implements Converter {
	
	/*@Autowired
	private SharedData sharedData;*/
	
	private ApplicationContext getSpringContext(){
		
		 ApplicationContext ctx = WebApplicationContextUtils
                .getRequiredWebApplicationContext((ServletContext) FacesContext
                        .getCurrentInstance().getExternalContext()
                        .getContext());
		 return ctx;
	}
	private PermissionService getRoleService(){
		
		PermissionService roleService = (PermissionService) getSpringContext().getBean("roleService");
		return roleService;
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
		if(value != null && value.trim().length() > 0) {
            try {
            	
            	//Role_Permission role = getRoleService().getEntityById(Integer.parseInt(value));
            	RootController userController = (RootController)getSpringContext().getBean("userController");
//            	Permission role = userController.getRoleById(Integer.parseInt(value));
            	return null;
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
            }
        }
        else {
            return null;
        }
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		// TODO Auto-generated method stub
		if(value != null)
		{
			Permission entity = (Permission)value;
			String index = String.valueOf(entity.getId()) ;
			return index;
		}
		else
			return null;
	}

}
