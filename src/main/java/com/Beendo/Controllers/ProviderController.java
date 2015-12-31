package com.Beendo.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.ProviderTransaction;
import com.Beendo.Services.EntityService;
import com.Beendo.Services.PayerService;
import com.Beendo.Services.PractiseService;
import com.Beendo.Services.ProviderService;
import com.Beendo.Services.TransactionService;
import com.Beendo.Utils.OperationType;
import com.Beendo.Utils.Role;
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
	
	@Autowired
	private PractiseService practiseService;
	
	@Autowired
	private TransactionService transactionService;
	private List<ProviderTransaction> transactions;

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
		practiceList = practiseService.fetchAllByRole();// new ArrayList(SharedData.getSharedInstace().getCurrentUser().getEntity().getPracticeList());

		transactions = transactionService.fetchAllByRole();
		
		if (!entityList.isEmpty()) {
			setCurrentEntity(entityList.get(0));
			onEntityChange();
		}
		initHashOne(entityList);
		initHashFour(providerList);
		initHashThree(payerList);
		initHashTwo(practiceList);

		return "ProviderView";
	}
	
	public String getFirstEntityName(){
		
		if(entityList.isEmpty())
			return "No Entity Exist";
		else
			return entityList.get(0).getName();
	}
	
	public boolean isSingleItemInEntityList(){
		
		if(entityList.size() <= 1)
		{
			return true;
		}
		else
			return false;
	}

	public void updateClicked(Provider _provider) {
		
		//selectedPayers = getSelectedPayers(_provider);
		selectedPractices = getSelectedPractices(_provider);
		provider = _provider;
		//practiceList = new ArrayList<>(provider.getCentity().getPracticeList());
		// provider.setPracticeList(getSelectedList());
		this.opetationType = OperationType.Edit;
	}
	
	/*private List<Payer> getSelectedPayers(Provider _provider)
	{
		List<Payer> list = new ArrayList();
		
		for (Payer payer : _provider.getPayerList()) {
			
			list.add(getPayerById(payer.getId()));
		}
		
		return list;
	}*/
	
	private List<Practice> getSelectedPractices(Provider _provider)
	{
		List<Practice> mlist = new ArrayList();
		Set<Practice> list = new HashSet<Practice>();
		
		for (Practice prac : _provider.getPracticeList()) {
			
			Practice tprac = getPractiseById(prac.getId());
			
			if(tprac != null)
				list.add(tprac);
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
	
	public boolean canEditProvider(){
		
		boolean isOK = true;
	
		if (SharedData.getSharedInstace().getCurrentUser().getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString()) &&
				!SharedData.getSharedInstace().getCurrentUser().getPermission().isCanProviderEdit())
			isOK = false;
		
		return isOK;
	}
	
	public boolean canCreateProvider(){
		
		boolean isOK = true;
	
		if (SharedData.getSharedInstace().getCurrentUser().getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString()) &&
				!SharedData.getSharedInstace().getCurrentUser().getPermission().isCanProviderAdd())
			isOK = false;
		
		return isOK;
	}

	public boolean isInfoValid(){
		
		boolean isOK = true;
		
		String error = providerService.isNameExist(provider.getFirstName(), provider.getLastName(), provider.getNpiNum());
		if(error != null)
		{
			isOK = false;
			showMessage(error);
		}
		
		return isOK;
	}
	
	public void saveInfo(ActionEvent event) {
		
			if(currentEntity == null && entityList.size() == 1)
			{
				currentEntity = entityList.get(0);
			}	
		
			provider.setPracticeList(new HashSet<>(selectedPractices));
			for (Practice practise : selectedPractices) {

				try
				{
					practise.getProviders().add(provider);
					practiseService.update(practise);
				}
				catch(Exception ex){}
			}
			
			//currentEntity.setPracticeList(provider.getPracticeList());			
			provider.setCentity(currentEntity);
			
			switch (this.opetationType) {
			case Create: {
				{
					
					//currentEntity.getProviderList().add(provider);
					
					if(isInfoValid())
					{
						providerList.add(provider);
						providerService.save(provider);
						entityService.update(currentEntity);
						RequestContext.getCurrentInstance().execute("PF('Dlg1').hide()");
						//showMessage("Provider has been saved");
					}
				} 
			}
				break;
			case Edit: {
				providerService.update(provider);
				//entityService.update(currentEntity);
				RequestContext.getCurrentInstance().execute("PF('Dlg1').hide()");
				//showMessage("Provider has been updated");
			}
				break;

			default:
				break;
			}

	}

	
	public void removeClicked(Provider provider) {
		
		try
		{
						
			for (ProviderTransaction providerTransaction : transactions) {
				
				if(providerTransaction.getProvider() != null && provider.getId() == providerTransaction.getProvider().getId())
				{
					providerTransaction.setProvider(null);
					transactionService.update(providerTransaction);
				}			
				
			}
			
			//currentEntity.setPracticeList(provider.getPracticeList());			
			//prov.setCentity(currentEntity);
			
//			prov.getPracticeList().clear();
			Set<Practice> practiceList = provider.getPracticeList();
			for (Practice practise : practiceList) {
				
				practise.getProviders().remove(provider);
//				provider.getPracticeList().remove(practise);
				
//				practiseService.update(practise);
			}
			
			practiseService.updatePractiseList(practiceList);
			
			int sz = provider.getPracticeList().size();
			
			currentEntity.setPracticeList(provider.getPracticeList());
			provider.setCentity(currentEntity);
			
//			providerService.update(provider);
			entityService.update(currentEntity);
			providerService.delete(provider);
			
			providerList.remove(provider);
			entityService.update(currentEntity);
			
		}
		catch(Exception ex)
		{}
	}
	
	
	public void clearData() {
		selectedPayers = null;
		selectedPractices = null;
		provider = new Provider();
		this.opetationType = OperationType.Create;
	}

	public void onEntityChange() {
		provider.setCentity(currentEntity);
		practiceList = new ArrayList(currentEntity.getPracticeList());
		//practiceList = new ArrayList<>(currentEntity.getPracticeList());
		//initHashTwo(practiceList);
		/*
		 * for (Practice practice : practiceList) {
		 * 
		 * 
		 * hashPractise.put(practice.getId(), practice); }
		 */
	}

	public void showMessage(String msg) {

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Provider", msg);
		RequestContext.getCurrentInstance().showMessageInDialog(message);
	}

}
