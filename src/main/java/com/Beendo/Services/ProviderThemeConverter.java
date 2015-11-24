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

import com.Beendo.Controllers.ProviderController;
import com.Beendo.Controllers.ReportsController;
import com.Beendo.Controllers.RootController;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Provider;
import com.Beendo.Utils.SharedData;

@Controller
@FacesConverter("providerConverter")
public class ProviderThemeConverter implements Converter {
	
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
            	Integer id = Integer.parseInt(value);
            	Provider provider = null;
            	
            	if(viewId.contains("ReportProvider.xhtml"))
            	{        		
            		RootController userController = (RootController) getSpringContext().getBean("reportsController");
	            	provider = userController.getProviderById(id);	            	
            	}
            	else if(viewId.contains("ProviderView.xhtml"))
            	{        		
	            	RootController userController = (RootController) getSpringContext().getBean("providerController");
	            	provider = userController.getProviderById(id);	            	
            	}
            	
            	return provider;
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
			Provider entity = (Provider)value;
			String index = String.valueOf(entity.getId()) ;
			return index;
		}
		else
			return null;
	}

}
