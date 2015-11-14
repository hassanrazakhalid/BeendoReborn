package com.Beendo.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Services.EntityService;
import com.Beendo.Services.UserService;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
public class EntityController {

	@Autowired
	private EntityService entityService;

	private CEntitiy entity = new CEntitiy();
	private List<CEntitiy> entities;
	
	public String viewEntity()
	{
		entities = entityService.findAll();
		//return "Entity/EntityView?faces-redirect=true";
		return "EntityView";
	}
	
	public void createEntity()
	{		
		Map<String,Object> options = new HashMap<String, Object>();
        options.put("resizable", false);
        options.put("modal", true);
        RequestContext.getCurrentInstance().openDialog("createEntity", options, null);
	}
	
//	public void editEntity(CEntitiy _entity)
//	{		
//		Map<String,Object> options = new HashMap<String, Object>();
//        options.put("resizable", false);
//        options.put("modal", true);
//        RequestContext.getCurrentInstance().openDialog("editEntity", options, null);
//	}
	
	public void createPressed()
	{
		entityService.save(entity);
		//showMessage("Entity has been saved");
		//RequestContext.getCurrentInstance().closeDialog("createEntity");
		//entity = new CEntitiy();
	}
	
	
	public void editPressed()
	{
		entityService.update(entity);
		//RequestContext.getCurrentInstance().closeDialog("editEntity");
		showMessage("Entity has been updated");
		entity = new CEntitiy();
	}
	
	
	public void showMessage(String msg) {
		
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Entity", msg);     
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }
	
}
