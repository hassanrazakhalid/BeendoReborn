package com.Beendo.Controllers;

import java.util.List;

import javax.faces.application.FacesMessage;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

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
	
	public String view()
	{
		payers = payerService.findAll();
		return "PayerView";
	}

	
	public void createPressed()
	{
		payers.add(payer);
		payerService.save(payer);
		showMessage("Entity has been saved");
		clearData();
	}
	
	
	public void editPressed()
	{
		payerService.update(payer);
		showMessage("Entity has been updated");
		clearData();
		
	}
	
	public void clearData()
	{
		payer = new Payer();
	}
	
	public void showMessage(String msg) {
		
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Entity", msg);     
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }
	
}
