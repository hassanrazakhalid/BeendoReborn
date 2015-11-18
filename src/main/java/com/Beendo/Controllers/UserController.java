package com.Beendo.Controllers;

import java.util.ArrayList;
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
import com.Beendo.Utils.OperationType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
public class UserController {
	
	private OperationType operationType;
	
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
		initUserList();
		return "User/UserView";
	}

	public void initUserList(){
		
		List<User> tmpList = userService.fetchAll();
		listUsers = tmpList;
	}
	
	public void init() {

		operationType = OperationType.Create;
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

		operationType = OperationType.Create;
		initUser();
	}

	public void entityChanged() {

		listPractise = selectedEntity.getPracticeList();
		System.out.println("Changed");
	}
	
	public void addUserToSelectedPractise(){
		
//		Collection<CEntitiy> col =  hashEntities.values();
//		List<CEntitiy> list = new ArrayList(col);
		
	/*	for (Object obj : selectedPractises) {

			Practice practise = (Practice)obj;
			user.setPractise(practise);
			practise.getUsers().add(user);
			System.out.println("asdsad");
			
		}
*/	}
	
	public void createButtonClicked(){
		
		switch (operationType) {
		case Create:
		case Copy:
		{
			user.setEntity(selectedEntity);
			user.setPractises(selectedPractises);
			user.setRole(selectedRole);
			addUserToSelectedPractise();
			userService.save(user);
			listUsers.add(user);
			initUser();
		}
			break;
		case Edit:
		{
			userService.update(user);
		}
			break;

		default:
			break;
		}
	}
	
	public void editButtonClicked(User sender){
		
		operationType = OperationType.Edit;
		user = sender;
	}
	
	public void copyButtonClicked(User sender){
		
		operationType = OperationType.Copy;
		user = User.copy(sender);
	}
	
	public void remove(User sender){
		
		userService.remove(sender);
		listUsers.remove(sender);
	}
}
