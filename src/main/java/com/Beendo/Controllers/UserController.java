package com.Beendo.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
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
public class UserController extends RootController {
	
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
	private ArrayList<Practice> selectedPractises =  new ArrayList<>();
	private Role_Permission selectedRole;
	
	private HashMap<Integer, Practice> tmpHasnPractise = new HashMap<Integer, Practice>();

	private List<CEntitiy> listEntities = new ArrayList<>();
	private List<Practice> listPractise = new ArrayList<>();
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
		
		for (User user : tmpList) {
			
			System.out.println("UserName:is"+user.getAppUserName()+"And Practise Count"+user.getPractises().size());
		}
		listUsers = tmpList;
	}
	
	public void init() {

		operationType = OperationType.Create;
		listEntities = entityService.fetchAllEntitiesByUser();
		initHashOne(listEntities);

		if (listEntities.size() > 0) {
			CEntitiy entity = listEntities.get(0);
			if (entity.getPracticeList().size() > 0)
			{
				listPractise = new ArrayList<Practice>(entity.getPracticeList());
				initHashTwo(listPractise);
				/*for (Practice practice : listPractise) {
					
					tmpHasnPractise.put(practice.getId(), practice);
				}*/
			}

		}
		listRoles = roleService.getRoleList();
		initRoleHash(listRoles);
		
	}
	
	/*public Practice getPractiseById(Integer id){
		
		return tmpHasnPractise.get(id);
	}
*/
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
		
		operationType = OperationType.Create;
		user = new User();
		selectedPractises.clear();
		
		List<CEntitiy> tmpListEntity = entityService.fetchAllEntitiesByUser();
		if(tmpListEntity.size() > 0)
		{
			selectedEntity = tmpListEntity.get(0);
		}
		List<Role_Permission> tmpRoles = getAllRoles();
		if(tmpRoles.size() > 0)
		{
			selectedRole = tmpRoles.get(0);
		}
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
			user.setRole(selectedRole);
			
			switch (operationType) {
			case Create:
			case Copy:
			{

//				addUserToSelectedPractise();
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
			
//			RequestContext.getCurrentInstance().execute("PF('userCreateDialog').hide()");
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
		
		selectedRole = getRoleById(user.getRole().getId());
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
