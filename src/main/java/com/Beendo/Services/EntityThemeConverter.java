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

import com.Beendo.Controllers.EntityController;
import com.Beendo.Controllers.ProviderController;
import com.Beendo.Controllers.RootController;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Utils.SharedData;

@Controller
@FacesConverter("entityConverter")
public class EntityThemeConverter implements Converter {
	
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
            	CEntitiy entity = null;
            	Integer id = Integer.parseInt(value);
            	if(viewId.contains("ProviderView"))
            	{
            		RootController entityService = (RootController) getSpringContext().getBean("providerController");
                	entity = entityService.getEntityById(id);
            	}
            	else if(viewId.contains("EntityView"))
            	{
            		RootController entityService = (RootController) getSpringContext().getBean("entityController");
                	entity = entityService.getEntityById(id);
            	}
            	else if(viewId.contains("PractiseView"))
            	{
            		RootController entityService = (RootController) getSpringContext().getBean("practiseController");
                	entity = entityService.getEntityById(id);
            	}
            	
            	else if(viewId.contains("UserView"))
            	{
            		RootController entityService = (RootController) getSpringContext().getBean("userController");
                	entity = entityService.getEntityById(id);
            	}
            	
            	
            	
//                EntityService service = (EntityService) context.getExternalContext().getApplicationMap().get("themeService");
//                return service.getThemes().get(Integer.parseInt(value));
//            	 entity = entityService.getEntityById();
            	return entity;
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
			CEntitiy entity = (CEntitiy)value;
			String index = String.valueOf(entity.getId()) ;
			return index;
		}
		else
			return null;
	}

}
