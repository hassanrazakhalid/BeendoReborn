package com.Beendo.Controllers;

import java.io.Serializable;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.hibernate.StaleObjectStateException;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Plan;
import com.Beendo.Entities.Practice;
import com.Beendo.Services.IPayerService;
import com.Beendo.Services.IPlanService;
import com.Beendo.Utils.Constants;
import com.Beendo.Utils.OperationType;
import com.github.javaplugs.jsf.SpringScopeView;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Controller
@SpringScopeView
public class CreatePayerController extends BaseViewController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8336317299374169267L;
	private Payer payer = new Payer();
	private OperationType operationType = OperationType.Create;
	@Autowired
	private IPayerService payerService;

	@Autowired
	private IPlanService planService;
	
	
	private String previousPayerName;

	public void onLoad() {

		getIdIfPresent();
	}

	public void getIdIfPresent() {

		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("id");

		if (id != null) {

			Payer res = payerService.get(Integer.parseInt(id));
			if (res != null) {
				payer = res;
				previousPayerName = payer.getName();
				operationType = OperationType.Edit;
			}
		}
	}

	public void addPlanClicked() {

		payer.addNewPlan();
	}

	public void removePlan(Plan sender) {

		if (sender.getId() != null){	
			planService.remove(sender);
		}
		payer.getPlans().remove(sender);
	}

	private boolean shouldCheckForDuplicateName() {

		boolean shouldCheck = true;

		if (operationType == OperationType.Edit &&
			previousPayerName.equalsIgnoreCase(payer.getName())) {

			shouldCheck = false;
		}
		return shouldCheck;
	}

	public void saveInfo() {

		try {

			if (shouldCheckForDuplicateName()) {
				boolean result = payerService.isNameExist(payer.getName());
				if (result) {

					showMessage("Payer name already exists!");
					return;
				}
			}

			payerService.saveOrUpdate(payer);
			showMessage("Success");

			// case Edit: {
			// payerService.update(payer);
			// RequestContext.getCurrentInstance().execute("PF('Dlg1').hide()");
			// showMessage("Payer has been updated");
			// }
			// break;

		} catch (StaleObjectStateException e) {

			// showMessage(Constants.ERRR_RECORDS_OUDATED);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
