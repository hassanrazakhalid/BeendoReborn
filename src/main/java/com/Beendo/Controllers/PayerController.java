package com.Beendo.Controllers;

import java.util.List;

import javax.faces.application.FacesMessage;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Payer;
import com.Beendo.Services.PayerService;
import com.Beendo.Utils.OperationType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
public class PayerController extends RootController {

	@Autowired
	private PayerService payerService;

	private Payer payer = new Payer();
	private List<Payer> payers;
	private OperationType operationType;

	public String view() {
		payers = payerService.findAll();

		initHashThree(payers);
		return "PayerView";
	}

	public void updateClicked(Payer _payer) {
		payer = _payer;
		operationType = OperationType.Edit;
	}

	public void saveInfo() {

		switch (this.operationType) {
		case Create: {

			List<Payer> result = payerService.isNameExist(payers, payer.getName(), payer.getPlanName(), payer.getCity(),
					payer.getState(), payer.getZip(), payer.getStreet());
			if (result.size() <= 0) {
				payers.add(payer);
				payerService.save(payer);
				RequestContext.getCurrentInstance().execute("PF('Dlg1').hide()");
				//showMessage("Payer has been saved");
			} else
				showMessage("Payer info already exists!");
		}
			break;
		case Edit: {
			payerService.update(payer);
			RequestContext.getCurrentInstance().execute("PF('Dlg1').hide()");
			//showMessage("Payer has been updated");
		}
			break;

		default:
			break;
		}
	}

	public void clearData() {
		payer = new Payer();
		operationType = OperationType.Create;
	}

	public void showMessage(String msg) {

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Payer", msg);
		RequestContext.getCurrentInstance().showMessageInDialog(message);
	}

}
