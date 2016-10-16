package com.Beendo.Controllers;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Plan;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.Transaction;
import com.Beendo.Services.IEntityService;
import com.Beendo.Services.ITransactionService;
import com.Beendo.Utils.Constants;
import com.Beendo.Utils.OperationType;
import com.Beendo.Utils.Role;
import com.Beendo.Utils.SharedData;
import com.github.javaplugs.jsf.SpringScopeView;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SpringScopeView
@Controller
public class CreateTransactionController extends BaseViewController implements Serializable {

	private enum RadioValue {
		
		Practise(1),Provider(2);
	    
	    private final Integer value;
	    private RadioValue(Integer val) {
	    	this.value = val;
		}
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5981716634583291159L;

	private Boolean isEditMode;
	
	private Integer currentPractice;
	private Integer currentProvider;
	
	private List<Practice> practiceList = new ArrayList<>();
	private List<Payer> payerList = new ArrayList<>();
	private List<Provider> providerList = new ArrayList<>();
	private Integer selectedPayer;
	
	private Integer radioValue = RadioValue.Practise.value;
	private Boolean canPracticeShow = true;
	
	private String[] statusList;
	private Transaction transaction = new Transaction();
	
	//PLan
	private Integer selectedPlanId;
	private List<Plan> currentPlanList = new ArrayList<>();
	
	@Autowired
	private IEntityService entityService;
	@Autowired
	private ITransactionService transactionService;
	
	public void onLoad(){
				
		canPracticeShow = true;		
		fetchData();
		getIdIfPresent();
	}
	
	public void fetchData(){
		
		Integer entityId = SharedData.getSharedInstace().getCurrentUser().getEntity().getId();
		
		Consumer<Map<String, Object>> response = (Map<String, Object> sender) -> {
			
			this.payerList = (List<Payer>) sender.get("payerList");
			this.practiceList = (List<Practice>) sender.get("practiceList");
			this.providerList = (List<Provider>) sender.get("providerList");
		};
		
		transactionService.fetchDataForTransactionCreation(response, entityId);
	}
	
	public void getIdIfPresent() {

		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("id");
		if (id != null) {
			Transaction res = transactionService.get(Integer.parseInt(id));
			if (res != null) {
				transaction = res;
				selectedPayer = transaction.getPayer().getId();
				if (transaction.getPractice() != null)
				{
					currentPractice = transaction.getPractice().getId();
					radioValue = RadioValue.Practise.value;
				}
				if (transaction.getProvider() != null )
				{
					radioValue = RadioValue.Provider.value;
					currentProvider =  transaction.getProvider().getId();
				}
				updateProviderStatusList();
				selectedPlanId = transaction.getPlan().getId();
			}
		}
	}
	
//	public void updateClicked(ProviderTransaction _transaction)
//	{
//		//currentPayer = getPayerById(_transaction.getPayer().getId());
////		List<Payer> prlist = new ArrayList();
//		selectedPayers.clear();
//		for (Payer payer : _transaction.getPayerList()) {
//			
////			prlist.add(getPayerById(payer.getId(), _transaction.getPayerList()));
//			selectedPayers.add(payer.getId());
//		}
//		
////		selectedPayers = prlist;
//		
//		if(_transaction.getPractice() != null)
//		{
//			currentPractice = _transaction.getPractice().getId();
////			currentPractice = getPractiseById(_transaction.getPractice().getId());
//			currentRadio = "rbPractice";
//			canPracticeShow = true;
//		}
//		else
//		{
//			currentProvider = _transaction.getProvider().getId();
//			currentRadio = "rbProvider";
//			canPracticeShow = false;
//		}
//		
//		updateProviderStatusList();
//		transaction = _transaction;
//		isEditMode = true;
//	}
//	
	
	private Payer getPayerById(Integer id, Set<Payer>payerlist) {
		
	Optional<Payer> payer =	payerlist.stream().
		filter(p -> p.getId().compareTo(id) == 0)
		.findFirst();
		
	if(payer.isPresent())
		return payer.get();
	else
		return null;
		// TODO Auto-generated method stub
		
	}
	
	public void onSelectionChange()
	{
		updateProviderStatusList();
	}
	
	private void updateProviderStatusList(){
		
		if(radioValue == RadioValue.Practise.value)
		{
			statusList =  Constants.practiceStatus;
			canPracticeShow = true;
		}
		else
		{
			statusList =  Constants.providerStatus;
			canPracticeShow = false;
		}
	}
	
	/*public void onProviderChange()
	{
		payerList.clear();
		payerList.addAll(currentProvider.getPayerList());
	}*/
	
	/*public void onPracticeChange()
	{
		Set<Payer> spayer = new HashSet<Payer>();
		
		for (Provider pro : currentPractice.getProviders()) {
			
			spayer.addAll(pro.getPayerList());
		}
		
		payerList = new ArrayList(spayer);
		
		initHashThree(payerList);
		
	}*/
	
	private Practice getPractiseById(Integer id) {
		
		Optional<Practice> res = practiceList.stream().
		filter(p -> p.getId().compareTo(id) == 0).findFirst();
		
		if(res.isPresent())
			return res.get();
		else
		return null;
	}

	private Provider getProviderById(Integer id) {
		
		Optional<Provider> res = providerList.stream().
		filter(p -> p.getId().compareTo(id) == 0).findFirst();
		
		if(res.isPresent())
			return res.get();
		else
		return null;
	}
	
	public void onPayerChange() {
		
		Optional<Payer> res = payerList.stream().filter( (p) -> p.getId() == selectedPayer).findFirst();
		if (res.isPresent()) {
			
			updatePlansList(res.get());
		}
	}
	
	private void updatePlansList(Payer sender){
		
		currentPlanList = sender.getPlans();
	}
	
	public void saveInfo()
	{
		//transaction.setPayer(currentPayer);
		
	Optional<Payer> payer =	payerList.stream()
		.filter(p -> {
			
			if(p.getId() == selectedPayer)
				return true;
			else 
				return false;
		}).findFirst();
		
		
//		Set<Payer> payers = new HashSet<Payer>(selectedPayers);
	if ( !payer.isPresent()) {
		return;
	}
	
	Optional<Plan> res = currentPlanList.stream().filter( (p) -> p.getId() == selectedPlanId).findFirst();
	if (!res.isPresent()) {
		return;
	}

		transaction.setPayer(payer.get());
		transaction.setPlan(res.get());
		Integer entityId = null;
		if(canPracticeShow)
		{
			Practice practice = getPractiseById(currentPractice);
			transaction.setPractice(practice);
			transaction.setProvider(null);
			entityId = practice.getEntity().getId();
		}
		else
		{
			Provider provider = getProviderById(currentProvider);
			transaction.setProvider(provider);
			transaction.setPractice(null);
			entityId = provider.getCentity().getId();
		}
		
		 
		CEntitiy entity = entityService.findEntityWithTransaction(entityId);
		transaction.setEntity(entity);
		
		transactionService.saveOrUpdate(transaction);
		try
		{
			
//			if(isEditMode)
			{		
//				transactionService.update(transaction);
				//entityService.update(entity);
				//showMessage("Transaction has been updated");
			}
//			else
			{
//				entity.getTransactionList().add(transaction);
//				transactionService.saveOrUpdate(transaction);
//				entityService.update(entity);
				//showMessage("Transaction has been saved");
			}
		}
		catch(Exception ex){
			
			System.out.println(ex);
		}		
	}
}
