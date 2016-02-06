package com.Beendo.Controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;

import org.hibernate.StaleObjectStateException;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.ProviderTransaction;
import com.Beendo.Entities.User;
import com.Beendo.Services.EntityService;
import com.Beendo.Services.PayerService;
import com.Beendo.Services.PractiseService;
import com.Beendo.Services.ProviderService;
import com.Beendo.Services.TransactionService;
import com.Beendo.Services.UserService;
import com.Beendo.Utils.Constants;
import com.Beendo.Utils.Role;
import com.Beendo.Utils.SharedData;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
@Scope(value="session")
public class TransactionController extends RootController {

	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private PayerService payerService;
	
	@Autowired
	private PractiseService practiseService;
	
	@Autowired
	private EntityService entityService;
	
	@Autowired
	private ProviderService providerService;
	
	private ProviderTransaction transaction = new ProviderTransaction();
	private List<ProviderTransaction> transactions;
	private Boolean isEditMode;
	
	
	private Payer currentPayer;
	private Practice currentPractice = new Practice();
	private Provider currentProvider = new Provider();
	
	private List<Practice> practiceList;
	private List<Payer> payerList;
	private List<Provider> providerList;
	private List<Payer> selectedPayers;
	
	private String currentRadio;
	private Boolean canPracticeShow = true;
	
	private User tmpUser;
	@Autowired
	private UserService userService;
	//private HashMap<Integer, Practice> _hash = new HashMap<Integer, Practice>();
	
	private void refreshAllData(){
		
		User user = SharedData.getSharedInstace().getCurrentUser();
		tmpUser = userService.findById(user.getId(), false);
		
		transactions = transactionService.fetchAllByRole();
		payerList = payerService.findAll();
		practiceList = practiseService.fetchAllByRole();
		providerList = providerService.fetchAllByRole();
		
		initHashTwo(practiceList);
		initHashThree(payerList);
		initHashFour(providerList);
		
		//payerList.clear();
		
		currentRadio = "rbPractice";
		canPracticeShow = true;
		
		/*for (Practice practice : practiceList) {
			
			_hash.put(practice.getId(), practice);
		}*/
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
		List<Payer> prlist = new ArrayList();
		
		for (Payer payer : _transaction.getPayerList()) {
			
			prlist.add(getPayerById(payer.getId()));
		}
		
		selectedPayers = prlist;
		
		if(_transaction.getPractice() != null)
		{
			currentPractice = getPractiseById(_transaction.getPractice().getId());
			currentRadio = "rbPractice";
			canPracticeShow = true;
		}
		else
		{
			currentProvider = getProviderById(_transaction.getProvider().getId());
			currentRadio = "rbProvider";
			canPracticeShow = false;
		}
		
		transaction = _transaction;
		isEditMode = true;
	}
	
	public void saveInfo()
	{
		//transaction.setPayer(currentPayer);
		Set<Payer> payers = new HashSet<Payer>(selectedPayers);
		transaction.setPayerList(payers);
		
		Integer entityId = null;
		if(canPracticeShow)
		{
			transaction.setPractice(currentPractice);
			transaction.setProvider(null);
			entityId = currentPractice.getEntity().getId();
		}
		else
		{
			transaction.setProvider(currentProvider);
			transaction.setPractice(null);
			entityId = currentProvider.getCentity().getId();
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
				transactionService.save(transaction);
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
			
			transactionService.delete(transac);
			transactions.remove(transac);
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
		currentPayer = null;
		currentPractice = null;
		selectedPayers = new ArrayList();
		transaction = new ProviderTransaction();
		
		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		Date date = new Date();
		
		transaction.setTransactionDate(dateFormat.format(date));
		isEditMode = false;
	}
	
	public void showMessage(String msg) {
		
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Transaction", msg);     
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }
	
	public void onSelectionChange()
	{
		if(currentRadio.equals("rbPractice"))
			canPracticeShow = true;
		else
			canPracticeShow = false;
		
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
	
}
