package com.Beendo.Controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;

import org.hibernate.StaleObjectStateException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.Beendo.Dto.DocumentCell;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Document;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.ProviderTransaction;
import com.Beendo.Entities.User;
import com.Beendo.Services.EntityService;
import com.Beendo.Services.IEntityService;
import com.Beendo.Services.IPayerService;
import com.Beendo.Services.IPractiseService;
import com.Beendo.Services.IProviderService;
import com.Beendo.Services.ITransactionService;
import com.Beendo.Services.IUserService;
import com.Beendo.Services.PayerService;
import com.Beendo.Services.PractiseService;
import com.Beendo.Services.ProviderService;
import com.Beendo.Services.TransactionService;
import com.Beendo.Services.UserService;
import com.Beendo.Utils.Constants;
import com.Beendo.Utils.OperationType;
import com.Beendo.Utils.ProviderFile;
import com.Beendo.Utils.Role;
import com.Beendo.Utils.Screen;
import com.Beendo.Utils.SharedData;
import com.github.javaplugs.jsf.SpringScopeView;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
//@Scope(value = "session")
@SpringScopeView
public class ProviderController implements DisposableBean {

	@Autowired
	private IProviderService providerService;


	@Autowired
	private IPractiseService practiseService;

	
	private List<ProviderTransaction> transactions;

	private String entityName;

	private Provider provider = new Provider();
	private List<Provider> providerList = new ArrayList<>();

	private List<Practice> practiceList = new ArrayList<>();
	private List<String> selectedPractices = new ArrayList<>();

//	private List<Payer> payerList = new ArrayList<>();;
//	private List<Integer> selectedPayers = new ArrayList<>();;

	private List<CEntitiy> entityList = new ArrayList<>();;

	private String currentEntity;
	private OperationType opetationType;

	private List<DocumentCell> documentCells = new ArrayList<>();;

	private boolean isEntityListDisabled;
	
	public boolean getIsEntityListDisabled() {

		return isEntityListDisabled;
	}

	private User tmpUser;

	private void refreshAllData() {

//		// User user = SharedData.getSharedInstace().getCurrentUser();
//		tmpUser = userService.findById(SharedData.getSharedInstace().getCurrentUser().getId(), false);
//
//		providerList = providerService.fetchAllByRole(); // providerService.fetchAllByUser();
//		entityList = entityService.fetchAllByRole(Screen.Screen_Provider);
//		payerList = payerService.getAll();
//		// new
//		// ArrayList(SharedData.getSharedInstace().getCurrentUser().getEntity().getPracticeList());
//
//		transactions = transactionService.fetchAllByRole();
		// initHashTwo(practiceList);

		refreshPractics();
		documentCells = new ArrayList<>();
		
		ProviderCallback response = (User user, List<Provider>providerList,List<CEntitiy> entityList,List<Payer> payerList,List<ProviderTransaction> transactions)->{
			
			this.tmpUser = user;
//			for(int i=0; i< 10;i++)
//			{
//				this.providerList.add(providerList.get(i));
//			}
			this.providerList = providerList;
			this.entityList = entityList;
//			this.payerList = payerList;
			this.transactions = transactions;
			
		};
		providerService.refreshAllData(response);
	}

	public void onLoad() {

		refreshAllData();
	}

	public String view() {

		return "ProviderView";
	}

	public String getFirstEntityName() {

		if (entityList.isEmpty())
			return "No Entity Exist";
		else
			return entityList.get(0).getName();
	}

	public boolean isSingleItemInEntityList() {

		if (entityList.size() <= 1) {
			return true;
		} else
			return false;
	}

