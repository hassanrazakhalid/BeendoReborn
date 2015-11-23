package com.Beendo.Controllers;

import java.util.List;

import javax.faces.application.FacesMessage;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Payer;
import com.Beendo.Services.PayerService;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
public class PayerController {

	@Autowired
	private PayerService payerService;
	
	private Payer payer = new Payer();
	private List<Payer> payers;
	private Boolean isEditMode;
	
	public String view()
	{
		payers = payerService.findAll();
		return "PayerView";
	}
	
	public void updateClicked(Payer _payer)
	{
		payer = _payer;
		isEditMode = true;
	}
	
	public void saveInfo()
	{
		if(isEditMode)
		{
			payerService.update(payer);
			showMessage("Payer has been updated");
		}
		else
		{
			List<Payer> result = payerService.isNameExist(payers, payer.getName(), payer.getPlanName(), payer.getCity(), payer.getState(), payer.getZip(), payer.getStreet());
			if(result.size() <= 0)
			{
				payers.add(payer);
				payerService.save(payer);
				showMessage("Payer has been saved");
			}
			else
				showMessage("Payer info already exists!");
		}
	}

	
	public void clearData()
	{
		payer = new Payer();
		isEditMode = false;
	}
	
	public void showMessage(String msg) {
		
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Payer", msg);     
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }
	
}
