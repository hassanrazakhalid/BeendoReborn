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

import com.Beendo.Controllers.RootController;

import com.Beendo.Controllers.ProviderController;
import com.Beendo.Controllers.ReportsController;
import com.Beendo.Controllers.TransactionController;
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
            	
            	if(value.equals("Select Practice"))
            		return null;
            	
            	String viewId = context.getViewRoot().getViewId();
            	Practice practise = null;
            	Integer id = Integer.parseInt(value);
            	if(viewId.contains("ProviderView.xhtml"))
            	{
            		RootController userController = (RootController)getSpringContext().getBean("providerController");
            		practise = userController.getPractiseById(id);
            	}
            	
            	else if(viewId.contains("ViewTransaction.xhtml"))
            	{
            		RootController userController = (RootController)getSpringContext().getBean("transactionController");
            		practise = userController.getPractiseById(id);
            		/*TransactionController userController = (TransactionController) getSpringContext().getBean("transactionController");
            		practise = userController.get_hash().get((Integer.parseInt(value)));*/
            	}
            	
            	else if(viewId.contains("ReportPractice.xhtml"))
            	{
            		ReportsController userController = (ReportsController) getSpringContext().getBean("reportsController");
//            		practise = userController.getHashPractice().get((Integer.parseInt(value)));
            	}
            	
            	else if(viewId.contains("UserView.xhtml"))
            	{
            		RootController userController = (RootController) getSpringContext().getBean("userController");
            		practise = userController.getPractiseById(id) ;
            	}
            	
            	else
            	{
            		UserController userController = (UserController) getSpringContext().getBean("userController");
//            		practise = userController.getPractiseById(Integer.parseInt(value));
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
			String index = String.valueOf(entity.getId()) ;
			return index;
		}
		else
			return null;
	}

}
