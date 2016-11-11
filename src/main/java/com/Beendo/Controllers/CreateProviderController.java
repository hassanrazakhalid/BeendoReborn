package com.Beendo.Controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.hibernate.StaleObjectStateException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Beendo.Dto.SlotCell;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.DegreeInfo;
import com.Beendo.Entities.Email;
import com.Beendo.Entities.FaxNumber;
import com.Beendo.Entities.Language;
import com.Beendo.Entities.PhoneNumber;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.Slot;
import com.Beendo.Entities.Speciality;
import com.Beendo.Entities.SpecialityInfo;
import com.Beendo.Services.IEntityService;
import com.Beendo.Services.IPractiseService;
import com.Beendo.Services.IProviderService;
import com.Beendo.Utils.Constants;
import com.Beendo.Utils.OperationType;
import com.Beendo.Utils.Role;
import com.Beendo.Utils.SharedData;
import com.Beendo.Utils.Weekdays;
import com.github.javaplugs.jsf.SpringScopeView;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Controller
@SpringScopeView
public class CreateProviderController extends BaseViewController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2518375996063241487L;
	private Integer selectedEntityId;
	private List<CEntitiy> entityList = new ArrayList<>();;

	private List<Practice> practiceList = new ArrayList<>();
	private List<Integer> selectedPractices = new ArrayList<>();

	private String currentEntity;
	private OperationType opetationType;

	private Provider provider = new Provider();
	@Autowired
	private IPractiseService practiseService;
	@Autowired
	private IProviderService providerService;
	@Autowired
	private IEntityService entityService;

	private boolean isEntityListDisabled;

	private boolean updateScenario = false;
	
	private boolean skip;
	// private List<Email> emailsList = new ArrayList<>();
	// private List<PhoneNumber> phoneList = new ArrayList<>();
	// private List<FaxNumber> faxList = new ArrayList<>();
	// private List<DegreeInfo> degreeList = new ArrayList<>();
	// private List<Speciality> specilityList = new ArrayList<>();
	// private List<Language> languageList = new ArrayList<>();

	// List to select
	private List<SpecialityInfo> specialityList = new ArrayList<>();
