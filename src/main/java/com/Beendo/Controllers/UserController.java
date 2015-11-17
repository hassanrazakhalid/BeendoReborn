package com.Beendo.Controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Role_Permission;
import com.Beendo.Entities.User;
import com.Beendo.Services.EntityService;
import com.Beendo.Services.RoleService;
import com.Beendo.Services.UserService;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
public class UserController {
	
	private boolean isEditMode;
	
	private Boolean sendEmail;
	
	@Autowired
	private EntityService entityService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;

	private User user = new User();

	private CEntitiy selectedEntity;
	private Set<Practice> selectedPractises;
	private Role_Permission selectedRole;
	
	private HashMap<Integer, Practice> tmpHasnPractise = new HashMap<Integer, Practice>();

	private List<CEntitiy> listEntities;
	private List<Practice> listPractise;
	private List<Role_Permission> listRoles;
	
	private List<User> listUsers = new ArrayList<User>();
	// ---------------------- Methods

	public String showUserMainView() {

		init();
		return "User/UserView";
	}

	public void initUserList(){
		
		List<User> tmpList = userService.fetchAll();
		listUsers = tmpList;
	}
	
	public void init() {

		isEditMode = false;
		listEntities = entityService.getAllEntities();

		if (listEntities.size() > 0) {
			CEntitiy entity = listEntities.get(0);
			if (entity.getPracticeList().size() > 0)
			{
				listPractise = entity.getPracticeList();
				for (Practice practice : listPractise) {
					
					tmpHasnPractise.put(practice.getId(), practice);
				}
			}

		}
		listRoles = roleService.getRoleList();
	}
	
	public Practice getPractiseById(Integer id){
		
		return tmpHasnPractise.get(id);
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
	
	public void addUserToSelectedPractise(){
		
//		Collection<CEntitiy> col =  hashEntities.values();
//		List<CEntitiy> list = new ArrayList(col);
		
		for (Object obj : selectedPractises) {

			Practice practise = (Practice)obj;
			user.setPractise(practise);
			practise.getUsers().add(user);
			System.out.println("asdsad");
			
		}
	}
	
	public void createButtonClicked(){
		
		if(isEditMode == false)
		{
			user.setEntity(selectedEntity);
			
			user.setRole(selectedRole);
			addUserToSelectedPractise();
			userService.save(user);
			listUsers.add(user);
			initUser();
		}
		else
		{
			userService.update(user);
		}
	}
	
	public void editButtonClicked(){
		
		isEditMode = true;
	}
	
	// Get Practse Obj
}
