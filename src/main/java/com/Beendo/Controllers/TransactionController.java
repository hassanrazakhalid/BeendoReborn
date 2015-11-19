package com.Beendo.Controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.ProviderTransaction;
import com.Beendo.Services.PayerService;
import com.Beendo.Services.PractiseService;
import com.Beendo.Services.TransactionService;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
public class TransactionController {

	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private PayerService payerService;
	
	@Autowired
	private PractiseService practiseService;
	
	private ProviderTransaction transaction = new ProviderTransaction();
	private List<ProviderTransaction> transactions;
	private Boolean isEditMode;
	
	
	private Payer currentPayer;
	private Practice currentPractice;
	
	private List<Practice> practiceList;
	private List<Payer> payerList;
	
	
	private HashMap<Integer, Practice> _hash = new HashMap<Integer, Practice>();
	
	public String view()
	{
		transactions = transactionService.findAll();
		payerList = payerService.findAll();
		practiceList = practiseService.fetchAll();
		
		for (Practice practice : practiceList) {
			
			_hash.put(practice.getId(), practice);
		}
		
		return "ViewTransaction";
	}
	
	public void updateClicked(ProviderTransaction _transaction)
	{
		currentPayer = _transaction.getPayer();
		currentPractice = _transaction.getPractice();
		transaction = _transaction;
		isEditMode = true;
	}
	
	public void saveInfo()
	{
		transaction.setPayer(currentPayer);
		transaction.setPractice(currentPractice);
		if(isEditMode)
		{
			transactionService.update(transaction);
			showMessage("Transaction has been updated");
		}
		else
		{
			transactions.add(transaction);
			transactionService.save(transaction);
			showMessage("Transaction has been saved");
		}
	}

	
	public void clearData()
	{	
		transaction = new ProviderTransaction();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		transaction.setTransactionDate(dateFormat.format(date));
		isEditMode = false;
	}
	
	public void showMessage(String msg) {
		
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Transaction", msg);     
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }
	
}
