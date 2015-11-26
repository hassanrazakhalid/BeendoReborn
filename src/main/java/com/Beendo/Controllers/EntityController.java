package com.Beendo.Controllers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Practice;
import com.Beendo.Services.EntityService;
import com.Beendo.Services.UserService;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
public class EntityController extends RootController {

	@Autowired
	private EntityService entityService;

	private CEntitiy entity = new CEntitiy();
	private List<CEntitiy> entities;
	
	public String viewEntity()
	{
		entities = entityService.fetchAllEntitiesByUser();
		initHashOne(entities);
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
//		if(!entity.getName().isEmpty())
//			RequestContext.getCurrentInstance().execute("addDlg.hide()");
//		else
//			return;
		
//		List<CEntitiy> result =	entityService.isNameExist(entities, entity.getName());
//		if(result.size() <= 0)
		{
			entities.add(entity);
			entityService.save(entity);
			showMessage("Entity has been saved");
			clearData();
		}
//		else
//			showMessage("Entity name already exists!");
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
