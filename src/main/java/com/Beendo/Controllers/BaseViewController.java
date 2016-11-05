package com.Beendo.Controllers;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;

import org.primefaces.context.RequestContext;

public class BaseViewController {

	
	public void showMessage(Severity severity, String title,String msg) {
		FacesMessage message = new FacesMessage(severity, title, msg);
		RequestContext.getCurrentInstance().showMessageInDialog(message);
	}
}
