package com.Beendo.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

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
		entities = entityService.getAllEntities();
		return "EntityView";
/*		HttpServletRequest request =(HttpServletRequest)	FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String path = request.getContextPath();
//		String pathInfo = request.getPathInfo();
		
*/	}
	
//	public void createEntity()
//	{		
//		Map<String,Object> options = new HashMap<String, Object>();
//        options.put("resizable", false);
//        options.put("modal", true);
//        RequestContext.getCurrentInstance().openDialog("createEntity", options, null);
//	}
	
//	public void editEntity(CEntitiy _entity)
//	{		
//		Map<String,Object> options = new HashMap<String, Object>();
//        options.put("resizable", false);
//        options.put("modal", true);
//        RequestContext.getCurrentInstance().openDialog("editEntity", options, null);
//	}
	
	public void createPressed()
	{
		entities.add(entity);
		entityService.save(entity);
		showMessage("Entity has been saved");
		clearData();
		//RequestContext.getCurrentInstance().closeDialog("createEntity");
		//entity = new CEntitiy();
	}
	
	
	public void editPressed()
	{
		entityService.update(entity);
		//RequestContext.getCurrentInstance().closeDialog("editEntity");
		showMessage("Entity has been updated");
		clearData();
		
	}
	
	public void clearData()
	{
		entity = new CEntitiy();
	}
	
	public void showMessage(String msg) {
		
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Entity", msg);     
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }
	
}
