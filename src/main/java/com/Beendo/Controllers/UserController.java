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

import org.hibernate.StaleObjectStateException;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
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
import com.Beendo.Utils.Constants;
import com.Beendo.Utils.OperationType;
import com.Beendo.Utils.Role;
import com.Beendo.Utils.Screen;
import com.Beendo.Utils.SharedData;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
@Scope(value="session")
public class UserController extends RootController implements DisposableBean, InitializingBean{

	private OperationType operationType;
	public List<String> listRoles = new ArrayList<>();

	private Boolean sendEmail;
	private String tmpUserName;
	private String tmpEmail;
	private boolean isEntityListDisabled;

	@Autowired
	private EntityService entityService;
	@Autowired
	private PermissionService roleService;
	@Autowired
	private UserService userService;

	@Autowired
	private PractiseService practiseService;

	private User user;

	private String selectedEntityId;
	// private CEntitiy selectedEntity;
	private List<String> selectedPractises = new ArrayList<>();
	private Permission selectedPermission;

	private boolean shouldshowEntity;
	private boolean shouldshowPractise;
	private boolean shouldshowPermission;

	private List<CEntitiy> listEntities = new ArrayList<>();
	private List<Practice> listPractise = new ArrayList<>();

	private List<User> listUsers = new ArrayList<User>();
	// Security Code

	@Autowired
	private AuthenticationManager authenticationManager;

	// ---------------------- Methods

	public boolean getIsEntityListDisabled(){
		
		return isEntityListDisabled;
	}
	
	public UserController(){
		
		System.out.println("In constructor");
	}
	
	public void onLoad(){
		
		shouldshowEntity = false;
		shouldshowPractise = false;
		shouldshowPermission = false;
		listRoles = reloadRolesList();
		operationType = OperationType.Create;
		initUserList();
		reloadEntities();

		if (listEntities.size() > 0) {

			selectedEntityId = listEntities.get(0).getId().toString();
		}
 
		reloadPractises();
	}
	
	public String showUserMainView() {


		return "UserView";
	}

	public void initUserList() {

		List<User> tmpList = userService.fetchAllByRole();

		for (User user : tmpList) {

			System.out
					.println("UserName:is" + user.getAppUserName() + "And Practise Count" + user.getPractises().size());
		}
		listUsers = tmpList;
	}

	public void initUser() {

		user = new User();
	}

	private CEntitiy getSelectedEntity() {

		/*
		 * if(selectedEntityId == null) selectedEntityId = "1";
		 */
		CEntitiy selectedEntity = getEntityById(Integer.valueOf(selectedEntityId));
		return selectedEntity;
	}

	public void entityChanged() {

		listPractise.clear();

		listPractise.addAll(getSelectedEntity().getPracticeList());
		clearHashTwo();
		initHashTwo(listPractise);
		System.out.println("Changed");
	}

