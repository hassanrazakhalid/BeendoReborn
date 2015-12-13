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
import com.Beendo.Utils.Role;
import com.Beendo.Utils.SharedData;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
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
	
	
	private String currentRadio;
	private Boolean canPracticeShow = true;
	
	
	//private HashMap<Integer, Practice> _hash = new HashMap<Integer, Practice>();
	
	public String view()
	{
		//transactions = transactionService.findAll();
//		transactions = transactionService.findAllByUser();
		transactions = transactionService.fetchAllByRole();
		payerList = payerService.findAll();
		practiceList = practiseService.fetchAllByRole();
		providerList = providerService.fetchAllByRole();
		
		initHashTwo(practiceList);
		initHashThree(payerList);
		initHashFour(providerList);
		
		payerList.clear();
		
		currentRadio = "rbPractice";
		canPracticeShow = true;
		
		/*for (Practice practice : practiceList) {
			
			_hash.put(practice.getId(), practice);
		}*/
		
		return "ViewTransaction";
	}
	
	public void updateClicked(ProviderTransaction _transaction)
	{
		//currentPayer = _transaction.getPayer();
		//currentPractice = _transaction.getPractice();
		currentPayer = getPayerById(_transaction.getPayer().getId());
		currentPractice = getPractiseById(_transaction.getPractice().getId());
		transaction = _transaction;
		isEditMode = true;
	}
	
	public void saveInfo()
	{
		transaction.setPayer(currentPayer);
		
		if(canPracticeShow)
			transaction.setPractice(currentPractice);
		else
			transaction.setProvider(currentProvider);
		
		CEntitiy entity = SharedData.getSharedInstace().getCurrentUser().getEntity();
		transaction.setEntity(entity);
		
		if(isEditMode)
		{		
			transactionService.update(transaction);
			entityService.update(entity);
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
		
		RequestContext.getCurrentInstance().execute("PF('Dlg1').hide()");
		
	}

	
	public void clearData()
	{	
		currentPayer = null;
		currentPractice = null;
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
	
	
	public void onProviderChange()
	{
		payerList.clear();
		payerList.addAll(currentProvider.getPayerList());
	}
	
	public void onPracticeChange()
	{
		Set<Payer> spayer = new HashSet<Payer>();
		
		for (Provider pro : currentPractice.getProviders()) {
			
			spayer.addAll(pro.getPayerList());
		}
		
		payerList = new ArrayList(spayer);
		
	}
	
	
	public boolean canEdit(){
		
		boolean isOK = true;
	
		if (SharedData.getSharedInstace().getCurrentUser().getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString()) &&
				!SharedData.getSharedInstace().getCurrentUser().getPermission().isCanPayerTransactionEdit())
			isOK = false;
		
		return isOK;
	}
	
	public boolean canCreate(){
		
		boolean isOK = true;
	
		if (SharedData.getSharedInstace().getCurrentUser().getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString()) &&
				!SharedData.getSharedInstace().getCurrentUser().getPermission().isCanPayerTransactionAdd())
			isOK = false;
		
		return isOK;
	}
	
}
