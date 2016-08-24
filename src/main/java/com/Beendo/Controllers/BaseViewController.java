package com.Beendo.Controllers;

import javax.faces.application.FacesMessage;

import org.primefaces.context.RequestContext;

public class BaseViewController {

	
	public void showMessage(String msg) {

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Provider", msg);
		RequestContext.getCurrentInstance().showMessageInDialog(message);
	}
}
