package com.Beendo.Controllers;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Practice;
import com.Beendo.Services.EntityService;
import com.Beendo.Services.PractiseService;
import com.Beendo.Utils.SharedData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Controller
public class PractiseController {

	private String entityName;
	private List<CEntitiy> listEntities;
	private List<Practice> listPractise;

	private CEntitiy currentEntity;
	
	private boolean isEditMode;

	@Autowired
	private PractiseService practiseService;
	
	@Autowired
	private EntityService entityService;
	
	private Practice practise = new Practice();

	public String viewPractise() {

/*		listEntities = new ArrayList<String>();
		listEntities.add("ABV");
		listEntities.add("XYZ");
*/
		listEntities = entityService.getAllEntities(); 
		listPractise = practiseService.fetchAll();
		
		/*listPractise = new ArrayList<Practice>();

		Practice tmpPractise = new Practice();
		tmpPractise.setId(1);
		tmpPractise.setName("KOKO");

		listPractise.add(tmpPractise);

		tmpPractise = new Practice();
		tmpPractise.setId(2);
		tmpPractise.setName("PKPK");

		listPractise.add(tmpPractise);
*/		
		SharedData.getSharedInstace().addPactiseList(listPractise);
		
		return "Practise/PractiseView?faces-redirect=true";
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

		isEditMode = false;
		initNewPractise();
	}

	public void updateClicked(Practice sender) {

		practise = sender;
		isEditMode = true;
	}
	
	public void createEditLogic() {

		if (isEditMode == false) {
	
			List<Practice> result =	practiseService.isNameExist(listPractise, practise.getName());
			if(result.size() <= 0)
			{
				currentEntity.getPracticeList().add(practise);
				practise.setEntity(currentEntity);
				entityService.update(currentEntity);
//				practiseService.save(practise);
				listPractise.add(practise);
				initNewPractise();				
			}
			else
			{
				
			}
		} else {
			
			practiseService.update(practise);
			FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage("Saved Sucessfully"));
		}
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