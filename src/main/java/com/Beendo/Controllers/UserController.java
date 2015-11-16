package com.Beendo.Controllers;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Role_Permission;
import com.Beendo.Entities.User;
import com.Beendo.Services.EntityService;
import com.Beendo.Services.RoleService;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
public class UserController {
	
	private Boolean sendEmail;
	
	@Autowired
	private EntityService entityService;

	@Autowired
	private RoleService roleService;

	private User user;

	private CEntitiy selectedEntity;
	private Practice selectedPractise;
	private Role_Permission selectedRole;

	private List<CEntitiy> listEntities;
	private List<Practice> listPractise;
	private List<Role_Permission> listRoles;
	// ---------------------- Methods

	public String showUserMainView() {

		init();
		return "User/UserView";
	}

	
	public void init() {

		listEntities = entityService.getAllEntities();

		if (listEntities.size() > 0) {
			CEntitiy entity = listEntities.get(0);
			if (entity.getPracticeList().size() > 0)
				listPractise = entity.getPracticeList();

		}
		listRoles = roleService.getRoleList();
	}

	public void initUser() {

		user = new User();

	}

	public void createUserClicked() {

		initUser();

	}

	public void entityChanged() {

		listPractise = selectedEntity.getPracticeList();
		System.out.println("Changed");

	}
}
