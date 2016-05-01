package com.Beendo.Controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.hibernate.StaleObjectStateException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Beendo.Dto.DocumentCell;
import com.Beendo.Entities.Document;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Services.EditProviderCallBack;
import com.Beendo.Services.IProviderService;
import com.Beendo.Utils.Constants;
import com.github.javaplugs.jsf.SpringScopeView;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
//@Scope(value = "session")
@SpringScopeView
public class EditProviderController {
	
	@Autowired
	private IProviderService providerService;


//	@Autowired
//	private IPractiseService practiseService;

	private String entityName;

	private String npiNumber;
	private Date expireDate;
	private Date effectiveDate;
	private Date reminderDate;
	
	private Provider provider = new Provider();
	private List<Provider> providerList = new ArrayList<>();

	private List<Practice> practiceList = new ArrayList<>();
	private List<Integer> selectedPractices = new ArrayList<>();

//	private String currentEntity;

	private List<DocumentCell> documentCells = new ArrayList<>();

//	private User tmpUser;

	private void refreshAllData() {

//		refreshPractics();
		
		Map<String, String> params =FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap();
		String parameterOne = params.get("id");
		
		documentCells = new ArrayList<>();
		
		EditProviderCallBack response = (Provider providerDetails)->{
			
			this.provider = providerDetails;
			
			this.npiNumber = providerDetails.getNpiNum();
			this.entityName = providerDetails.getCentity().getName();
			this.practiceList = new ArrayList<>(providerDetails.getCentity().getPracticeList());
			documentCells = providerDetails.getDocumentCellList();
			List<Integer> selectedIds = providerDetails.getPracticeList().stream()
					.map(Practice::getId)
					.collect(Collectors.toList());
			this.selectedPractices = selectedIds;
			
//			this.tmpUser = user;
//			this.providerList = providerList;
//			this.entityList = entityList;
//			this.payerList = payerList;
//			this.transactions = transactions;
			
		};
		providerService.getAllProviderInfo(Integer.valueOf(parameterOne),response);
	}

	public void onLoad() {

		refreshAllData();
	}

	public String view() {

		return "ProviderView";
	}

	public List<String> getSelectedPracticesIds(Provider provider) {

		Set<Practice> tmpPracticeList = provider.getPracticeList();
		List<String> tmpSelectedPractice = new ArrayList<>();
		for (Practice practice : tmpPracticeList) {
			tmpSelectedPractice.add(String.valueOf(practice.getId()));
		}
		return tmpSelectedPractice;
	}

	public void updateClicked(Provider _provider) {

		// selectedPayers = getSelectedPayers(_provider);
		practiceList.clear();
		// practiceList.addAll(_provider.getCentity().getPracticeList());

		Set<Practice> tmpPractices = null;//getPracticeListByEntity(_provider.getCentity().getId());
		practiceList.addAll(tmpPractices);

		// initHashTwo(practiceList);
	
		documentCells = _provider.getDocumentCellList();
//		selectedPractices = getSelectedPracticesIds(_provider);
		provider = _provider;
		// practiceList = new
		// ArrayList<>(provider.getCentity().getPracticeList());
		// provider.setPracticeList(getSelectedList());
//		setCurrentEntity(String.valueOf(_provider.getCentity().getId()));
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

	private Set<Practice> getSelectedList() {

		Set<Practice> list = new HashSet<>();

//		CEntitiy entity = null;//getEntityById(Integer.valueOf(currentEntity));
		Set<Practice> allPractices = provider.getCentity().getPracticeList();

		for (Integer practiceId : selectedPractices) {

			for (Practice practice : allPractices) {

				if (practice.getId().compareTo(practiceId) == 0) {
					list.add(practice);
					break;
				}
			}
		}
		return list;
	}

	public boolean isInfoValid() {

		boolean isOK = true;

		if(this.npiNumber.equalsIgnoreCase(provider.getNpiNum()))
			return true;
		
		String error = providerService.isNameExist(provider.getFirstName(), provider.getLastName(),
				provider.getNpiNum());
		if (error != null) {
			isOK = false;
			showMessage(FacesMessage.SEVERITY_ERROR,error);
		}

		return isOK;
	}

	public String actionSaveInfo(){
		
		saveInfo(null);
		return null;
	}
	
	public void saveInfo(ActionEvent event) {

		try {

			if (!isInfoValid())
				return;

			Set<Practice> tmpPractices = getSelectedList();
			provider.setNpiNum(npiNumber);
			provider.setPracticeList(tmpPractices);
//			practiseService.updatePractiseList(tmpPractices);
//			provider.setCentity(entity);

			providerService.update(provider);
			showMessage(FacesMessage.SEVERITY_INFO,"Saved Succesfully");
			// entityService.update(currentEntity);			

		} catch (StaleObjectStateException e) {

			showMessage(FacesMessage.SEVERITY_ERROR,Constants.ERRR_RECORDS_OUDATED);
		} catch (Exception e) {
			
			showMessage(FacesMessage.SEVERITY_ERROR,e.toString());
			// TODO: handle exception
		}
	}

//	private void refreshPractics() {
//
//		if (!entityList.isEmpty()) {
//			setCurrentEntity(String.valueOf(entityList.get(0).getId()));
//
//			if (SharedData.getSharedInstace().getCurrentUser().getRoleName()
//					.equalsIgnoreCase(Role.ENTITY_USER.toString())) {
//				practiceList = practiseService.fetchAllByRole();
//			} else
//				onEntityChange();
//		}
//	}

//	private CEntitiy getEntityById(Integer id) {
//
//		for (CEntitiy cEntitiy : entityList) {
//
//			if (cEntitiy.getId().compareTo(id) == 0)
//				return cEntitiy;
//		}
//		return null;
//	}

	public String getExtension(String fileName){
		
		List<String> strList = new ArrayList<>(Arrays.asList(fileName.split("\\."))) ;
		
		return strList.get(strList.size()-1);
	}
	
	 public void onDateSelect(DocumentCell docCell){
		 
		 Document doc = docCell.getDocument();
		 
		 doc.updateReminderCount();
//		 System.out.println("");
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

	public void showMessage(Severity type,String msg) {

//		FacesMessage message = new FacesMessage(type, "Provider", msg);
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(type, msg, null));

	}
}
