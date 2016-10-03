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
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;

import org.hibernate.StaleObjectStateException;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Beendo.Dto.LazyTransactionModel;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.ProviderTransaction;
import com.Beendo.Entities.User;
import com.Beendo.Services.IEntityService;
import com.Beendo.Services.ITransactionCallback;
import com.Beendo.Services.ITransactionService;
import com.Beendo.Utils.Constants;
import com.Beendo.Utils.Role;
import com.Beendo.Utils.SharedData;
import com.github.javaplugs.jsf.SpringScopeView;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
//@Scope(value="session")
@SpringScopeView
public class TransactionController implements DisposableBean, Serializable{
	
/**
	 * 
	 */
	private static final long serialVersionUID = -6178498762662211117L;

	//	extends RootController
	@Autowired
	private ITransactionService transactionService;

	private LazyTransactionModel lazyModel;

	
	@Autowired
	private IEntityService entityService;
	

	
	private ProviderTransaction transaction = new ProviderTransaction();
	private List<ProviderTransaction> transactions = new ArrayList<>();
	private Boolean isEditMode;
	
	private Integer currentPractice;
	private Integer currentProvider;
	
	private List<Practice> practiceList = new ArrayList<>();
	private List<Payer> payerList = new ArrayList<>();
	private List<Provider> providerList = new ArrayList<>();
	private List<Integer> selectedPayers = new ArrayList<>();
	
	private String currentRadio;
	private Boolean canPracticeShow = true;
	
	private String[] statusList;
	
	private User tmpUser;

	private List<Integer> payerFilter = new ArrayList<>();
	private List<ProviderTransaction> filterTransactions = new ArrayList<>();
	private List<ProviderTransaction> realTimefilterList = new ArrayList<>();
	//private HashMap<Integer, Practice> _hash = new HashMap<Integer, Practice>();
	
	private void refreshAllData(){
		
		Integer entityId = SharedData.getSharedInstace().getCurrentUser().getEntity().getId();

		transactionService.getLatestTransactions(entityId);
		
		ITransactionCallback callBack = (User user, List<ProviderTransaction>transactions, List<Payer>payerList, List<Practice>practiceList, List<Provider>providerList, Map<String,Object>otherInfo)->{
			
			tmpUser = user;
			this.lazyModel = new LazyTransactionModel(transactionService,entityId);
			this.lazyModel.setRowCount((Integer)otherInfo.get("count"));
//			this.lazyModel.setRowCount(20);
//			this.transactions = transactions;
			this.payerList = payerList;
			this.practiceList = practiceList;
			this.providerList = providerList;
			this.filterTransactions.clear();
			this.filterTransactions.addAll(transactions);
		};

		transactionService.refreshAllData(0, 10, entityId, callBack);
		currentRadio = "rbPractice";
		canPracticeShow = true;		
	}
	
	public void onLoad(){

		refreshAllData();
	}
	
