package com.Beendo.Controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.FlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.PracticeAddressInfo;
import com.Beendo.Services.IEntityService;
import com.Beendo.Services.IPractiseService;
import com.Beendo.Utils.OperationType;
import com.Beendo.Utils.SharedData;
import com.github.javaplugs.jsf.SpringScopeView;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Controller
@SpringScopeView
public class CreatePracticeController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8962330758522000597L;
	private Practice practice = new Practice();
	private Integer selectedEntityId;
	private List<CEntitiy> entityList = new ArrayList<>();

	@Autowired
	private IEntityService entityService;
	@Autowired
	private IPractiseService practiseService;
	private List<String> stateList = new ArrayList<>();

	private OperationType type = OperationType.Create;
	private boolean skip;

	public void onLoad() {

		fetchData();
		getIdIfPresent();
	}

	private void fetchData() {

		Integer entityId = SharedData.getSharedInstace().getCurrentUser().getEntity().getId();
		entityService.getEntityWithPractises(entityId, (entityList) -> {

			this.entityList = entityList;

			if (!entityList.isEmpty()) {
				selectedEntityId = entityList.get(0).getId();
			}
		});
	}

	public void getIdIfPresent() {

		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("id");

		if (id != null) {

			Practice res = practiseService.get(Integer.parseInt(id));

			if (res != null) {
				practice = res;
				selectedEntityId = res.getEntity().getId();
				type = OperationType.Edit;
			}

			// practiseService.getProviderDetailsNoFiles(Integer.parseInt(id),
			// (p) -> {
			//
			// if (p != null) {
			// this.provider = p;
			// List<Integer> ids =
			// p.getPracticeList().stream().map(Practice::getId).collect(Collectors.toList());
			// selectedPractices.addAll(ids);
			// }
			//
			// });
		}

	}

	public boolean isEntityListDisabled(){
		
	  if ( type == OperationType.Edit) {
		  return true;
	  }
	  else
		  return false;
	  
	}
	
	public boolean shouldShowList() {
		if (entityList.size() <= 1) {

			return false;
		} else
			return true;
	}

	public void addServiceAddress() {
		practice.getPracticeAddress().getServiceAddress().add(new PracticeAddressInfo());
	}

	public void removeServiceAddress(PracticeAddressInfo sender) {
		practice.getPracticeAddress().getServiceAddress().remove(sender);
	}

	public void addBillingAddress() {
		practice.getPracticeAddress().getBillingAddress().add(new PracticeAddressInfo());
	}

	public void removeBillingAddress(PracticeAddressInfo sender) {
		practice.getPracticeAddress().getBillingAddress().remove(sender);
	}

	public String onFlowProcess(FlowEvent event) {
		if (skip) {
			skip = false; // reset in case user goes back
			return "confirm";
		} else {
			return event.getNewStep();
		}
	}

	private Optional<CEntitiy> getEntityById(Integer id) {

		Optional<CEntitiy> result = getEntityList().stream().filter((p) -> {

			if (p.getId() == selectedEntityId) {

				return true;
			} else {

				return false;
			}
		}).findFirst();

		return result;
	}

	public void saveButtonClicked() {

		Optional<CEntitiy> result = getEntityById(selectedEntityId);

		if (result.isPresent()) {
			CEntitiy entity = result.get();
			if (practice.getEntity() == null) {

				practice.setEntity(entity);
				entity.getPracticeList().add(practice);
				entityService.saveOrUpdate(entity);
			}
			else {
				
				practiseService.saveOrUpdate(practice);
			}
		

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Sucessfully Saved."));
		}

	}
}
