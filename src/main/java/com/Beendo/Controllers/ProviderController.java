package com.Beendo.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.Beendo.Utils.OperationType;
import com.Beendo.Utils.SharedData;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
public class ProviderController extends RootController {

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
	private List<Payer> selectedPayers;

	private List<CEntitiy> entityList;

	private CEntitiy currentEntity;
	private OperationType opetationType;

	public String view() {
		providerList = providerService.fetchAllByRole(); //providerService.fetchAllByUser();
		entityList = entityService.fetchAllByRole();
		payerList = payerService.findAll();

		if (!entityList.isEmpty()) {
			setCurrentEntity(entityList.get(0));
			onEntityChange();
		}
		initHashOne(entityList);
		initHashFour(providerList);
		initHashThree(payerList);

		return "ProviderView";
	}

	public void updateClicked(Provider _provider) {
		
		selectedPayers = getSelectedPayers(_provider);
		selectedPractices = getSelectedPractices(_provider);
		provider = _provider;
		//practiceList = new ArrayList<>(provider.getCentity().getPracticeList());
		// provider.setPracticeList(getSelectedList());
		this.opetationType = OperationType.Edit;
	}
	
	private List<Payer> getSelectedPayers(Provider _provider)
	{
		List<Payer> list = new ArrayList();
		
		for (Payer payer : _provider.getPayerList()) {
			
			list.add(getPayerById(payer.getId()));
		}
		
		return list;
	}
	
	private List<Practice> getSelectedPractices(Provider _provider)
	{
		List<Practice> mlist = new ArrayList();
		Set<Practice> list = new HashSet<Practice>();
		
		for (Practice prac : _provider.getPracticeList()) {
			
			list.add(getPractiseById(prac.getId()));
		}
		
		mlist.addAll(list);
		
		return mlist;
	}

	private List<Practice> getSelectedList() {
		List<Practice> list = new ArrayList();

		for (Practice practice : provider.getCentity().getPracticeList()) {

			for (Practice prac : provider.getPracticeList()) {

				if (prac.getId() == practice.getId())
					list.add(practice);
			}
		}
		return list;
	}

	public void saveInfo() {
		
		provider.setPayerList(selectedPayers);
		provider.setPracticeList(new HashSet<>(selectedPractices));
		for (Practice practise : selectedPractices) {

			practise.getProviders().add(provider);
		}
		
		provider.setCentity(currentEntity);
		
		switch (this.opetationType) {
		case Create: {
			List<Provider> result = providerService.isNameExist(providerList, provider.getName(), provider.getNpiNum());
			if (result.size() <= 0) {
				
				//currentEntity.getProviderList().add(provider);
				providerList.add(provider);
				providerService.save(provider);
				entityService.update(currentEntity);
				RequestContext.getCurrentInstance().execute("PF('Dlg1').hide()");
				//showMessage("Provider has been saved");
			} 
			else
				showMessage("Provider name or npi already exists!");
		}
			break;
		case Edit: {
			providerService.update(provider);
			entityService.update(currentEntity);
			RequestContext.getCurrentInstance().execute("PF('Dlg1').hide()");
			//showMessage("Provider has been updated");
		}
			break;

		default:
			break;
		}
	}

	public void clearData() {
		selectedPayers = null;
		selectedPractices = null;
		provider = new Provider();
		this.opetationType = OperationType.Create;
	}

	public void onEntityChange() {
		provider.setCentity(currentEntity);
		practiceList = new ArrayList<>(currentEntity.getPracticeList());
		initHashTwo(practiceList);
		/*
		 * for (Practice practice : practiceList) {
		 * 
		 * 
		 * hashPractise.put(practice.getId(), practice); }
		 */
	}

	public void showMessage(String msg) {

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Provider", msg);
		RequestContext.getCurrentInstance().showMessageInDialog(message);
	}

}
