package com.Beendo.Controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.event.ActionEvent;

import org.hibernate.StaleObjectStateException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.DegreeInfo;
import com.Beendo.Entities.Email;
import com.Beendo.Entities.FaxNumber;
import com.Beendo.Entities.Language;
import com.Beendo.Entities.PhoneNumber;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.Speciality;
import com.Beendo.Services.IPractiseService;
import com.Beendo.Services.IProviderService;
import com.Beendo.Utils.Constants;
import com.Beendo.Utils.OperationType;
import com.Beendo.Utils.Role;
import com.Beendo.Utils.SharedData;
import com.github.javaplugs.jsf.SpringScopeView;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Controller
@SpringScopeView
public class CreateProviderController extends BaseViewController {

	private String entityName;
	private List<CEntitiy> entityList = new ArrayList<>();;

	private List<Practice> practiceList = new ArrayList<>();
	private List<String> selectedPractices = new ArrayList<>();
	
	private String currentEntity;
	private OperationType opetationType;
	
	private Provider provider = new Provider();
	@Autowired
	private IPractiseService practiseService;
	@Autowired
	private IProviderService providerService;
	
	private boolean isEntityListDisabled;
	
	private boolean skip;
	private List<Email> emailsList = new ArrayList<>();
	private List<PhoneNumber> phoneList = new ArrayList<>();
	private List<FaxNumber> faxList = new ArrayList<>();
	private List<DegreeInfo> degreeList = new ArrayList<>();
	private List<Speciality> specilityList = new ArrayList<>();
	private List<Language> languageList = new ArrayList<>();
	
	private List<String> primarySpeciality = new ArrayList<>();
	private List<String> nameOfBoards = new ArrayList<>();
	
	public boolean getIsEntityListDisabled() {

		return isEntityListDisabled;
	}
	
	public void onLoad() {

		refreshAllData();
	}
	
	private void refreshAllData() {
		
		primarySpeciality.add("Specility 1");
		primarySpeciality.add("Specility 2");
		primarySpeciality.add("Specility 2");
		
		nameOfBoards.add("Board 1");
		nameOfBoards.add("Board 2");
		nameOfBoards.add("Board 3");
		
		this.entityList = entityList;
		
		specilityList.add(new Speciality());
		
		emailsList.clear();
		emailsList.add(new Email());
		
		degreeList.clear();
		degreeList.add(new DegreeInfo());
	}
	
	public void addEmailClicked(){	
		emailsList.add(new Email());
	}
	public void removeEmailClicked(Email sender){
		emailsList.remove(sender);
	}
	
	public void addPhoneClicked(){	
		phoneList.add(new PhoneNumber());
	}
	public void removePhoneClicked(PhoneNumber sender){
		phoneList.remove(sender);
	}
	
	public void addFaxClicked(){	
		faxList.add(new FaxNumber());
	}
	public void removeFaxClicked(FaxNumber sender){
		faxList.remove(sender);
	}
	
	public void addOtherDegreeClicked(){	
		degreeList.add(new DegreeInfo());
	}
	public void removeOtherDegree(DegreeInfo sender){
		degreeList.remove(sender);
	}
	
	public void addOtherSpecilityClicked(){	
		specilityList.add(new Speciality());
	}
	public void removeOtherSpecility(Speciality sender){
		specilityList.remove(sender);
	}
	
	public void addLanguageClicked(){	
		languageList.add(new Language());
	}
	public void removeLanguage(Language sender){
		languageList.remove(sender);
	}
	
	public void createProviderClicked() {

//		isEntityListDisabled = false;
////		selectedPayers = null;
//		selectedPractices = null;
//		provider = new Provider();
//		this.opetationType = OperationType.Create;
//		practiceList.clear();
//
//		if (entityList.size() > 0) {
//			CEntitiy entity = entityList.get(0);
//			currentEntity = String.valueOf(entity.getId());
//			// practiceList.addAll(entity.getPracticeList());
//			refreshPractics();
//		}
//
//	documentCells = new ArrayList<>();
////		documentCells = provider.getDocumentCellList();
	}
	
	public String onFlowProcess(FlowEvent event) {
        if(skip) {
            skip = false;   //reset in case user goes back
            return "confirm";
        }
        else {
            return event.getNewStep();
        }
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
	
//		documentCells = _provider.getDocumentCellList();
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

	public boolean isInfoValid() {

		boolean isOK = true;

		String error = providerService.isNPIExist(provider.getNpiNum());
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
//							providerList.add(provider);

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
}
