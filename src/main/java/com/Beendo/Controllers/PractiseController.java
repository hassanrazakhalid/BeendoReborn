package com.Beendo.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;

import org.primefaces.context.RequestContext;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.CEntitiy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Controller
public class PractiseController {

	private List<CEntitiy> entityList;
	private String practiseName;
	
	public String viewPractise()
	{
		return "Practise/PractiseView?faces-redirect=true";
	}
	
	public void createPractise	()
	{
		Map<String,Object> options = new HashMap<String, Object>();
        options.put("resizable", false);
        options.put("modal", true);
        RequestContext.getCurrentInstance().openDialog("Practise/createPractise", options, null);
		//return "EntityView";
	}
	
	public void showMessage() {
		
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "What we do in life", "Echoes in eternity.");     
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }
	
}
