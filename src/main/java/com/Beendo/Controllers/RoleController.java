package com.Beendo.Controllers;

import java.util.List;

import javax.faces.application.FacesMessage;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Permission;
import com.Beendo.Services.PermissionService;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
public class RoleController {

	@Autowired
	private PermissionService roleService;
	
	private Permission role = new Permission();
	private List<Permission> roles;
	private Boolean isEditMode;
	
	public String view()
	{
		roles = roleService.findAll();
		return "RoleView";
	}
	
	public void updateClicked(Permission _role)
	{
		role = _role;
		isEditMode = true;
	}
	
	public void copyClicked(Permission _role)
	{
		Permission rol = Permission.copy(_role);
		role = rol;
		isEditMode = false;
	}
	
	public void saveInfo()
	{
/*		if(isEditMode)
		{
			roleService.update(role);
			showMessage("Role has been updated");
		}
		else
		{
			List<Permission> result =	roleService.isNameExist(roles, role.getName());
			if(result.size() <= 0)
			{
				roles.add(role);
				roleService.save(role);
				clearData();
				RequestContext.getCurrentInstance().execute("PF('Dlg1').hide()"); //oncomplete="PF('Dlg1').hide();" 
				//showMessage("Role has been saved");
			}
			else
				showMessage("Role name already exists!");
		}*/
	}
	
	public void clearData()
	{
		role = new Permission();
		isEditMode = false;
	}
	
	public void showMessage(String msg) {
		
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Role", msg);     
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }
	
}
