package com.Beendo.Controllers;

import java.util.List;

import javax.faces.application.FacesMessage;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.Role_Permission;
import com.Beendo.Services.RoleService;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
public class RoleController {

	@Autowired
	private RoleService roleService;
	
	private Role_Permission role = new Role_Permission();
	private List<Role_Permission> roles;
	private Boolean isEditMode;
	
	public String view()
	{
		roles = roleService.findAll();
		return "RoleView";
	}
	
	public void updateClicked(Role_Permission _role)
	{
		role = _role;
		isEditMode = true;
	}
	
	public void copyClicked(Role_Permission _role)
	{
		Role_Permission rol = Role_Permission.copy(_role);
		role = rol;
		isEditMode = false;
	}
	
	public void saveInfo()
	{
		if(isEditMode)
		{
			roleService.update(role);
			showMessage("Role has been updated");
		}
		else
		{
			roles.add(role);
			roleService.save(role);
			clearData();
			showMessage("Role has been saved");
		}
	}
	
	public void clearData()
	{
		role = new Role_Permission();
		isEditMode = false;
	}
	
	public void showMessage(String msg) {
		
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Role", msg);     
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }
	
}