//	private List<BoardInfo> boardList = new ArrayList<>();

	private List<SlotCell> slotCells = new ArrayList<>();
	// private List<String> days = new ArrayList<String>();

	public boolean getIsEntityListDisabled() {

		return isEntityListDisabled;
	}

	public void onLoad() {

		refreshAllData();
	}

	private void refreshAllData() {

		Integer entityId = SharedData.getSharedInstace().getCurrentUser().getEntity().getId();
		entityService.getEntityWithPractisesAndBoardSpeciality(entityId, (map) -> {

			this.entityList = (List<CEntitiy>) map.get("arg1");
			this.specialityList = (List<SpecialityInfo>) map.get("arg2");
//			this.boardList = (List<BoardInfo>) map.get("arg3");
			this.practiceList.clear();

			if (!entityList.isEmpty()) {

				this.practiceList.addAll(entityList.get(0).getPracticeList());
				selectedEntityId = entityList.get(0).getId();
			}
		});

		getIdIfPresent();
		slotCells.addAll(provider.getOtherInfo().getSlotCells());
		// for (Weekdays day : Weekdays.values()) {
		//
		// days.add(day.toString());
		// }

		// emailsList.clear();
		// emailsList.add(new Email());

		// degreeList.add(new DegreeInfo());

		// providerService.enterDummy();
	}

	public void getIdIfPresent() {

		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("id");

		if (id != null) {

			providerService.getProviderDetailsNoFiles(Integer.parseInt(id), (p) -> {

				if (p != null) {
					updateScenario = true;
					this.provider = p;
					List<Integer> ids = p.getPracticeList().stream().map(Practice::getId).collect(Collectors.toList());
					selectedPractices.addAll(ids);
				}

			});
		}

	}

	public void onEntityChange() {

		practiceList.clear();
		Optional<CEntitiy> res = entityList.stream().filter((p) -> {

			if (p.getId() == selectedEntityId) {
				return true;
			} else {
				return false;
			}

		}).findFirst();

		if (res.isPresent()) {
			practiceList.addAll(res.get().getPracticeList());
		}

	}

	public void addEmailClicked() {
		provider.getEmails().add(new Email());
	}

	public void removeEmailClicked(Email sender) {
		provider.getEmails().remove(sender);
	}

	public void addPhoneClicked() {
		provider.getPhoneNumbers().add(new PhoneNumber());
	}

	public void removePhoneClicked(PhoneNumber sender) {
		provider.getPhoneNumbers().remove(sender);
	}

	public void addFaxClicked() {
		provider.getFaxNumbers().add(new FaxNumber());
	}

	public void removeFaxClicked(FaxNumber sender) {
		provider.getFaxNumbers().remove(sender);
	}

	public void addOtherDegreeClicked() {
		provider.getQualitication().getOtherDegrees().add(new DegreeInfo());
	}

	public void removeOtherDegree(DegreeInfo sender) {
		provider.getQualitication().getOtherDegrees().remove(sender);
	}

	public void addOtherSpecilityClicked() {
		provider.getQualitication().getOtherSpecialities().add(new Speciality());
	}

	public void removeOtherSpecility(Speciality sender) {
		provider.getQualitication().getOtherSpecialities().remove(sender);
	}

	public void addLanguageClicked() {
		provider.getOtherInfo().getLanguagesList().add(new Language());
	}

	public void removeLanguage(Language sender) {
		provider.getOtherInfo().getLanguagesList().remove(sender);
	}

	public void createProviderClicked() {

		// isEntityListDisabled = false;
		//// selectedPayers = null;
		// selectedPractices = null;
		// provider = new Provider();
		// this.opetationType = OperationType.Create;
		// practiceList.clear();
		//
		// if (entityList.size() > 0) {
		// CEntitiy entity = entityList.get(0);
		// currentEntity = String.valueOf(entity.getId());
		// // practiceList.addAll(entity.getPracticeList());
		// refreshPractics();
		// }
		//
		// documentCells = new ArrayList<>();
		//// documentCells = provider.getDocumentCellList();
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

	// public List<String> getSelectedPracticesIds(Provider provider) {
	//
	// Set<Practice> tmpPracticeList = provider.getPracticeList();
	// List<String> tmpSelectedPractice = new ArrayList<>();
	// for (Practice practice : tmpPracticeList) {
	// tmpSelectedPractice.add(String.valueOf(practice.getId()));
	// }
	// return tmpSelectedPractice;
	// }

	// private Set<Practice> getPracticeListByEntity(Integer id) {
	//
	// for (CEntitiy cEntitiy : entityList) {
	//
	// if (cEntitiy.getId().compareTo(id) == 0)
	// return cEntitiy.getPracticeList();
	// }
	// return new HashSet<>();
	// }

	// public void updateClicked(Provider _provider) {
	//
	// // selectedPayers = getSelectedPayers(_provider);
	// isEntityListDisabled = true;
	// practiceList.clear();
	// // practiceList.addAll(_provider.getCentity().getPracticeList());
	//
	// Set<Practice> tmpPractices =
	// getPracticeListByEntity(_provider.getCentity().getId());
	// practiceList.addAll(tmpPractices);
	//
	// // initHashTwo(practiceList);
	//
	//// documentCells = _provider.getDocumentCellList();
	// selectedPractices = getSelectedPracticesIds(_provider);
	// provider = _provider;
	// // practiceList = new
	// // ArrayList<>(provider.getCentity().getPracticeList());
	// // provider.setPracticeList(getSelectedList());
	// this.opetationType = OperationType.Edit;
	// setCurrentEntity(String.valueOf(_provider.getCentity().getId()));
	// }

	/*
	 * private List<Payer> getSelectedPayersObject() {
	 * 
	 * return this.payerList.stream().filter( p -> {
	 * 
	 * if(this.selectedPayers.contains(p.getId())) return true; else return
	 * false; }).collect(Collectors.toList());
	 * 
	 * }
	 */

	private List<Practice> getSelectedList() {

		List<Practice> res = practiceList.stream().filter((p) -> {

			if (selectedPractices.contains(p.getId())) {
				return true;
			} else {
				return false;
			}
		}).collect(Collectors.toList());
		return res;
	}

	public boolean isInfoValid() {

		boolean isOK = true;

		String error = providerService.isNPIExist(provider.getNpiNum());
		if (error != null) {
			isOK = false;
			showMessage(FacesMessage.SEVERITY_ERROR, "Provider", "Provider already exist with this NPI");
		}

		return isOK;
	}

//	private BoardInfo getBoardById(Integer id) {
//
//		Optional<BoardInfo> res = this.boardList.stream().filter((s) -> {
//
//			if (s.getId() == id) {
//
//				return true;
//			} else {
//				return false;
//			}
//		}).findFirst();
//		return res.get();
//	}

	private SpecialityInfo getSpecilityById(Integer id) {

		Optional<SpecialityInfo> res = this.specialityList.stream().filter((s) -> {

			if (s.getId() == id) {

				return true;
			} else {
				return false;
			}
		}).findFirst();
		return res.get();
	}

	private void assignSpecialityValues() {

		Integer specId = provider.getQualitication().getPrimarySpeciality().getSpecialityInfo().getId();

//		BoardInfo boardInfo = getBoardById(boardId);
		SpecialityInfo specInfo = getSpecilityById(specId);

		if (specInfo != null) {
			provider.getQualitication().getPrimarySpeciality().setSpecialityInfo(specInfo);
		}

		for (Speciality spec : provider.getQualitication().getOtherSpecialities()) {

			specInfo = getSpecilityById(spec.getSpecialityInfo().getId());
			if (specInfo != null) {

				spec.setSpecialityInfo(specInfo);
//				spec.setBoardInfo(boardInfo);
			}

		}

	}

	public void saveInfo(ActionEvent event) {

		// providerService.createNewProvider(provider);
		for (SlotCell cell : slotCells) {

			Slot slot = cell.getSlot();

			if (slot.getEndTime() != null && slot.getStartTime() != null) {
				provider.getOtherInfo().getSlots().put(cell.getDay(), slot);
			}
		}
		provider.getPracticeList().clear();
		provider.getPracticeList().addAll(getSelectedList());

		provider.setCentity(getEntityById(selectedEntityId));

		assignSpecialityValues();
		providerService.saveOrUpdate(provider);
		showMessage(FacesMessage.SEVERITY_INFO, "Provider", "Provider saved sucessfully");
		return;

		// try {
		//
		// if (this.opetationType == OperationType.Create && !isInfoValid())
		// return;
		//
		// CEntitiy entity = null;
		// if (entityList.size() >= 1) {
		// entity = getEntityById(Integer.valueOf(currentEntity));//
		// entityList.get();
		// }
		//
		//// List<Payer>payerList = getSelectedPayersObject();
		//
		// Set<Practice> tmpPractices = getSelectedList();
		// provider.setPracticeList(tmpPractices);
		//// practiseService.updatePractiseList(tmpPractices);
		// provider.setCentity(entity);
		//// provider.get
		// switch (this.opetationType) {
		// case Create: {
		// {
		//
		// // currentEntity.getProviderList().add(provider);
		//
		// if (isInfoValid()) {
		// try {
		// providerService.addProviderToPractise(provider, tmpPractices);
		//// providerList.add(provider);
		//
		// } catch (StaleObjectStateException e) {
		//
		// showMessage(Constants.ERRR_RECORDS_OUDATED);
		// } catch (Exception e) {
		// // TODO: handle exception
		// }
		//
		// // entityService.update(currentEntity);
		// RequestContext.getCurrentInstance().execute("PF('Dlg1').hide()");
		// // showMessage("Provider has been saved");
		// }
		// }
		// }
		// break;
		// case Edit: {
		// providerService.update(provider);
		// // entityService.update(currentEntity);
		// RequestContext.getCurrentInstance().execute("PF('Dlg1').hide()");
		// // showMessage("Provider has been updated");
		// }
		// break;
		//
		// default:
		// break;
		// }
		//
		// } catch (StaleObjectStateException e) {
		//
		// showMessage(Constants.ERRR_RECORDS_OUDATED);
		// } catch (Exception e) {
		// // TODO: handle exception
		// }
	}

	// private void refreshPractics() {
	//
	// if (!entityList.isEmpty()) {
	// setCurrentEntity(String.valueOf(entityList.get(0).getId()));
	//
	// if (SharedData.getSharedInstace().getCurrentUser().getRoleName()
	// .equalsIgnoreCase(Role.ENTITY_USER.toString())) {
	// practiceList = practiseService.fetchAllByRole();
	// } else
	// onEntityChange();
	// }
	// }

	private CEntitiy getEntityById(Integer id) {

		if (id == -1) {
			return entityService.get(1);
		} else {
			for (CEntitiy cEntitiy : entityList) {

				if (cEntitiy.getId().compareTo(id) == 0)
					return cEntitiy;
			}
			return null;
		}

	}

	// public void onEntityChange() {
	//
	// CEntitiy entity = getEntityById(Integer.valueOf(currentEntity));
	// provider.setCentity(entity);
	// practiceList = new ArrayList(entity.getPracticeList());
	// // practiceList = new ArrayList<>(currentEntity.getPracticeList());
	// // initHashTwo(practiceList);
	// /*
	// * for (Practice practice : practiceList) {
	// *
	// *
	// * hashPractise.put(practice.getId(), practice); }
	// */
	// }

	public String onFlowProcess(FlowEvent event) {
		if (skip) {
			skip = false; // reset in case user goes back
			return "confirm";
		} else {
			return event.getNewStep();
		}
	}
}
