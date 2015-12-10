package com.Beendo.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
	private List<ProviderTransaction> providerTransactions;
	private List<ProviderTransaction> practiceTransactions;
	private List<Payer> selectedPayers;

	// Payer
	private List<Payer> payerList;
	private Payer currentPayer;
	public String currentPayerStatus;
	private List<Payer> selectedPayerList;

	// Provider
	private List<Provider> providerList;
	private List<Provider> selectedProviderList;
	private Provider currentProvider;
	private List<Payer> payerProvider;

	// Practice
	private List<Practice> practiceList;
	private List<Practice> selectedPracticeList;
	private Practice currentPractice;

	public HashMap<Integer, Provider> tmpHasnPractise = new HashMap<Integer, Provider>();
	public HashMap<Integer, Payer> hashPayer = new HashMap<Integer, Payer>();
	public HashMap<Integer, Practice> hashPractice = new HashMap<Integer, Practice>();

	// ---------------------------------------PRACTICE REPORT-----------------------------------------------

	public String viewRepPractice() {
		cleanData();
		practiceList = practiseService.fetchAllByRole();
		hashPractice.clear();
		// payerProvider = payerService.findAll();

		List<Provider> provList = providerService.fetchAllByRole();
		practiceTransactions = transactionService.fetchAllByRole();
		initHashFour(provList);

		for (Practice practice : practiceList) {

			hashPractice.put(practice.getId(), practice);
		}

		// providerList = providerService.findAll();

		/*
		 * tmpHasnPractise.clear();
		 * 
		 * for (Provider pro : providerList) {
		 * 
		 * tmpHasnPractise.put(pro.getId(), pro); }
		 */

		return "ReportPractice";
	}

	/*
	 * private List<Provider> getProvidersFromPractice( ) { List<Provider>
	 * provList = providerService.fetchAllByUser(); List<Provider> tmpProvider =
	 * new ArrayList<Provider>();
	 * 
	 * for (Provider provider : provList) {
	 * 
	 * for (Practice prac : provider.getPracticeList()) {
	 * 
	 * if(prac.getId().equals(currentPractice.getId())) {
	 * tmpProvider.add(provider); break; } } }
	 * 
	 * return tmpProvider;
	 * 
	 * }
	 */

	public Provider getProviderById(Integer id) {

		return tmpHasnPractise.get(id);
	}

	public void onPracticeChange() {

		providerList.clear();

		Set<Provider> spayer = new HashSet<Provider>();
		
		for (Practice practice : selectedPracticeList) {
			spayer.addAll(practice.getProviders());
		}
		
		providerList = new ArrayList(spayer);
		
		/*for (Practice practice : selectedPracticeList) {

			providerList.addAll(practice.getProviders());
		}*/

		// providerList = getProvidersFromPractice();

		tmpHasnPractise.clear();

		for (Provider pro : providerList) {

			tmpHasnPractise.put(pro.getId(), pro);
		}
	}

	public void searchPayerData() {

		payerProvider.clear();

		if (selectedPracticeList.size() > 0)
		{
			transactions = practiceTransactions.stream()
					.filter(f -> selectedPracticeList.stream().filter(p -> p.getId() == f.getPractice().getId()).count() > 0)
					.collect(Collectors.toList());
			
			if (selectedPayerList.size() > 0)
			{
				transactions = transactions.stream()
						.filter(f -> selectedPayerList.stream().filter(p -> p.getId() == f.getPayer().getId()).count() > 0)
						.collect(Collectors.toList());
				
				if(!currentPayerStatus.isEmpty())
				{
					transactions = transactions.stream()
							.filter(f -> f.getParStatus().equals(currentPayerStatus))
							.collect(Collectors.toList());
				}
				
			}
			
		}
		else
		{
			transactions = practiceTransactions.stream()
					.filter(f -> (f.getPractice().getName() != null))
					.collect(Collectors.toList());
		}
		
		/*if (selectedPracticeList.size() > 0) {
			if (selectedProviderList.size() > 0) {
				if (selectedPayerList.size() > 0) {

					if (currentPayerStatus != "") {
						
						for (Payer payer : selectedPayerList) {						
							
							for (ProviderTransaction providerTransaction : practiceTransactions) {
								
								if(payer.getId() == providerTransaction.getPayer().getId() && providerTransaction.getPractice().getName() != null && currentPayerStatus.equals(providerTransaction.getParStatus()))
									payerProvider.add(providerTransaction.getPayer());
							}
							
						}
						
						for (Payer payer : selectedPayerList) {

							if (payer.getPar().equals(currentPayerStatus))
								payerProvider.add(payer);
						}
					} else {
						payerProvider.addAll(selectedPayerList);
					}
				} else {
					
					Set<Payer> spayer = new HashSet<Payer>();
					
					for (Provider pro : selectedProviderList) {
						spayer.addAll(pro.getPayerList());
					}
					
					payerProvider = new ArrayList(spayer);
					
					for (Provider pro : selectedProviderList) {
						payerProvider.addAll(pro.getPayerList());
					}
				}
			} else {
				
				Set<Payer> spayer = new HashSet<Payer>();
				
				for (Provider pro : providerList) {
					spayer.addAll(pro.getPayerList());
				}
				
				payerProvider = new ArrayList(spayer);
				
				for (Provider pro : providerList) {
					payerProvider.addAll(pro.getPayerList());
				}
			}
		}
		else
		{
			
			Set<Payer> spayer = new HashSet<Payer>();
			
			for (Practice practice : practiceList) {
				for (Provider prov : practice.getProviders()) {
					spayer.addAll(prov.getPayerList());
				}
			}
			
			payerProvider = new ArrayList(spayer);
			
			for (Practice practice : practiceList) {
				for (Provider prov : practice.getProviders()) {
					payerProvider.addAll(prov.getPayerList());
				}
			}
		}*/

	}

	// ---------------------------------------TRANSACTION REPORT-----------------------------------------------

	public String viewRepTransac() {
		cleanData();
		payerList = payerService.findAll();

		initHashThree(payerList);
//		savedTransactions = transactionService.findAll();
		savedTransactions = transactionService.fetchAllByRole();
		
		transactions = savedTransactions;
		
		return "ReportTransaction";
	}

	// submit clicked
	public void getData() {
		if (selectedPayers.size() > 0)
			transactions = savedTransactions.stream()
					.filter(f -> selectedPayers.stream().filter(p -> p.getId() == f.getPayer().getId()).count() > 0)
					.collect(Collectors.toList());
		else
			transactions = savedTransactions;

		// List<ProviderTransaction> tlist = savedTransactions.stream().filter(f
		// ->
		// selectedPayers.contains(f.getPayer())).collect(Collectors.toList());
		// List<ProviderTransaction> intersect =
		// savedTransactions.stream().filter(selectedPayers::contains).collect(Collectors.toList());

	}

	// ---------------------------------------PROVIDER REPORT-----------------------------------------------

	public String viewRepProvider() {
		cleanData();
		providerList = providerService.fetchAllByRole();
		tmpHasnPractise.clear();
		providerTransactions = transactionService.fetchAllByRole();

		for (Provider pro : providerList) {

			tmpHasnPractise.put(pro.getId(), pro);
		}

		return "ReportProvider";
	}

	public void onEntityChange() {
		// currentProvider.setCentity(currentEntity);
		// payerProvider = currentProvider.getPayerList();

		payerList.clear();
		Set<Payer> spayer = new HashSet<Payer>();
		
		for (Provider pro : selectedProviderList) {
			//payerList.addAll(pro.getPayerList());
			spayer.addAll(pro.getPayerList());
		}
		
		payerList = new ArrayList(spayer);

		hashPayer.clear();

		// for (Payer pro : currentProvider.getPayerList()) {
		//
		// hashPayer.put(pro.getId(), pro);
		// }

		
		
		
		for (Payer pro : payerList) {

			hashPayer.put(pro.getId(), pro);
		}

		initHashThree(payerList);
		// initHashThree(currentProvider.getPayerList());

	}

	public void getPayerData() {

		payerProvider.clear();
		
//		List<ProviderTransaction> transac = providerTransactions.stream()
//				.filter(f -> (f.getParStatus().equals(currentPayerStatus)) && selectedPayerList.stream().filter(p -> p.getId() == f.getId()).count() > 0 ).collect(Collectors.toList());

//		List<ProviderTransaction> transac = providerTransactions.stream()
//				.filter(f -> (f.getParStatus().equals(currentPayerStatus)) ).collect(Collectors.toList());
//		
//		List<Payer> pay = selectedPayerList.stream()
//				.filter(f -> (providerTransactions.stream().filter(p -> p.getPayer().getId() == f.getId())).count() > 0 ).collect(Collectors.toList());
		
		if (selectedProviderList.size() > 0)
		{
			transactions = providerTransactions.stream()
					.filter(f -> selectedProviderList.stream().filter(p -> p.getId() == f.getProvider().getId()).count() > 0)
					.collect(Collectors.toList());
			
			if (selectedPayerList.size() > 0)
			{
				transactions = transactions.stream()
						.filter(f -> selectedPayerList.stream().filter(p -> p.getId() == f.getPayer().getId()).count() > 0)
						.collect(Collectors.toList());
				
				if(!currentPayerStatus.isEmpty())
				{
					transactions = transactions.stream()
							.filter(f -> f.getParStatus().equals(currentPayerStatus))
							.collect(Collectors.toList());
				}
				
			}
			
		}
		else
		{
			transactions = providerTransactions.stream()
					.filter(f -> (f.getProvider().getName() != null))
					.collect(Collectors.toList());
		}
		
		/*if (selectedProviderList.size() > 0) {
			if (selectedPayerList.size() > 0) {

				if (currentPayerStatus != "") {
					
					for (Payer payer : selectedPayerList) {						
					
						for (ProviderTransaction providerTransaction : providerTransactions) {
							
							if(payer.getId() == providerTransaction.getPayer().getId() && providerTransaction.getProvider().getName() != null && currentPayerStatus.equals(providerTransaction.getParStatus()))
								payerProvider.add(providerTransaction.getPayer());
						}
						
						for (ProviderTransaction providerTransaction : transac) {
							
							if(payer.getId() == providerTransaction.getPayer().getId())
								payerProvider.add(providerTransaction.getPayer());
						}
					}
					
					for (Payer payer : selectedPayerList) {

						if (payer.getPar().equals(currentPayerStatus))
							payerProvider.add(payer);
					}
					
				} else {
					payerProvider.addAll(selectedPayerList);
				}
			} else {
				
				Set<Payer> spayer = new HashSet<Payer>();
				
				for (Provider pro : selectedProviderList) {
					spayer.addAll(pro.getPayerList());
				}
				
				payerProvider = new ArrayList(spayer);
				
				for (Provider pro : selectedProviderList) {
					payerProvider.addAll(pro.getPayerList());
				}
			}
		} else {
			
			Set<Payer> spayer = new HashSet<Payer>();
			
			for (Provider pro : providerList) {
				spayer.addAll(pro.getPayerList());
			}
			
			payerProvider = new ArrayList(spayer);
			
			for (Provider pro : providerList) {
				payerProvider.addAll(pro.getPayerList());
			}
		}*/

	}

	// -------------------------

	private void cleanData() {
		// Transaction
		transactions = new ArrayList<ProviderTransaction>();
		savedTransactions = new ArrayList<ProviderTransaction>();

		// Payer
		payerList = new ArrayList<Payer>();
		Payer currentPayer = new Payer();

		// Provider
		providerList = new ArrayList<Provider>();
		currentProvider = new Provider();
		payerProvider = new ArrayList<Payer>();

		// Practice
		practiceList = new ArrayList<Practice>();
		currentPractice = new Practice();
	}

}