	public boolean shouldShowDelete() {

		boolean show = true;

		if (tmpUser.getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString()))
			show = false;
		return show;
	}

	public List<String> getSelectedPracticesIds(Provider provider) {

		Set<Practice> tmpPracticeList = provider.getPracticeList();
		List<String> tmpSelectedPractice = new ArrayList<>();
		for (Practice practice : tmpPracticeList) {
			tmpSelectedPractice.add(String.valueOf(practice.getId()));
		}
		return tmpSelectedPractice;
	}

	private Set<Practice> getPracticeListByEntity(Integer id) {

		for (CEntitiy cEntitiy : entityList) {

			if (cEntitiy.getId().compareTo(id) == 0)
				return cEntitiy.getPracticeList();
		}
		return new HashSet<>();
	}

	public void updateClicked(Provider _provider) {

		// selectedPayers = getSelectedPayers(_provider);
		isEntityListDisabled = true;
		practiceList.clear();
		// practiceList.addAll(_provider.getCentity().getPracticeList());

		Set<Practice> tmpPractices = getPracticeListByEntity(_provider.getCentity().getId());
		practiceList.addAll(tmpPractices);

		// initHashTwo(practiceList);
	
		documentCells = _provider.getDocumentCellList();
		selectedPractices = getSelectedPracticesIds(_provider);
		provider = _provider;
		// practiceList = new
		// ArrayList<>(provider.getCentity().getPracticeList());
		// provider.setPracticeList(getSelectedList());
		this.opetationType = OperationType.Edit;
		setCurrentEntity(String.valueOf(_provider.getCentity().getId()));
	}

	
