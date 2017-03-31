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
	
	private Boolean isDisabled = false;
	
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
	
	public List<Integer> getUniqueTransactionString() {
		
		String finalString = "";
		
		if (currentProvider != null) {
			finalString += RadioValue.Provider.value;
			finalString += currentProvider;
		}
		else if (currentPractice != null) {
			
			finalString += RadioValue.Practise.value;
			finalString += currentPractice;
		}
		
		if (selectedPayer != null) {
			finalString += selectedPayer;
		}
		
		List<Integer> uniqueTransactionsIds = new ArrayList<>();
		if (selectedPlanIds != null) {
			
			for (Integer planId : selectedPlanIds) {
				
				
				Integer expectedId = Integer.parseInt(finalString + planId);
				if (expectedId != null) {
					uniqueTransactionsIds.add(expectedId);	
				}
			}	
		}
		return uniqueTransactionsIds;
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
			currentProvider = null;
		}
		else
		{
			statusList =  Constants.providerStatus;
			canPracticeShow = false;
			transaction.setType(TransactionType.Provider.getValue());
			currentPractice = null;
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
			
			if (transaction.getPlan() != null) {
				
				Set<Plan> tmpPlans = new HashSet<>(transaction.getPayer().getPlans());
				currentPlanList = new ArrayList<>(tmpPlans);
				selectedPlanIds.add(transaction.getPlan().getId());
//				selectedPlanId = transaction.getPlan().getId();
//				selectedPlanIds = transaction.getPlans().stream().map(Plan::getId).collect(Collectors.toList());
			}
			updateProviderStatusList();
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
	
	public List<Plan> getSelectedPlansList(){
		
		List<Plan> res = currentPlanList.stream().filter(
				(p) -> {
					
					boolean ok = selectedPlanIds.contains(p.getId());
					return ok;
				}).collect(Collectors.toList());
		return res;
	}
	
	public Transaction assignValuesToTransaction(Transaction sender) throws Exception {
		
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
			
//			List<Plan> res = getSelectedPlansList();
//			if (res.isEmpty()) {
//				throw new Exception("Must select plan");
//			}
			
//			sender.setPlan(res.get());	
			sender.setPayer(payer.get());
				
				
				Integer entityId = null;
				CEntitiy entity = null;
				if(canPracticeShow)
				{
					Practice practice = getPractiseById(currentPractice);
					sender.setPractice(practice);
					sender.setProvider(null);
//					entityId = practice.getEntity().getId();
					entity = practice.getEntity();
				}
				else
				{
					Provider provider = getProviderById(currentProvider);
					sender.setProvider(provider);
					sender.setPractice(null);
//					entityId = provider.getCentity().getId();
					entity = provider.getCentity();
				}
				
				 
//				CEntitiy entity = entityService.get(entityId);
				sender.setEntity(entity);	
				return sender;
//				transactionService.saveOrUpdate(transaction);
	}
	
	public List<Transaction> getTransactionListByPlan() throws Exception{
		
		List<Plan> res = getSelectedPlansList();
		if (res.isEmpty()) {
			throw new Exception("Must select plan");
		}
		List<Transaction> finalList = new ArrayList<>();

		for (Plan plan : res) {
			
			Transaction tmpTransaction = new Transaction();
			tmpTransaction.setComments(transaction.getComments());
			tmpTransaction.setParStatus(transaction.getParStatus());
			tmpTransaction.setType(transaction.getType());
			tmpTransaction.setTransactionDate(transaction.getTransactionDate());
			tmpTransaction.setTransactionState(transaction.getTransactionState());
			tmpTransaction.setPlan(plan);
			assignValuesToTransaction(tmpTransaction);
			finalList.add(tmpTransaction);
		}
		return finalList;
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
