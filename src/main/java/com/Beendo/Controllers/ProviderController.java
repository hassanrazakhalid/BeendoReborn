package com.Beendo.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import com.Beendo.Utils.CustomArrayList;

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

	private List<Practice> practiceList = new ArrayList<>();
	private List<Practice> selectedPractices =  new ArrayList<>();

	private List<Payer> payerList;

	private List<CEntitiy> entityList;

	private CEntitiy currentEntity;
	private OperationType opetationType;

	public String view() {
		providerList = providerService.findAll();
		entityList = entityService.getAllEntities();
		payerList = payerService.findAll();

		if (!entityList.isEmpty()) {
			setCurrentEntity(entityList.get(0));
			onEntityChange();
		}
		initHashOne(entityList);
		initHashTwo(new ArrayList<>(currentEntity.getPracticeList()));
		initHashFour(providerList);
		initHashThree(payerList);

		return "ProviderView";
	}

	public void updateClicked(Provider _provider) {
		provider = _provider;
//		practiceList = new ArrayList<>();
//		provider.getPracticeList().toArray()
		
		practiceList.clear();
		for (Practice practise : _provider.getPracticeList() ) {
			
			selectedPractices.add(getPractiseById(practise.getId()));
			
		}
		
		practiceList.addAll(getAllHashTwo());
//	selectedPractices =	updateListForEdit(practiceList,selectedPractices);
		// provider.setPracticeList(getSelectedList());
		this.opetationType = OperationType.Edit;
	}
	
	public ArrayList<Practice> updateListForEdit(List<Practice> fromList, List<Practice> toList )
	{
		CustomArrayList<Practice> toCustomList = new CustomArrayList<>();
		toCustomList.addAll(fromList);
		for (Practice superPractice : fromList) {
			
			for (Practice practice : toList) {
				
				if(!toCustomList.contains(superPractice))
				{
					toCustomList.add(superPractice);
				}
			}			
		}
		
		return new ArrayList<Practice>(toCustomList);
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
		provider.setPracticeList(new HashSet<>(selectedPractices));
		for (Practice practise : selectedPractices) {

			practise.getProviders().add(provider);
		}
		
		provider.setCentity(currentEntity);
		switch (this.opetationType) {
		case Create: {
			List<Provider> result = providerService.isNameExist(providerList, provider.getName(), provider.getNpiNum());
			if (result.size() <= 0) {
				providerList.add(provider);
				providerService.save(provider);
				showMessage("Provider has been saved");
			} else
				showMessage("Provider name or npi already exists!");
		}
			break;
		case Edit: {
			providerService.update(provider);
			showMessage("Provider has been updated");
		}
			break;

		default:
			break;
		}
	}

	public void clearData() {
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
