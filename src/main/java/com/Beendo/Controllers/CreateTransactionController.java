package com.Beendo.Controllers;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.print.attribute.standard.Severity;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Plan;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.Transaction;
import com.Beendo.Entities.User;
import com.Beendo.Services.IEntityService;
import com.Beendo.Services.ITransactionService;
import com.Beendo.Utils.Constants;
import com.Beendo.Utils.DateUtil;
import com.Beendo.Utils.OperationType;
import com.Beendo.Utils.ReportType;
import com.Beendo.Utils.Role;
import com.Beendo.Utils.SharedData;
import com.Beendo.Utils.TransactionType;
import com.github.javaplugs.jsf.SpringScopeView;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SpringScopeView
@Controller
public class CreateTransactionController extends BaseViewController implements Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5981716634583291159L;

//	private Boolean isEditMode;
	

	
	private List<Practice> practiceList = new ArrayList<>();
	private List<Payer> payerList = new ArrayList<>();
	private List<Provider> providerList = new ArrayList<>();
	private User tmpUser;
	
	List<TransactionViewModel> transactionViewModelList = new ArrayList<>();
//	List<TransactionViewModel> createdTransactionViewModelList = new ArrayList<>();
	
	@Autowired
	private IEntityService entityService;
	@Autowired
	private ITransactionService transactionService;
	
	private OperationType operationType = OperationType.Create;
	
	@PostConstruct
	public void onLoad(){
				
	
		fetchData();
		getIdIfPresent();
		checkIfProviderPresent();
		checkIfPractisePresent();
	}
	
	public Date getMaxDate(){
		return new Date();
//		return DateUtil.addDays(new Date(), 1);
	} 
	
	public void fetchData(){
		
		Integer entityId = SharedData.getSharedInstace().getCurrentUser().getEntity().getId();
		
		Consumer<Map<String, Object>> response = (Map<String, Object> sender) -> {
			
			this.payerList = (List<Payer>) sender.get("payerList");
			this.practiceList = (List<Practice>) sender.get("practiceList");
			this.providerList = (List<Provider>) sender.get("providerList");
			this.tmpUser = (User)sender.get("user");
		};
		
		transactionService.fetchDataForTransactionCreation(response, entityId);
	}
	
	public void addTransactionClicked(){
		
		transactionViewModelList.add(new TransactionViewModel(practiceList, providerList, payerList, entityService, transactionService));
	}
	
	public void removeTransactionClicked(TransactionViewModel sender){
	
		transactionViewModelList.remove(sender);
	}
	
	public void getIdIfPresent() {

		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("id");
		if (id != null) {
			
			transactionViewModelList.clear();
			
			operationType = OperationType.Edit;
			List<String> profiles = Arrays.asList("transactionPlanList","planList");
			Transaction res = transactionService.getEntityByProfiles(Integer.parseInt(id), profiles); //transactionService.get(Integer.parseInt(id));
			TransactionViewModel transactionViewMode = new TransactionViewModel(practiceList, providerList, payerList, entityService, transactionService);
			transactionViewMode.setTransaction(res);
			transactionViewMode.reloadForEdit();
			transactionViewModelList.add(transactionViewMode);
//				updateProviderStatusList();			
			}
	}
	
	private void checkIfPractisePresent(){
		
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("practiseId");
		
		if (id != null){
			
			transactionViewModelList.clear();
			
			TransactionViewModel transactionViewMode = new TransactionViewModel(practiceList, providerList, payerList, entityService, transactionService);
			transactionViewMode.setCurrentPractice(Integer.parseInt(id));
			transactionViewMode.onSelectionChange();
			transactionViewModelList.add(transactionViewMode);
		}
	}
	
	private void checkIfProviderPresent(){
		
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("providerId");
		
		if (id != null){
			
			transactionViewModelList.clear();
			
			TransactionViewModel transactionViewMode = new TransactionViewModel(practiceList, providerList, payerList, entityService, transactionService);
			transactionViewMode.setCurrentProvider(Integer.parseInt(id));
			transactionViewMode.setRadioValue(2);
			transactionViewMode.onSelectionChange();
			transactionViewModelList.add(transactionViewMode);
		}
	}
	
	public boolean shouldRenderAddButton(){
		return operationType == OperationType.Create ? true : false;
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
	


	private boolean areAllTransactionsUnique() {
		
		boolean isOK = true;
		
		if (!transactionViewModelList.isEmpty()) {
			
			for (int i=0; i<transactionViewModelList.size();i++) {
				List<Integer> fixedIds = transactionViewModelList.get(i).getUniqueTransactionString();
				for (int j=i+1; j<transactionViewModelList.size();j++) {
					List<Integer> changingIds = transactionViewModelList.get(j).getUniqueTransactionString();
					
					Optional<Integer> result = fixedIds.stream().filter( x -> changingIds.contains(x)).findFirst();
					if (result.isPresent())
						return false;
				}
			}
		}
		return isOK;
	}

	public void saveInfo()
	{
		//transaction.setPayer(currentPayer);

		if (!areAllTransactionsUnique()) {
			showMessage(FacesMessage.SEVERITY_INFO, "Transaction", "Transactions must be unique");
			return;
		}
		
		try {
			
		List<Transaction> transactionList =	new ArrayList<>();
		
		
		for (TransactionViewModel transactionViewModel : transactionViewModelList) {
			
			
				transactionList.addAll(transactionViewModel.getActiveTransctions());
			}

		for (TransactionViewModel transactionViewModel : transactionViewModelList) {				
			transactionViewModel.setIsDisabled(true);	
			}
		
		transactionService.saveTransactions(transactionList);
		showMessage(FacesMessage.SEVERITY_INFO, "Transaction", "Transaction saved successfully");
			
		} catch (Exception e) {
			
			showMessage(FacesMessage.SEVERITY_ERROR, "Transaction", e.getMessage());
//			showMessage(FacesMessage.SEVERITY_ERROR, "Transaction", "Must select atleast one plan");
		}
		
//		transactionService.saveOrUpdate(transaction);
		
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
