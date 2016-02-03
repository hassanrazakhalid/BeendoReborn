package com.Beendo.Controllers;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Practice;
import com.Beendo.Services.EntityService;
import com.Beendo.Services.PractiseService;
import com.Beendo.Services.ProviderService;
import com.Beendo.Services.TransactionService;
import com.Beendo.Services.UserService;
import com.Beendo.Utils.OperationType;
import com.Beendo.Utils.Role;
import com.Beendo.Utils.Screen;
import com.Beendo.Utils.SharedData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Controller
@Scope(value = "session")
public class PractiseController {
	
	private OperationType operationType;

	private String entityName;
	private Integer entityId;
	private List<CEntitiy> listEntities;
	private List<Practice> listPractise;

	private CEntitiy currentEntity;

	private String practiceName;

	private boolean isEntityListEnabled;
	
	@Autowired
	private PractiseService practiseService;
	
	@Autowired
	private ProviderService providerService;

	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EntityService entityService;

	private Practice practise = new Practice();

	public boolean getIsEntityListEnabled(){
		return isEntityListEnabled;
	}
	
	public void onLoad() {
		refreshAll();
	}

	private void refreshAll() {
		listEntities = entityService.fetchAllByRole(Screen.Screen_Practice);
		listPractise = practiseService.fetchAllByRole();
		// return "Practise/PractiseView?faces-redirect=true";
		// return "PractiseView";

	
	}

	public String viewPractise() {

		/*
		 * listEntities = entityService.fetchAllByRole(); listPractise =
		 * practiseService.fetchAllByRole();
		 */

		// initHashOne(listEntities);
		// initHashTwo(listPractise);
		// return "Practise/PractiseView?faces-redirect=true";
		return "PractiseView";
	}

	public boolean canEditPracise() {

		boolean isOK = true;

		if (SharedData.getSharedInstace().getCurrentUser().getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString()))
			isOK = false;

		return isOK;
	}

	public boolean canCreatePracise() {

		boolean isOK = true;

		if (SharedData.getSharedInstace().getCurrentUser().getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString()))
			isOK = false;

		return isOK;
	}

	public boolean isSingleItemInEntityList() {

		if (listEntities.size() <= 1) {
			return true;
		} else
			return false;
	}

	/*
	 * public boolean isMoreItemsInEntityList(){
	 * 
	 * if(!isSingleItemInEntityList()) { return true; } else return false; }
	 */

	public String getFirstEntityName() {

		if (listEntities.isEmpty())
			return "Create Entity first";
		else
			return listEntities.get(0).getName();
	}

	public void initNewPractise() {

		practise = new Practice();
		practiceName = "";
	}

	public void createClicked() {

		this.operationType = OperationType.Create;
		isEntityListEnabled = false;
		initNewPractise();
	}

	public void updateClicked(Practice sender) {

		isEntityListEnabled = true;
		practiceName = sender.getName();
		practise = sender;

		this.operationType = OperationType.Edit;
	}

	private boolean shouldSavePractise() {

		boolean isOK = true;
		String error = null;

		if (operationType == OperationType.Edit) {
			if (!practiceName.equalsIgnoreCase(practise.getName())) {
				error = practiseService.checkDuplicateUsername(practiceName);
			}
		} else {
			error = practiseService.checkDuplicateUsername(practiceName);
		}

		if (error != null) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, error, null));
			isOK = false;
		}

		return isOK;
	}

	public void createEditLogic(ActionEvent event) {

		if (shouldSavePractise()) {

			try {
				practise.setName(practiceName);
				if (listEntities.size() > 0) {
					// Set<Practice> set = new HashSet<Practice>();
					// set.add(practise);

					if (listEntities.size() == 1)
						currentEntity = listEntities.get(0);
					else
						currentEntity = getEntityById(entityId);

					// currentEntity.setPracticeList(set);
					practise.setEntity(currentEntity);

					switch (operationType) {
					case Create: {

						{
							currentEntity.getPracticeList().add(practise);
							// practiseService.save(practise);
							entityService.update(currentEntity);
							listPractise.add(practise);
							initNewPractise();
						}
					}
						break;
					case Edit: {

						practiseService.update(practise);
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Saved Sucessfully"));
					}
						break;
					default:
						break;
					}
					RequestContext.getCurrentInstance().execute("PF('dlg2').hide()");
				}
			} catch (Exception e) {

				System.out.println(e);
			}
		}

	}

	private CEntitiy getEntityById(Integer entityId2) {
		// TODO Auto-generated method stub
		for (CEntitiy cEntitiy : listEntities) {

			if (cEntitiy.getId().compareTo(entityId2) == 0) {
				return cEntitiy;
			}
		}
		return null;
	}

	public void remove(Practice sender) {

		
		String error = "This practice is currently being used. Kindly unassign it before removing";
		try {
		
//			Set<Provider> providerList = sender.getProviders();
//			for (Provider provider : providerList) {
				
//				provider.getPracticeList().remove(sender);
//				provider.getPracticeList().remove(practise);
//				
//				practiseService.update(practise);
//			}
//			
//			providerService.updateProviderList(providerList);
			
			if(!userService.isUserExistForPractice(sender.getId()))
			{
				transactionService.deleteTransactionByPractics(sender.getId());
				practiseService.remove(sender);
				listPractise.remove(sender);				
			}
			else
			{
				showMessage("Error",error);
			}
		}
		catch (DataIntegrityViolationException e){
			
			showMessage("Error",error);
		}
		catch (Exception e) {
			
			System.out.println(e);
			// TODO: handle exception
		}
	}
	
	Practice getPracticeById(Integer id){
		
		for (Practice practice : listPractise) {
			
			if(practice.getId().compareTo(id) == 0)
				return practice;
		}
		return null;
	}

	public void showMessage(String heading,String msg) {

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error",
				msg);
		RequestContext.getCurrentInstance().showMessageInDialog(message);
	}
}