	public void roleChanged() {

		updateFlags();
		if (user.getRoleName().equalsIgnoreCase(Role.ENTITY_ADMIN.toString())) // just
																				// show
																				// entity
		{
			// reloadEntities();
		} else if (user.getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString())) {
			// reloadEntities();
			if(user.getEntity() != null &&
			   user.getEntity().getId().compareTo(1) != 1)
			{
				if(listEntities.size() > 0)
					selectedEntityId = listEntities.get(0).getId().toString();
			}
				reloadPractises();
		} else {

			// listEntities.clear();
			listPractise.clear();
			// Hide Enrity + practise by default
			// clear the varavles as well
		}
	}

	private void resetDefaultFlags() {

		shouldshowEntity = false;
		shouldshowPractise = false;
		shouldshowPermission = false;
	}

	private void updateFlags() {

		if (user.getRoleName().equalsIgnoreCase(Role.ENTITY_ADMIN.toString())) // just
																				// show
																				// entity
		{
			shouldshowEntity = true;
			shouldshowPractise = false;
			shouldshowPermission = false;
		} else if (user.getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString())) {
			shouldshowEntity = true;
			shouldshowPractise = true;
			shouldshowPermission = true;
		} else {
			resetDefaultFlags();
		}
	}

	private void reloadEntities() {

		listEntities = entityService.fetchAllByRole(Screen.Screen_User);
		initHashOne(listEntities);
	}

	private void reloadPractises() {

		if (listEntities.size() > 0) {

			CEntitiy entity = null;
			if (selectedEntityId == null)
				entity = listEntities.get(0);
			else
				entity = getSelectedEntity();
			if (entity != null &&
				entity.getPracticeList().size() > 0) {
				listPractise = new ArrayList<Practice>(entity.getPracticeList());
				initHashTwo(listPractise);
			}
		}
	}

	public void createButtonClicked() {

		isEntityListDisabled = false;
		operationType = OperationType.Create;
		initUser();
		tmpUserName = "";
		tmpEmail = "";
		selectedPractises.clear();

		List<CEntitiy> tmpListEntity = listEntities;
		// List<CEntitiy> tmpListEntity = entityService.fetchAllByRole();
		if (tmpListEntity.size() > 0) {
			// selectedEntity = tmpListEntity.get(0);
		}
		selectedPermission = new Permission();
		resetDefaultFlags();
		/*
		 * List<Permission> tmpPermissions = getAllPermission();
		 * if(tmpPermissions.size() > 0) { selectedPermission =
		 * tmpPermissions.get(0); }
		 */
	}

	private boolean isUserInfoValid() {

		String error = null;
		boolean isOK = true;

		if (selectedEntityId == null)
			selectedEntityId = "1";
		CEntitiy selectedEntity = getEntityById(Integer.valueOf(selectedEntityId));

		if (operationType == OperationType.Edit) {
			if (!tmpUserName.equalsIgnoreCase(user.getAppUserName())) {

				error = userService.isUsernameExist(tmpUserName);
			}
			if (error == null &&
				!tmpEmail.equalsIgnoreCase(user.getEmail()) &&
				tmpEmail.length() > 0) {
				error = userService.isEmailExist(tmpEmail);
			}

			if (error == null && user.getRoleName().equalsIgnoreCase(Role.ENTITY_ADMIN.toString()))// Check//
																									// for//
																									// 1//
																									// admin
			{

				if (selectedEntity.getId().compareTo(user.getEntity().getId()) != 0) {
					error = userService.isEntityAdminExist(selectedEntity.getId());
				}
			}
		} else {
			error = userService.isUsernameExist(tmpUserName);

			if (error == null &&
				tmpEmail.length() > 0) {
				error = userService.isEmailExist(tmpEmail);
			}
			if (error == null && user.getRoleName().equalsIgnoreCase(Role.ENTITY_ADMIN.toString())) {
				error = userService.isEntityAdminExist(selectedEntity.getId());
			}
		}

		// if(userName.equalsIgnoreCase(user.getAppUserName()) )
		// {
		// error = null;
		// }
		// else
		// {
		// error = userService.isUsernameExist(getUserName());
		// }

		if (error != null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, error, null);
			FacesContext.getCurrentInstance().addMessage(null, message);
			isOK = false;
		}

		/* RequestContext.getCurrentInstance().execute("PF('block').hide()"); */
		return isOK;
	}

	public void popoulatePracticesByIds(List<String> tmpSelectedPractises) {

		for (int i = 0; i < tmpSelectedPractises.size(); i++) {
			String selectedIndex = tmpSelectedPractises.get(i);
			user.getPractises().add(getPractiseById(Integer.parseInt(selectedIndex)));
			// user.setPractises(new HashSet<>(selectedPractises));
		}
	}

	public void saveButtonClicked(ActionEvent event) {

		try {
			
		}
		catch (StaleObjectStateException e){
			
			showMessage(Constants.ERRR_RECORDS_OUDATED);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		if (isUserInfoValid()) {

			CEntitiy selectedEntity = null;

			user.setAppUserName(tmpUserName);
			user.setEmail(tmpEmail);

			user.getPractises().clear();
			if (user.getRoleName().equalsIgnoreCase(Role.ROOT_ADMIN.toString())
					|| user.getRoleName().equalsIgnoreCase(Role.ROOT_USER.toString())) {
				selectedEntity = entityService.fetchById(1);
			} 
			else
			{
				selectedEntity = getSelectedEntity();
				popoulatePracticesByIds(selectedPractises);
			}
			int x = selectedEntity.getUsers().size();
			user.setEntity(selectedEntity);

			
			

			if (!user.getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString())) {
				selectedPermission.selectAllPermissions();
			}
			user.setPermission(selectedPermission);

			switch (operationType) {
			case Create:
			case Copy: {
				selectedEntity.getUsers().add(user); // addUserToSelectedPractise();
				// userService.save(user);

				{
					entityService.update(selectedEntity);
					listUsers.add(user);
					initUser();
				}

			}
				break;
			case Edit: {
				userService.update(user);
			}
				break;

			default:
				break;
			}

			RequestContext.getCurrentInstance().execute("PF('userCreateDialog').hide()");
		}
	}

	public boolean shouldShowCreateUser() {

		boolean isOK = true;

		if (SharedData.getSharedInstace().getCurrentUser().getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString()))
			isOK = false;

		return isOK;

	}

	public void editButtonClicked(User sender) {

		isEntityListDisabled = true;
		operationType = OperationType.Edit;

		// Set<Practice> practises = sender.getPractises();

		// listPractise = getSelectedPractices(sender);

		tmpUserName = sender.getAppUserName();
		tmpEmail = sender.getEmail();
		updateSelectedEntity(sender);
		updatePractiseList(sender);
		updateSelectedPermission(sender);

		user = sender;
		updateFlags();
	}

	private void updatePractiseList(User user) {

		if (user.getEntity().getId().compareTo(1) != 0) {
			selectedPractises.clear();
			for (Practice practise : user.getPractises()) {

				selectedPractises.add(String.valueOf(practise.getId()));
			}
			listPractise.clear();
			listPractise.addAll(getSelectedEntity().getPracticeList());
		}
	}

	private void updateSelectedPermission(User user) {

		selectedPermission = user.getPermission();
	}

	private void updateSelectedEntity(User user) {

			selectedEntityId = String.valueOf(user.getEntity().getId());
		// selectedEntity = getEntityById(user.getEntity().getId());
	}

	public void copyButtonClicked(User sender) {

		operationType = OperationType.Copy;
		user = User.copy(sender);
	}

	public void deleteUserClicked(User sender) {

		try {
			
			CEntitiy entity = getEntityById(sender.getEntity().getId());
			if(entity != null)
			{
				int x = entity.getUsers().size();
				entity.removeUserById(sender.getId());
				x = entity.getUsers().size();
				entityService.update(entity);
			}
			
			userService.remove(sender);
			listUsers.remove(sender);

			
		}
		catch (StaleObjectStateException e){
			
			showMessage(Constants.ERRR_RECORDS_OUDATED);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	public CEntitiy getEntityById(Integer id) {

		for (CEntitiy cEntitiy : listEntities) {

			if (cEntitiy.getId().compareTo(id) == 0)
				return cEntitiy;
		}
		return null;
	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("user bean destryed");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Login bean created");
	}
	
public void showMessage(String msg) {
		
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "User", msg);     
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }
}