/*	  private List<Payer> getSelectedPayersObject() {
	  
	 return this.payerList.stream().filter( p -> {
		 
		 if(this.selectedPayers.contains(p.getId()))
			 return true;
		 else
		  return false;
	  }).collect(Collectors.toList());
	  
	  }*/
	 

	private Set<Practice> getSelectedList() {

		Set<Practice> list = new HashSet<>();

		CEntitiy entity = getEntityById(Integer.valueOf(currentEntity));
		Set<Practice> allPractices = entity.getPracticeList();

		for (String practiceId : selectedPractices) {

			for (Practice practice : practiceList) {

				if (practice.getId().compareTo(Integer.valueOf(practiceId)) == 0) {
					list.add(practice);
					break;
				}
			}
		}
		return list;
	}

	public boolean canEditProvider() {

		boolean isOK = true;

		if (tmpUser.getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString())
				&& !tmpUser.getPermission().isCanProviderEdit())
			isOK = false;

		// if
		// (SharedData.getSharedInstace().getCurrentUser().getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString())
		// &&
		// !SharedData.getSharedInstace().getCurrentUser().getPermission().isCanProviderEdit())
		// isOK = false;

		return isOK;
	}

	public boolean canCreateProvider() {

		boolean isOK = true;

		if (tmpUser.getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString())
				&& !tmpUser.getPermission().isCanProviderAdd())
			isOK = false;
		// if
		// (SharedData.getSharedInstace().getCurrentUser().getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString())
		// &&
		// !SharedData.getSharedInstace().getCurrentUser().getPermission().isCanProviderAdd())
		// isOK = false;

		return isOK;
	}

	public boolean isInfoValid() {

		boolean isOK = true;

		String error = providerService.isNameExist(provider.getFirstName(), provider.getLastName(),
				provider.getNpiNum());
		if (error != null) {
			isOK = false;
			showMessage(error);
		}

		return isOK;
	}

	public void saveInfo(ActionEvent event) {

		try {

			if (this.opetationType == OperationType.Create && !isInfoValid())
				return;

			CEntitiy entity = null;
			if (entityList.size() >= 1) {
				entity = getEntityById(Integer.valueOf(currentEntity));// entityList.get();
			}

//			List<Payer>payerList = getSelectedPayersObject();
			
			Set<Practice> tmpPractices = getSelectedList();
			provider.setPracticeList(tmpPractices);
//			practiseService.updatePractiseList(tmpPractices);
			provider.setCentity(entity);
//			provider.get
			switch (this.opetationType) {
			case Create: {
				{

					// currentEntity.getProviderList().add(provider);

					if (isInfoValid()) {
						try {
							providerService.addProviderToPractise(provider, tmpPractices);
							providerList.add(provider);

						} catch (StaleObjectStateException e) {

							showMessage(Constants.ERRR_RECORDS_OUDATED);
						} catch (Exception e) {
							// TODO: handle exception
						}

						// entityService.update(currentEntity);
						RequestContext.getCurrentInstance().execute("PF('Dlg1').hide()");
						// showMessage("Provider has been saved");
					}
				}
			}
				break;
			case Edit: {
				providerService.update(provider);
				// entityService.update(currentEntity);
				RequestContext.getCurrentInstance().execute("PF('Dlg1').hide()");
				// showMessage("Provider has been updated");
			}
				break;

			default:
				break;
			}

		} catch (StaleObjectStateException e) {

			showMessage(Constants.ERRR_RECORDS_OUDATED);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void removeClicked(Provider provider) {

		try {

//			int sz = provider.getPracticeList().size();

			// currentEntity.setPracticeList(provider.getPracticeList());
			// provider.setCentity(currentEntity);

			// providerService.update(provider);
			// entityService.update(currentEntity);
			providerService.remove(provider);
			providerList.remove(provider);
			refreshAllData();
			// entityService.update(currentEntity);

		} catch (StaleObjectStateException e) {

			showMessage(Constants.ERRR_RECORDS_OUDATED);
		} catch (Exception ex) {
		}
	}
	
	public String moveToEditView(Provider provider){
		
		return "EditProviderView";
	}

	public void createProviderClicked() {

		isEntityListDisabled = false;
//		selectedPayers = null;
		selectedPractices = null;
		provider = new Provider();
		this.opetationType = OperationType.Create;
		practiceList.clear();

		if (entityList.size() > 0) {
			CEntitiy entity = entityList.get(0);
			currentEntity = String.valueOf(entity.getId());
			// practiceList.addAll(entity.getPracticeList());
			refreshPractics();
		}

	documentCells = new ArrayList<>();
//		documentCells = provider.getDocumentCellList();
	}

	private void refreshPractics() {

		if (!entityList.isEmpty()) {
			setCurrentEntity(String.valueOf(entityList.get(0).getId()));

			if (SharedData.getSharedInstace().getCurrentUser().getRoleName()
					.equalsIgnoreCase(Role.ENTITY_USER.toString())) {
				practiceList = practiseService.fetchAllByRole();
			} else
				onEntityChange();
		}
	}

	private CEntitiy getEntityById(Integer id) {

		for (CEntitiy cEntitiy : entityList) {

			if (cEntitiy.getId().compareTo(id) == 0)
				return cEntitiy;
		}
		return null;
	}

	public void onEntityChange() {

		CEntitiy entity = getEntityById(Integer.valueOf(currentEntity));
		provider.setCentity(entity);
		practiceList = new ArrayList(entity.getPracticeList());
		// practiceList = new ArrayList<>(currentEntity.getPracticeList());
		// initHashTwo(practiceList);
		/*
		 * for (Practice practice : practiceList) {
		 * 
		 * 
		 * hashPractise.put(practice.getId(), practice); }
		 */
	}

	public String getExtension(String fileName){
		
		List<String> strList = new ArrayList<>(Arrays.asList(fileName.split("\\."))) ;
		
		return strList.get(strList.size()-1);
	}
	
	/**
	 * Upload Code
	 * 
	 * @param Called
	 *            when file is selected
	 */
	public void handleFileUpload(FileUploadEvent event) {

		UploadedFile file = event.getFile();
		DocumentCell obj =  (DocumentCell)event.getComponent().getAttributes().get("name");

		Document doc = (Document)obj.getDocument();//provider.getFilenameByType(docName);

		doc.setOrignalName(event.getFile().getFileName());
		doc.setNameOnDisk(provider.getId()+"_"+doc.getType()+"."+getExtension(file.getFileName()));
		doc.removeFileOnDisk();
		
		try {
			file.write(doc.getFullPath());
			FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
			providerService.addDocumentToProvider(provider, doc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteFileClicked(DocumentCell cell) {

		providerService.removeDocumentFromProvider(provider,cell.getDocument());
		documentCells = provider.getDocumentCellList();
		
//		for (DocumentCell tmpCell : documentCells) {
//			
//			System.out.println(tmpCell.getDocument().getOrignalName());
//		}
	}

	public void showMessage(String msg) {

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Provider", msg);
		RequestContext.getCurrentInstance().showMessageInDialog(message);
	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("");
	}
}
