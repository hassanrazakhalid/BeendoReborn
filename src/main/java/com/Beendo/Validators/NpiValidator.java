package com.Beendo.Validators;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.component.inputtext.InputText;
import org.primefaces.validate.ClientValidator;

@FacesValidator("npiValidator")
public class NpiValidator implements Validator, ClientValidator	 {

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		// TODO Auto-generated method stub
		
		InputText tf = (InputText)component;
		System.out.println(value);
		new ValidatorException(new FacesMessage("Custom Message"));
	}

	@Override
	public Map<String, Object> getMetadata() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValidatorId() {
		// TODO Auto-generated method stub
		return "com.npiValidator";
	}

}
