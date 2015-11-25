package com.Beendo.Controllers;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Practice;
import com.Beendo.Services.EntityService;
import com.Beendo.Services.PractiseService;
import com.Beendo.Utils.OperationType;
import com.Beendo.Utils.SharedData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Controller
public class PractiseController extends RootController {

	private OperationType operationType;

	private String entityName;
	private List<CEntitiy> listEntities;
	private List<Practice> listPractise;

	private CEntitiy currentEntity;

	@Autowired
	private PractiseService practiseService;

	@Autowired
	private EntityService entityService;

	private Practice practise = new Practice();

	public String viewPractise() {

		listEntities = entityService.getAllEntities();
		listPractise = practiseService.fetchAll();
		
		initHashOne(listEntities);
		initHashTwo(listPractise);
		// return "Practise/PractiseView?faces-redirect=true";
		return "PractiseView";
	}

	/*
	 * public void showCreatePractiseView(ActionEvent event) { Map<String,
	 * Object> options = new HashMap<String, Object>(); options.put("resizable",
	 * false); options.put("modal", true);
	 * 
	 * 
	 * 
	 * RequestContext.getCurrentInstance().openDialog("Practise/createPractise",
	 * options, null); // return "EntityView"; }
	 */

	/*
	 * public void onDialogReturn(SelectEvent event) {
	 * 
	 * Object rating = event.getObject();
	 * 
	 * FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
	 * "You rated the book with " + rating, null);
	 * 
	 * FacesContext.getCurrentInstance().addMessage(null, message);
	 * 
	 * }
	 */

	public void initNewPractise() {

		practise = new Practice();
	}

	public void createClicked() {

		this.operationType = OperationType.Create;
		initNewPractise();
	}

	public void updateClicked(Practice sender) {

		practise = sender;
		this.operationType = OperationType.Edit;
	}

	public void createEditLogic(ActionEvent event) {

		switch (operationType) {
		case Create: {
			List<Practice> result = practiseService.isNameExist(listPractise, practise.getName());
			if (result.size() <= 0) {
				currentEntity.getPracticeList().add(practise);
				practise.setEntity(currentEntity);
				entityService.update(currentEntity);
				// practiseService.save(practise);
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

	public void remove(Practice sender) {

		listPractise.remove(sender);
		practiseService.remove(sender);

	}

	public void showMessage() {

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "What we do in life",
				"Echoes in eternity.");
		RequestContext.getCurrentInstance().showMessageInDialog(message);
	}
}