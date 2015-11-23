package com.Beendo.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Role_Permission;
import com.Beendo.Entities.User;
import com.Beendo.Services.EntityService;
import com.Beendo.Services.PractiseService;
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
	
	@Autowired
	private PractiseService practiseService;

	private User user = new User();

	private CEntitiy selectedEntity;
	private Set<Practice> selectedPractises;
	private Practice[] selectedPrac;
	private Role_Permission selectedRole;
	
	private HashMap<Integer, Practice> tmpHasnPractise = new HashMap<Integer, Practice>();

	private List<CEntitiy> listEntities;
	private List<Practice> listPractise;
	private List<Role_Permission> listRoles;
	
	private List<User> listUsers = new ArrayList<User>();
	// Security Code
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	// ---------------------- Methods
	
	public String showUserMainView() {

		init();
		initUserList();
		return "UserView";
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
		user = new User();
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
		user = new User();
		operationType = OperationType.Edit;
		
		Set<Practice> practises = sender.getPractises();

		List<Practice> list = 		practiseService.fetchAll();
		listPractise =  new ArrayList<Practice>();
	/*	for (Practice practic : list) {
			Practice pr = new Practice();
			
			pr.setId(practic.getId());
			pr.setName(practic.getName());
			
			listPractise.add(pr);
		}*/
		
		
		listPractise = list ;
		
List<Practice> listSelected = new ArrayList<Practice>(practises);
List<Practice> listSelectedFinal = new ArrayList<Practice>();


for (Practice pract : listPractise) {
for (Practice practic : listSelected) {
	if(pract.getId()==practic.getId())
	{
		listSelectedFinal.add(pract);
	}
	
	
}
}


 selectedPrac = new Practice[listSelectedFinal.size()];
 selectedPrac = listSelectedFinal.toArray(selectedPrac);

		
//		selectedPractises = new  HashSet<>(0);
//		selectedPractises = practises;
		
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

	public OperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}

	public Boolean getSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(Boolean sendEmail) {
		this.sendEmail = sendEmail;
	}

	public EntityService getEntityService() {
		return entityService;
	}

	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public CEntitiy getSelectedEntity() {
		return selectedEntity;
	}

	public void setSelectedEntity(CEntitiy selectedEntity) {
		this.selectedEntity = selectedEntity;
	}

	public Set<Practice> getSelectedPractises() {
		return selectedPractises;
	}

	public void setSelectedPractises(Set<Practice> selectedPractises) {
		this.selectedPractises = selectedPractises;
	}

	public Role_Permission getSelectedRole() {
		return selectedRole;
	}

	public void setSelectedRole(Role_Permission selectedRole) {
		this.selectedRole = selectedRole;
	}

	public HashMap<Integer, Practice> getTmpHasnPractise() {
		return tmpHasnPractise;
	}

	public void setTmpHasnPractise(HashMap<Integer, Practice> tmpHasnPractise) {
		this.tmpHasnPractise = tmpHasnPractise;
	}

	public List<CEntitiy> getListEntities() {
		return listEntities;
	}

	public void setListEntities(List<CEntitiy> listEntities) {
		this.listEntities = listEntities;
	}

	public List<Practice> getListPractise() {
		return listPractise;
	}

	public void setListPractise(List<Practice> listPractise) {
		this.listPractise = listPractise;
	}

	public List<Role_Permission> getListRoles() {
		return listRoles;
	}

	public void setListRoles(List<Role_Permission> listRoles) {
		this.listRoles = listRoles;
	}

	public List<User> getListUsers() {
		return listUsers;
	}

	public void setListUsers(List<User> listUsers) {
		this.listUsers = listUsers;
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public PractiseService getPractiseService() {
		return practiseService;
	}

	public void setPractiseService(PractiseService practiseService) {
		this.practiseService = practiseService;
	}

	public Practice[] getSelectedPrac() {
		return selectedPrac;
	}

	public void setSelectedPrac(Practice[] selectedPrac) {
		this.selectedPrac = selectedPrac;
	}


	
}
