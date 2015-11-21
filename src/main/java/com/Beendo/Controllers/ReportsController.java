package com.Beendo.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.ProviderTransaction;
import com.Beendo.Services.PayerService;
import com.Beendo.Services.PractiseService;
import com.Beendo.Services.ProviderService;
import com.Beendo.Services.TransactionService;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
public class ReportsController {

	@Autowired
	private PayerService payerService;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private ProviderService providerService;
	
	@Autowired
	private PractiseService practiseService;
	
	// Transaction
	private List<ProviderTransaction> transactions;
	private List<ProviderTransaction> savedTransactions;
	
	// Payer
	private List<Payer> payerList;
	private Payer currentPayer;
	
	//Provider
	private List<Provider> providerList;
	private Provider currentProvider;
	private List<Payer> payerProvider;
	
	// Practice
	private List<Practice> practiceList;
	private Practice currentPractice;
	
	
	public HashMap<Integer, Provider> tmpHasnPractise = new HashMap<Integer, Provider>();
	public HashMap<Integer, Payer> hashPayer = new HashMap<Integer, Payer>();
	
	
	public String viewRepTransac()
	{
		payerList = payerService.findAll();
		savedTransactions = transactionService.findAll();
		return "ReportTransaction";
	}
	
	public String viewRepProvider()
	{
		providerList = providerService.findAll();
		
		for (Provider pro : providerList) {
			
			tmpHasnPractise.put(pro.getId(), pro);
		}
		
		return "ReportProvider";
	}
	
	public String viewRepPractice()
	{
		practiceList = practiseService.fetchAll();
		providerList = providerService.findAll();
		
		return "ReportPractice";
	}
	
	public Provider getProviderById(Integer id){
		
		return tmpHasnPractise.get(id);
	}
	
	public void onEntityChange()
	{
		//currentProvider.setCentity(currentEntity);
		//payerProvider = currentProvider.getPayerList();
			
		hashPayer.clear();
		for (Payer pro : currentProvider.getPayerList()) {
			
			hashPayer.put(pro.getId(), pro);
		}
		
	}
	
	
	public void onPracticeChange()
	{
		
	}
	
	
	public void getPayerData()
	{	
		//payerList = payerProvider;// = currentProvider.getPayerList();
	}
	
	public void getData()
	{
		transactions = new ArrayList<ProviderTransaction>();
		
		for (ProviderTransaction trans : savedTransactions) {
			
			if(currentPayer.getId() == trans.getPayer().getId())
				transactions.add(trans);
		}
		
	}
	
}
