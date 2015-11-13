package com.Beendo.Controllers;

import java.util.HashMap;
import java.util.Map;

import javax.faces.application.FacesMessage;

import org.primefaces.context.RequestContext;
import org.springframework.stereotype.Controller;

@Controller
public class EntityController {

	public String viewEntity()
	{
		return "EntityView";
	}
	
	public void createEntity()
	{
		Map<String,Object> options = new HashMap<String, Object>();
        options.put("resizable", false);
        options.put("modal", true);
        RequestContext.getCurrentInstance().openDialog("createEntity", options, null);
		//return "EntityView";
	}
	
	public void showMessage() {
		
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "What we do in life", "Echoes in eternity.");     
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }
	
}
