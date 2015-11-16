package com.Beendo.Controllers;

import java.util.List;

import javax.faces.application.FacesMessage;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Services.EntityService;
import com.Beendo.Services.PayerService;
import com.Beendo.Services.ProviderService;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
public class ProviderController {

	@Autowired
	private ProviderService providerService;
	
	@Autowired
	private PayerService payerService;
	
	@Autowired
	private EntityService entityService;

	private String entityName;
	
	private Provider provider = new Provider();
	private List<Provider> providerList;
	
	private List<Practice> practiceList;
	private List<Practice> selectedPractices;
	
	private List<Payer> payerList;
	private List<Provider> selectedPayers;
	
	private List<CEntitiy> entityList;
	private List<CEntitiy> entitySel;
	
	private CEntitiy currentEntity;
	private Boolean isEditMode;
	
	public String view()
	{
		providerList = providerService.findAll();
		entityList = entityService.getAllEntities();
		payerList = payerService.findAll();
		
		return "ProviderView";
	}
	
	public void updateClicked(Provider _provider)
	{
		provider = _provider;
		isEditMode = true;
	}
	
	public void saveInfo()
	{
		
		if(isEditMode)
		{
			providerService.update(provider);
			showMessage("Provider has been updated");
		}
		else
		{
			Provider p = provider;
			providerList.add(provider);
			providerService.save(provider);
			showMessage("Provider has been saved");
		}
	}

	
	public void clearData()
	{
		provider = new Provider();
		isEditMode = false;
	}
	
	
	public void onEntityChange()
	{
		provider.setCentity(currentEntity);
		practiceList = currentEntity.getPracticeList();
	}
	
	
	public void showMessage(String msg) {
		
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Provider", msg);     
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }
	
}
