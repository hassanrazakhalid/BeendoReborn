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

import org.hibernate.StaleObjectStateException;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Practice;
import com.Beendo.Services.EntityService;
import com.Beendo.Services.UserService;
import com.Beendo.Utils.Constants;
import com.Beendo.Utils.OperationType;
import com.Beendo.Utils.Screen;
import com.Beendo.Utils.SharedData;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
@Scope(value="session")
public class EntityController extends RootController {

	@Autowired
	private EntityService entityService;

	private String entityName;
	
	private CEntitiy entity = new CEntitiy();
	private List<CEntitiy> entities;
	
	private OperationType operation;
	
	public String viewEntity() 
	{
		entities = entityService.fetchAllByRole(Screen.Screen_Entity);
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
	
	public void editButtonPressed(CEntitiy sender){
		
		entityName = sender.getName();
		entity = sender;
		operation = OperationType.Edit;
	}
	
	public boolean isEntityInfoValid(){
		
		boolean isOK = true;
		if(entityName.equalsIgnoreCase(entity.getName()))
			return isOK;
		
		String error =	entityService.isUsernameExist(entityName);
		if(error != null)
		{
			showMessage(error);
			isOK = false;
		}
		return isOK;
	}
	
	public void createPressed()
	{
		clearData();
		operation =  OperationType.Create;
	}
	
	public void saveButtonPressed(){
		
	
		try {
			if(isEntityInfoValid())
			{
				entity.setName(entityName);
				switch (operation) {
				case Create:
				{
					entities.add(entity);
					entityService.save(entity);
				}
					break;
				case Edit:
				{
					entityService.update(entity);
				}
				break;
				default:
					break;
				}
				
				//showMessage("Entity has been saved");
				clearData();
				RequestContext.getCurrentInstance().execute("PF('addDlg').hide()"); 
			}
			//RequestContext.getCurrentInstance().closeDialog("createEntity");
			//entity = new CEntitiy();
		}
		catch (StaleObjectStateException e){
			
			showMessage(Constants.ERRR_RECORDS_OUDATED);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	

	}
	
	public void deleteEntityClicked(CEntitiy sender){
		
		try {
			entityService.delete(sender);
			entities.remove(sender);
		}
		catch(DataIntegrityViolationException e){
			
			showMessage("Kindly unassign all practices before removing");
		}
		catch (StaleObjectStateException e){
			
			showMessage(Constants.ERRR_RECORDS_OUDATED);
		}
		catch (Exception e) {
			 
			showMessage("Kindly unassign all practices before removing");
			// TODO: handle exception
		}
	}
	
	public void clearData()
	{
		entity = new CEntitiy();
		entityName = "";
	}
	
	public void showMessage(String msg) {
		
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Entity", msg);     
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }
}
