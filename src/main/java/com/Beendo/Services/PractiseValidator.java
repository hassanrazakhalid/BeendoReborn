package com.Beendo.Services;

import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.validate.ClientValidator;

import com.Beendo.Controllers.RootController;
import com.Beendo.Entities.Practice;
import com.Beendo.Utils.SharedData;

@FacesValidator("custom.PractiseValidator")
public class PractiseValidator implements Validator, ClientValidator {

	@Override
	public Map<String, Object> getMetadata() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValidatorId() {
		// TODO Auto-generated method stub
		return "custom.PractiseValidator";
	}

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		// TODO Auto-generated method stub
		if(value == null)
			return;
		String strVal = (String)value;
		String viewName = context.getViewRoot().getViewId();
//		Integer id = Integer.parseInt(strVal);
		List<Practice> list = null;
		if(viewName.contains("PractiseView"))
		{
			RootController rootController = (RootController)SharedData.getSharedInstace().getSpringContext().getBean("practiseController");
			list = rootController.getAllHashTwo();
		}
		 
		 if(strVal.length() > 0) {
			 
			List<Practice> result = null;/// PractiseService.isNameExist(list, strVal);
			 
/*			 FacesContext context = FacesContext.getCurrentInstance();
*/
			if(result.size() > 0)
			{
				context.addMessage(component.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: ", 
                        value + " already exist"));
				context.validationFailed();
			}
			else
			{
				context.addMessage(component.getClientId(), new FacesMessage(FacesMessage.SEVERITY_INFO, "Correct", "Valid Name"));
			} 
	            /*throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error", 
	                        value + " is not a valid email;"));*/
	        }
	}
}
