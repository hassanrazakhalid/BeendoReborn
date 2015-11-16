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

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Utils.SharedData;

@Controller
@FacesConverter("entityConverter")
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
            	
            	
            	EntityService entityService = (EntityService) getSpringContext().getBean("entityService");
            	
//                EntityService service = (EntityService) context.getExternalContext().getApplicationMap().get("themeService");
//                return service.getThemes().get(Integer.parseInt(value));
            	CEntitiy entity = entityService.getEntityById(Integer.parseInt(value));
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
			EntityService entityService = (EntityService) getSpringContext().getBean("entityService");
			String index = String.valueOf(entity.getId()) ;
			return index;
		}
		else
			return null;
	}

}
