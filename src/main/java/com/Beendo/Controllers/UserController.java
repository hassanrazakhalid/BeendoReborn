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
import com.Beendo.Utils.SharedData;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
public class UserController extends RootController {

	private OperationType operationType;
	public List<String> listRoles = new ArrayList<>();

	private Boolean sendEmail;
	private String userName;

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

	public String showUserMainView() {

		shouldshowEntity = false;
		shouldshowPractise = false;
		shouldshowPermission = false;
		listRoles = reloadRolesList();
		operationType = OperationType.Create;
		initUserList();
		reloadEntities();
		reloadPractises();
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
//			 reloadEntities();
		} else if (user.getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString())) {
			// reloadEntities();
			reloadPractises();
		} else {
			
//			listEntities.clear();
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

		listEntities = entityService.fetchAllByRole();
		initHashOne(listEntities);
	}

	private void reloadPractises() {

		if (listEntities.size() > 0) {
			CEntitiy entity = listEntities.get(0);
			if (entity.getPracticeList().size() > 0) {
				listPractise = new ArrayList<Practice>(entity.getPracticeList());
				initHashTwo(listPractise);
			}
		}
	}

	public void createButtonClicked() {

		operationType = OperationType.Create;
		initUser();
		userName = "";
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

		if(selectedEntityId == null)
			selectedEntityId = "1";
		CEntitiy selectedEntity = getEntityById(Integer.valueOf(selectedEntityId));

		if (operationType == OperationType.Edit) {
			if (!getUserName().equalsIgnoreCase(user.getAppUserName())) {

				error = userService.isUsernameExist(getUserName());
			}
			if (error == null) {
				if (user.getRoleName().equalsIgnoreCase(Role.ENTITY_ADMIN.toString()))// Check// for// 1// admin
				{

					if (selectedEntity.getId().compareTo(user.getEntity().getId()) != 0) {
						error = userService.isEntityAdminExist(selectedEntity.getId());
					}
				}

			}

		} else {
			error = userService.isUsernameExist(getUserName());
			if (error == null) {
				if (user.getRoleName().equalsIgnoreCase(Role.ENTITY_ADMIN.toString())) {
					error = userService.isEntityAdminExist(selectedEntity.getId());
				}
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

		if (isUserInfoValid()) {

			CEntitiy selectedEntity = null;

			user.setAppUserName(getUserName());

			if (user.getRoleName().equalsIgnoreCase(Role.ROOT_ADMIN.toString())
					|| user.getRoleName().equalsIgnoreCase(Role.ROOT_USER.toString())) {
				selectedEntity = entityService.fetchById(1);
			} else
				selectedEntity = getSelectedEntity();
			user.setEntity(selectedEntity);

			user.getPractises().clear();
			popoulatePracticesByIds(selectedPractises);

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

		operationType = OperationType.Edit;

		// Set<Practice> practises = sender.getPractises();

		// listPractise = getSelectedPractices(sender);

		this.setUserName(sender.getAppUserName());
		updateSelectedEntity(sender);
		updatePractiseList(sender);
		updateSelectedPermission(sender);

		user = sender;
		updateFlags();
		return;
		/*
		 * List<Practice> list = practiseService.fetchAll(); listPractise = new
		 * ArrayList<Practice>(); for (Practice practic : list) { Practice pr =
		 * new Practice();
		 * 
		 * pr.setId(practic.getId()); pr.setName(practic.getName());
		 * 
		 * listPractise.add(pr); }
		 * 
		 * 
		 * listPractise = list ;
		 * 
		 * List<Practice> listSelected = new ArrayList<Practice>(practises);
		 * List<Practice> listSelectedFinal = new ArrayList<Practice>();
		 * 
		 * 
		 * for (Practice pract : listPractise) { for (Practice practic :
		 * listSelected) { if(pract.getId()==practic.getId()) {
		 * listSelectedFinal.add(pract); }
		 * 
		 * 
		 * }
		 * 
		 * 
		 * selectedPrac = new Practice[listSelectedFinal.size()]; selectedPrac =
		 * listSelectedFinal.toArray(selectedPrac);
		 * 
		 * 
		 * // selectedPractises = new HashSet<>(0); // selectedPractises =
		 * practises;
		 * 
		 * user = sender;
		 */

	}

	/*
	 * private List<Payer> getSelectedPayers(Provider _provider) { List<Payer>
	 * list = new ArrayList();
	 * 
	 * for (Payer payer : _provider.getPayerList()) {
	 * 
	 * list.add(getPayerById(payer.getId())); }
	 * 
	 * return list; }
	 */

	private void updatePractiseList(User user) {

		/*
		 * HashMap<Integer, Practice> tmpHashMap = new HashMap<>();
		 * 
		 * for (Practice practise : getAllHashTwo()) {
		 * 
		 * tmpHashMap.put(practise.getId(), practise); }
		 * 
		 * listPractise = new ArrayList<Practice>(tmpHashMap.values());
		 */

		if(user.getEntity().getId().compareTo(1) != 0)
		{
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

		userService.remove(sender);
		listUsers.remove(sender);
	}
}
