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
		List<Practice> list = SharedData.getSharedInstace().getListPractise();
		 if(strVal.length() < 0) {
			 
			 
			 
/*			 FacesContext context = FacesContext.getCurrentInstance();
*/
		        context.addMessage(component.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error: ", 
                        value + " is not a valid email;"));
			 
	            /*throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error", 
	                        value + " is not a valid email;"));*/
	        }
	}

}
