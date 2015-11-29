package com.Beendo.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.print.attribute.standard.Severity;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.Permission;
import com.Beendo.Entities.User;
import com.Beendo.Services.EntityService;
import com.Beendo.Services.PractiseService;
import com.Beendo.Services.PermissionService;
import com.Beendo.Services.UserService;
import com.Beendo.Utils.OperationType;
import com.Beendo.Utils.Role;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
public class UserController extends RootController {
	
	private OperationType operationType;
	
	private Boolean sendEmail;
	
	@Autowired
	private EntityService entityService;
	@Autowired
	private PermissionService roleService;
	@Autowired
	private UserService userService;
	
	@Autowired
	private PractiseService practiseService;

	private User user;

	private CEntitiy selectedEntity;
	private ArrayList<Practice> selectedPractises =  new ArrayList<>();
	private Permission selectedPermission;
	
	private boolean shouldshowEntity;
	private boolean shouldshowPractise;

	private List<CEntitiy> listEntities = new ArrayList<>();
	private List<Practice> listPractise = new ArrayList<>();
	
	
	
	private List<User> listUsers = new ArrayList<User>();
	// Security Code
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	// ---------------------- Methods
	
	public String showUserMainView() {

		shouldshowEntity = false;
		shouldshowPractise = false;
		reloadRolesList();
		operationType = OperationType.Create;
		initUserList();
		reloadEntities();
		return "UserView";
	}

	public void initUserList(){
		
		List<User> tmpList = userService.fetchAll();
		
		for (User user : tmpList) {
			
			System.out.println("UserName:is"+user.getAppUserName()+"And Practise Count"+user.getPractises().size());
		}
		listUsers = tmpList;
	}
	
	public void initUser() {

		user = new User();
	}

	public void createUserClicked() {

		operationType = OperationType.Create;
		initUser();
	}

	public void entityChanged() {

		listPractise.clear();
		listPractise.addAll(selectedEntity.getPracticeList());
		clearHashTwo();
		initHashTwo(listPractise);
		System.out.println("Changed");
	}
	
	public void roleChanged(){
		
		updateFlags();
		if(user.getRoleName().equalsIgnoreCase(Role.ENTITY_ADMIN.toString())) //just show entity
		{
			reloadEntities();
		}
		else if(user.getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString()))
		{
			reloadEntities();
			reloadPractises();
		}
		else 
		{
			listEntities.clear();
			listPractise.clear();
			// Hide Enrity + practise by default
			// clear the varavles as well	
		}
	}
	
	private void updateFlags(){
		
		if(user.getRoleName().equalsIgnoreCase(Role.ENTITY_ADMIN.toString())) //just show entity
		{
			shouldshowEntity = true;
			shouldshowPractise = false;
		}
		else if(user.getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString()))
		{
			shouldshowEntity = true;
			shouldshowPractise = true;
		}
		else 
		{
			shouldshowEntity = false;
			shouldshowPractise = false;
		}
	}
	
	private void reloadEntities(){
		
		listEntities = entityService.fetchAllEntitiesByUser();
		initHashOne(listEntities);
	}
	
	private void reloadPractises(){
		
		if (listEntities.size() > 0) {
			CEntitiy entity = listEntities.get(0);
			if (entity.getPracticeList().size() > 0)
			{
				listPractise = new ArrayList<Practice>(entity.getPracticeList());
				initHashTwo(listPractise);
			}
		}
	}
	
	public void createButtonClicked(){
		
		operationType = OperationType.Create;
		initUser();
		selectedPractises.clear();
		
		List<CEntitiy> tmpListEntity = entityService.fetchAllEntitiesByUser();
		if(tmpListEntity.size() > 0)
		{ 
			selectedEntity = tmpListEntity.get(0);
		}
		selectedPermission = new Permission();
	/*	List<Permission> tmpPermissions = getAllPermission();
		if(tmpPermissions.size() > 0)
		{
			selectedPermission = tmpPermissions.get(0);
		}*/
	}
	
	private boolean isUserInfoValid(){
		
		boolean isOK = true;
		
		if(user.getUsername().length() <= 0)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Username", "Username cannot be empty"));
			isOK = false;
		}
		
		return isOK;
	}
	
	public void saveButtonClicked(ActionEvent event){
		
		if(isUserInfoValid())
		{
			user.setEntity(selectedEntity);
			user.setPractises(new HashSet<>(selectedPractises));
			user.setPermission(selectedPermission);
			
			switch (operationType) {
			case Create:
			case Copy:
			{

				selectedEntity.getUsers().add(user);
//				addUserToSelectedPractise();
//				userService.save(user);
				entityService.update(selectedEntity);
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
			
			RequestContext.getCurrentInstance().execute("PF('userCreateDialog').hide()");
		}
	}
	
	public void editButtonClicked(User sender){
		
		operationType = OperationType.Edit;
		
	//	Set<Practice> practises = sender.getPractises();

//		listPractise = getSelectedPractices(sender);
		updatePractiseList(sender);
		updateSelectedRole(sender);
		updateSelectedEntity(sender);
		user = sender;
		updateFlags();
		return;
/*		List<Practice> list = 		practiseService.fetchAll();
		listPractise =  new ArrayList<Practice>();
		for (Practice practic : list) {
			Practice pr = new Practice();
			
			pr.setId(practic.getId());
			pr.setName(practic.getName());
			
			listPractise.add(pr);
		}
		
		
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


 selectedPrac = new Practice[listSelectedFinal.size()];
 selectedPrac = listSelectedFinal.toArray(selectedPrac);

		
//		selectedPractises = new  HashSet<>(0);
//		selectedPractises = practises;
		
		user = sender;*/

	}
	
	/*private List<Payer> getSelectedPayers(Provider _provider)
	{
		List<Payer> list = new ArrayList();
		
		for (Payer payer : _provider.getPayerList()) {
			
			list.add(getPayerById(payer.getId()));
		}
		
		return list;
	}*/
	
	private void updatePractiseList(User user){
		
/*		HashMap<Integer, Practice> tmpHashMap = new HashMap<>();
		
		for (Practice practise : getAllHashTwo()) {
			
			tmpHashMap.put(practise.getId(), practise);
		}
		
		listPractise = new ArrayList<Practice>(tmpHashMap.values());*/
		
		selectedPractises.clear();
		for (Practice practise : user.getPractises()) {
			
			selectedPractises.add(getPractiseById(practise.getId()));
		}
	}

	private void updateSelectedRole(User user){
		
		selectedPermission = user.getPermission();
	}
	
	private void updateSelectedEntity(User user){
		
		selectedEntity = getEntityById(user.getEntity().getId());
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