	public boolean shouldShowDelete(){
		
		boolean show = true;
		
		if(tmpUser.getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString()))
			show = false;
		return show;
	}
	
	public String view()
	{

		onLoad();
		
		return "ViewTransaction";
	}
	
	public void updateClicked(ProviderTransaction _transaction)
	{
		//currentPayer = getPayerById(_transaction.getPayer().getId());
//		List<Payer> prlist = new ArrayList();
		selectedPayers.clear();
		for (Payer payer : _transaction.getPayerList()) {
			
//			prlist.add(getPayerById(payer.getId(), _transaction.getPayerList()));
			selectedPayers.add(payer.getId());
		}
		
//		selectedPayers = prlist;
		
		if(_transaction.getPractice() != null)
		{
			currentPractice = _transaction.getPractice().getId();
//			currentPractice = getPractiseById(_transaction.getPractice().getId());
			currentRadio = "rbPractice";
			canPracticeShow = true;
		}
		else
		{
			currentProvider = _transaction.getProvider().getId();
			currentRadio = "rbProvider";
			canPracticeShow = false;
		}
		
		updateProviderStatusList();
		transaction = _transaction;
		isEditMode = true;
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

	public void saveInfo()
	{
		//transaction.setPayer(currentPayer);
		
	List<Payer> payers =	payerList.stream()
		.filter(p -> {
			
			if(selectedPayers.contains(p.getId()))
				return true;
			else 
				return false;
		}).collect(Collectors.toList());
		
		
//		Set<Payer> payers = new HashSet<Payer>(selectedPayers);


		Set<Payer> payersSet = new HashSet<Payer>(payers);
		transaction.setPayerList(payersSet);
		
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
		
		try
		{
			if(isEditMode)
			{		
				transactionService.update(transaction);
				//entityService.update(entity);
				//showMessage("Transaction has been updated");
			}
			else
			{
				entity.getTransactionList().add(transaction);
				transactions.add(transaction);
				filterTransactions.add(transaction); 
				transactionService.saveOrUpdate(transaction);
				entityService.update(entity);
				//showMessage("Transaction has been saved");
			}
		}
		catch(Exception ex){
			
			System.out.println(ex);
		}
		
		RequestContext.getCurrentInstance().execute("PF('Dlg1').hide()");
		
	}

	public void removeClicked(ProviderTransaction transac)
	{
		
		try {
			
			transactionService.remove(transac);
			transactions.remove(transac);
			realTimefilterList.remove(transac);
			filterTransactions.remove(transac);
			refreshAllData();
			
		}
		catch (StaleObjectStateException e){
			
			showMessage(Constants.ERRR_RECORDS_OUDATED);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
//		if(transac.getPractice() != null)
//		{
//			List<Integer> ids =  new ArrayList<>();
//			ids.add(transac.getPractice().getId());
//			transactionService.deleteTransactionByPractics(ids);
//		}
//		else
//		{
//			List<Integer> ids =  new ArrayList<>();
//			ids.add(transac.getProvider().getId());
//			transactionService.deleteTransactionByProvider(ids);
//		}
		
	}
	
	public void clearData()
	{	
		currentProvider = null;
		currentPractice = null;
		selectedPayers = new ArrayList();
		transaction = new ProviderTransaction();
		
		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		Date date = new Date();
		
		transaction.setTransactionDate(dateFormat.format(date));
		isEditMode = false;
		onSelectionChange();
	}
	
	public void showMessage(String msg) {
		
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Transaction", msg);     
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }
	
	public void onSelectionChange()
	{
		updateProviderStatusList();
	}
	
	private void updateProviderStatusList(){
		
		if(currentRadio.equals("rbPractice"))
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
	
	
	public boolean canEdit(){
		
		boolean isOK = true;
	
		if (tmpUser.getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString()) &&
				!tmpUser.getPermission().isCanPayerTransactionEdit())
			isOK = false;
//		if (SharedData.getSharedInstace().getCurrentUser().getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString()) &&
//				!SharedData.getSharedInstace().getCurrentUser().getPermission().isCanPayerTransactionEdit())
//			isOK = false;
		
		return isOK;
	}
	
	public boolean canCreate(){
		
		boolean isOK = true;
	
		
		if (tmpUser.getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString()) &&
				!tmpUser.getPermission().isCanPayerTransactionAdd())
			isOK = false;
//		if (SharedData.getSharedInstace().getCurrentUser().getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString()) &&
//				!SharedData.getSharedInstace().getCurrentUser().getPermission().isCanPayerTransactionAdd())
//			isOK = false;
		
		return isOK;
	}
	
	//Filter Code
	/**
	 * Filter Data logic
	 * @param obj
	 */
	public void payerCheckBoxChanged(Object obj){
		
		filterTransactions.clear();
		if(payerFilter.size() <= 0)
		{
			// show all
			filterTransactions.addAll(transactions);
		}
		else
		{
			
			Predicate<ProviderTransaction> predicate = (t)->{
				
				Optional<Payer> res = t.getPayerList().stream().filter(p -> payerFilter.contains(p.getId()))
				.findFirst();
				if(res.isPresent())
				{
					System.out.println("true");
					return true;
				}
				else
				{
					System.out.println("false");
					return false;
				}
			};
			
			List<ProviderTransaction> result = transactions.stream()
					.filter(predicate)
					.collect(Collectors.toList());
			
			filterTransactions.addAll(result);
		}
		this.payerFilter.size();
	}
	

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("");
	}
}
