package com.Beendo.Controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Plan;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.Transaction;
import com.Beendo.Services.IEntityService;
import com.Beendo.Services.ITransactionService;
import com.Beendo.Utils.Constants;
import com.Beendo.Utils.TransactionType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionViewModel {

	private enum RadioValue {
		
		Practise(1),Provider(2);
	    
	    private final Integer value;
	    private RadioValue(Integer val) {
	    	this.value = val;
		}
	}
	
	private IEntityService entityService;
	private ITransactionService transactionService;
	
	private List<Practice> practiceList = new ArrayList<>();
	private List<Payer> payerList = new ArrayList<>();
	private List<Provider> providerList = new ArrayList<>();
	
	private Integer currentPractice;
	private Integer currentProvider;
	private Integer selectedPayer;
	private Integer radioValue = RadioValue.Practise.value;
	
	//PLan
	private List<Integer> selectedPlanIds = new ArrayList<>();
	private List<Plan> currentPlanList = new ArrayList<>();
	
	private Transaction transaction = new Transaction();
	
	private Boolean canPracticeShow = true;
	
	private String[] statusList;
	
	public TransactionViewModel() {
		// TODO Auto-generated constructor stub
		
		init();
	}
	
	public TransactionViewModel(List<Practice> practList,List<Provider> providertList,List<Payer> payertList, IEntityService entityService, ITransactionService transactionService) {
		// TODO Auto-generated constructor stub
		this.practiceList = practList;
		this.providerList = providertList;
		this.payerList = payertList;
		this.entityService = entityService;
		this.transactionService = transactionService;
		
		init();
	}
	
	private void init(){
		canPracticeShow = true;
		updateProviderStatusList();
	}
	
	private void updateProviderStatusList(){
		
		if(radioValue == RadioValue.Practise.value)
		{
			statusList =  Constants.practiceStatus;
			canPracticeShow = true;
			transaction.setType(TransactionType.Practise.getValue());
		}
		else
		{
			statusList =  Constants.providerStatus;
			canPracticeShow = false;
			transaction.setType(TransactionType.Provider.getValue());
		}
	}
	
	//edit case
	public TransactionViewModel reloadForEdit(){
		
//		TransactionViewModel transViewModel = new TransactionViewModel();
		if (transaction != null) {
//			transViewModel.transaction = res;
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
			
			if (transaction.getPlans() != null) {
				
				Set<Plan> tmpPlans = new HashSet<>(transaction.getPayer().getPlans());
				currentPlanList = new ArrayList<>(tmpPlans);
				selectedPlanIds = transaction.getPlans().stream().map(Plan::getId).collect(Collectors.toList());
			}		
		}
		return this;
	}
	//create case
	public void onSelectionChange()
	{
		updateProviderStatusList();
	}

	public void onPayerChange() {
		
		Optional<Payer> res = payerList.stream().filter( (p) -> p.getId() == selectedPayer).findFirst();
		if (res.isPresent()) {
			
			updatePlansList(res.get());
		}
	}
	private void updatePlansList(Payer sender){
		
		currentPlanList =  new ArrayList(sender.getPlans());
	}
	
	public void onPlanSelect(){
		System.out.println(currentPlanList);
		System.out.println(selectedPlanIds);
	}
	
	public void saveButtonClicked() throws Exception {
		
		Optional<Payer> payer =	payerList.stream()
				.filter(p -> {
					
					if(p.getId() == selectedPayer)
						return true;
					else 
						return false;
				}).findFirst();
				
				
//				Set<Payer> payers = new HashSet<Payer>(selectedPayers);
			if ( !payer.isPresent()) {
				throw new Exception("Must select payer");
			
			}
			
			
			List<Plan> res = currentPlanList.stream().filter(
					(p) -> {
						
						boolean ok = selectedPlanIds.contains(p.getId());
						return ok;
					}).collect(Collectors.toList());
			if (res.isEmpty()) {
				throw new Exception("Must select atleast one plan");
//				showMessage(FacesMessage.SEVERITY_ERROR, "Transaction", "Must select atleast one plan");
//				return;
			}

				transaction.setPayer(payer.get());
				
				Set<Plan> planSet = new HashSet<>();
				planSet.addAll(res);
				
				transaction.setPlans(planSet);
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
				
				 
				CEntitiy entity = entityService.get(entityId);//entityService.findEntityWithTransaction(entityId);
				transaction.setEntity(entity);
				
				transactionService.saveOrUpdate(transaction);
	}
	
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
	
}
