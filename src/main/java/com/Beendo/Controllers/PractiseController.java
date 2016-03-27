package com.Beendo.Controllers;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.hibernate.StaleObjectStateException;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.User;
import com.Beendo.Services.EntityService;
import com.Beendo.Services.IEntityService;
import com.Beendo.Services.IPracticeCallBack;
import com.Beendo.Services.IPractiseService;
import com.Beendo.Services.IProviderService;
import com.Beendo.Services.ITransactionService;
import com.Beendo.Services.IUserService;
import com.Beendo.Services.PractiseService;
import com.Beendo.Services.ProviderService;
import com.Beendo.Services.TransactionService;
import com.Beendo.Services.UserService;
import com.Beendo.Utils.Constants;
import com.Beendo.Utils.OperationType;
import com.Beendo.Utils.Role;
import com.Beendo.Utils.Screen;
import com.Beendo.Utils.SharedData;
import com.github.javaplugs.jsf.SpringScopeView;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Controller
//@Scope(value = "session")
@SpringScopeView
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
	private IUserService userService;
	@Autowired
	private IPractiseService practiseService;
	@Autowired
	private IEntityService entityService;
	

	private Practice practise = new Practice();
	
	private User tmpUser;

	public boolean getIsEntityListEnabled(){
		return isEntityListEnabled;
	}
	
	public void onLoad() {
		refreshAllData();
	}

	private void refreshAllData() {
		
		IPracticeCallBack callback = (User user, List<CEntitiy>entityList, List<Practice>practiceList)->{

			tmpUser = user;
			listEntities = entityList;
			listPractise = practiceList;			
		};
		// return "Practise/PractiseView?faces-redirect=true";
		// return "PractiseView";
		practiseService.refreshAllData(callback);
	}

	public boolean shouldShowDelete(){
		
		boolean show = true;
		
		if(tmpUser.getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString()))
			show = false;
		return show;
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

					if (listEntities.size() == 1)//means just pick the 1st entry from list
						currentEntity = listEntities.get(0);
					else
						currentEntity = getEntityById(entityId);

					// currentEntity.setPracticeList(set);
					practise.setEntity(currentEntity);

					switch (operationType) {
					case Create: {

						{
//							 practiseService.save(practise);
							currentEntity.getPracticeList().add(practise);
							
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
//					refreshAll();
				}
			}
			catch (StaleObjectStateException e){
				
				showMessage("Error",Constants.ERRR_RECORDS_OUDATED);
			}
			catch (Exception e) {

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

	/*
	 * this will 
	 */
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
//				List<Integer> idsPractice = new ArrayList<>(); 
//				idsPractice.add(sender.getId());
//				transactionService.deleteTransactionByPractics(idsPractice);
				
//				List<Integer> idsProvider = new ArrayList<>();
//				for (Provider provider : sender.getProviders()) {
//					idsProvider.add(provider.getId());
//				}
//				transactionService.deleteTransactionByProvider(idsProvider);
				
				
			
				practiseService.remove(sender);
				listPractise.remove(sender);
				refreshAllData();
			}
			else
			{
				showMessage("Error",error);
			}
		}
		catch (DataIntegrityViolationException e){
			
			showMessage("Error",error);
		}
		catch (StaleObjectStateException e){
			
			showMessage("Error",Constants.ERRR_RECORDS_OUDATED);
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