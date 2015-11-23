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

import com.Beendo.Controllers.ReportsController;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Provider;
import com.Beendo.Utils.SharedData;

@Controller
@FacesConverter("payerConverter")
public class PayerThemeConverter implements Converter {
	
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
            	
            	if(viewId.contains("ReportProvider.xhtml"))
            	{        	
	            	Payer entity = null;
	            	ReportsController userController = (ReportsController) getSpringContext().getBean("reportsController");
	            	entity = userController.hashPayer.get((Integer.parseInt(value)));
	            	
	            	return entity;
            	}
            	
            	else if(viewId.contains("ReportPractice.xhtml"))
            	{        	
	            	Payer entity = null;
	            	ReportsController userController = (ReportsController) getSpringContext().getBean("reportsController");
	            	entity = userController.hashPayer.get((Integer.parseInt(value)));
	            	
	            	return entity;
            	}
            	
            	else
            	{
	            	PayerService payerService = (PayerService) getSpringContext().getBean("payerService");         	
	            	Payer payer = payerService.getEntityById(Integer.parseInt(value));
	            	return payer;
            	}
            	
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
			Payer entity = (Payer)value;
			String index = String.valueOf(entity.getId()) ;
			return index;
		}
		else
			return null;
	}

}
