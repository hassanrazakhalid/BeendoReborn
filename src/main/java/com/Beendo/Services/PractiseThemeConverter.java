package com.Beendo.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.Beendo.Controllers.ProviderController;
import com.Beendo.Controllers.UserController;
import com.Beendo.Entities.Practice;

@FacesConverter("practiseConverter")
public class PractiseThemeConverter implements Converter {
	
	/*@Autowired
	private SharedData sharedData;*/
	
	private ApplicationContext getSpringContext(){
		
		 ApplicationContext ctx = WebApplicationContextUtils
                .getRequiredWebApplicationContext((ServletContext) FacesContext
                        .getCurrentInstance().getExternalContext()
                        .getContext());
		 return ctx;
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
		if(value != null && value.trim().length() > 0) {
            try {
            	
            	String viewId = context.getViewRoot().getViewId();
            	Practice practise = null;
            	
            	if(viewId.contains("ProviderView.xhtml"))
            	{
            		ProviderController userController = (ProviderController) getSpringContext().getBean("providerController");
            	practise = userController.hashPractise.get((Integer.parseInt(value)));
            	}
            	else
            	{
            		UserController userController = (UserController) getSpringContext().getBean("userController");
            	}
//                EntityService service = (EntityService) context.getExternalContext().getApplicationMap().get("themeService");
//                return service.getThemes().get(Integer.parseInt(value));
//            	Practice entity = userController.getPractiseById((Integer.parseInt(value)));
            	 
            	return practise;
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
			Practice entity = (Practice)value;
//			String viewId = context.getViewRoot().getViewId();
//			
//           	if(viewId.compareToIgnoreCase("ProviderView.xhtml") == 0)
//        	{
//        		ProviderController userController = (ProviderController) getSpringContext().getBean("providerController");
//        	practise = userController.hashPractise.get((Integer.parseInt(value)));
//        	}
//        	else
//        	{
//        		UserController userController = (UserController) getSpringContext().getBean("userController");
//        	}
			
//			hashPractise.put(entity.getId(), entity);
			String index = String.valueOf(entity.getId()) ;
			return index;
		}
		else
			return null;
	}

}
