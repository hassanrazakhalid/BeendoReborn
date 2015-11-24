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
public class ReportsController extends RootController {

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
	public String currentPayerStatus;
	
	//Provider
	private List<Provider> providerList;
	private Provider currentProvider;
	private List<Payer> payerProvider;
	
	// Practice
	private List<Practice> practiceList;
	private Practice currentPractice;
	
	
	public HashMap<Integer, Provider> tmpHasnPractise = new HashMap<Integer, Provider>();
	public HashMap<Integer, Payer> hashPayer = new HashMap<Integer, Payer>();
	public HashMap<Integer, Practice> hashPractice = new HashMap<Integer, Practice>();
	
	
	public String viewRepTransac()
	{
		cleanData();
		payerList = payerService.findAll();
		savedTransactions = transactionService.findAll();
		return "ReportTransaction";
	}
	
	public String viewRepProvider()
	{
		cleanData();
		providerList = providerService.findAll();
		tmpHasnPractise.clear();
		
		for (Provider pro : providerList) {
			
			tmpHasnPractise.put(pro.getId(), pro);
		}
		
		return "ReportProvider";
	}
	
	public String viewRepPractice()
	{
		cleanData();
		practiceList = practiseService.fetchAll();
		hashPractice.clear();
		
		for (Practice practice : practiceList) {
			
			hashPractice.put(practice.getId(), practice);
		}
		
		//providerList = providerService.findAll();
		
		/*tmpHasnPractise.clear();
		
		for (Provider pro : providerList) {
			
			tmpHasnPractise.put(pro.getId(), pro);
		}*/
		
		return "ReportPractice";
	}
	
	
	private void cleanData()
	{
		// Transaction
		transactions = new ArrayList<ProviderTransaction>();
		savedTransactions = new ArrayList<ProviderTransaction>();
		
		// Payer
		payerList = new ArrayList<Payer>();
		Payer currentPayer = new Payer();
		
		//Provider
		providerList = new ArrayList<Provider>();
		currentProvider = new Provider();
		payerProvider = new ArrayList<Payer>();
		
		// Practice
		practiceList = new ArrayList<Practice>();
		currentPractice = new Practice();
	}
	
	
	private List<Provider> getProvidersFromPractice( )
	{
		List<Provider> provList = providerService.findAll();
		List<Provider> tmpProvider = new ArrayList<Provider>();
		
		for (Provider provider : provList) {
			
			for (Practice prac : provider.getPracticeList()) {
				
				if(prac.getId().equals(currentPractice.getId()))
				{
					tmpProvider.add(provider);
					break;
				}
			}
		}
		
		return tmpProvider;
		
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
		
		providerList = getProvidersFromPractice();
		
		tmpHasnPractise.clear();
		
		for (Provider pro : providerList) {
			
			tmpHasnPractise.put(pro.getId(), pro);
		}
	}
	
	
	public void getPayerData()
	{	
		//payerList = payerProvider;// = currentProvider.getPayerList();
		List<Payer> tmpList = new ArrayList<Payer>();
		for (Payer payer : payerProvider) {
			
			if(payer.getPar().equals(currentPayerStatus))
				tmpList.add(payer);
		}
		
		payerProvider = tmpList;
		
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
