package com.Beendo.Controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.ProviderTransaction;
import com.Beendo.Services.IPayerService;
import com.Beendo.Services.IPractiseService;
import com.Beendo.Services.IProviderService;
import com.Beendo.Services.IReportCallback;
import com.Beendo.Services.IReportService;
import com.Beendo.Services.ITransactionService;
import com.Beendo.Services.PayerService;
import com.Beendo.Services.PractiseService;
import com.Beendo.Services.ProviderService;
import com.Beendo.Services.TransactionService;
import com.Beendo.Utils.Constants;
import com.Beendo.Utils.ReportType;
import com.github.javaplugs.jsf.SpringScopeView;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
@Scope(value="session")
//@SpringScopeView
public class ReportsController implements DisposableBean,Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = -3324193225905098097L;
//	 extends RootController


	@Autowired
	IReportService reportService;
	// Transaction
	private List<ProviderTransaction> transactions;
	private List<ProviderTransaction> savedTransactions;
	private List<ProviderTransaction> providerTransactions;
	private List<ProviderTransaction> practiceTransactions;
//	private List<Integer> selectedPayerIds;

	// Payer
	private List<Payer> payerList;
	private Payer currentPayer;
	public String currentPayerStatus;
	private List<Integer> selectedPayerIdList;

	// Provider
	private List<Provider> providerList;
//	private List<Provider> selectedProviderList;
	private Integer currentProviderId;
//	private List<Payer> payerProvider;

	// Practice
	private List<Practice> practiceList;
//	private List<Practice> selectedPracticeList;
	private Integer currentPracticeId;

//	public HashMap<Integer, Provider> tmpHasnPractise = new HashMap<Integer, Provider>();
//	public HashMap<Integer, Payer> hashPayer = new HashMap<Integer, Payer>();
//	public HashMap<Integer, Practice> hashPractice = new HashMap<Integer, Practice>();

	private String [] statusList;
	
	// ---------------------------------------PRACTICE REPORT-----------------------------------------------

	public String viewRepPractice() {
		
		statusList = Constants.practiceStatus;
		cleanData();
		
		loadDataByReportType(ReportType.ReportTypePractise);
//		practiceList = practiseService.fetchAllByRole();
		
//		payerList = payerService.getAll();

		return "ReportPractice";
	}
	
	/**
	 * Load Data For all reports using type
	 * @param type
	 */
	private void loadDataByReportType(ReportType type){
		
		IReportCallback callBack = (List<Practice>tmpPractiseList, List<Provider>tmpProviderList, List<Payer>tmpPayerList, List<ProviderTransaction>tmpListTransaction,ReportType reportType) -> {
			
//			hashPractice.clear();
			this.practiceList = tmpPractiseList;
			this.payerList = tmpPayerList;
			
			switch (reportType) {
			case ReportTypeProvider:
				this.providerTransactions = tmpListTransaction;
				break;
			case ReportTypePractise:
				this.practiceTransactions = tmpListTransaction;
				break;
				
			case ReportTypeTransaction:
				this.savedTransactions = tmpListTransaction;
				break;

			default:
				break;
			}
			
			this.providerList = tmpProviderList;
//			for (Practice practice : practiceList) {
//
//				hashPractice.put(practice.getId(), practice);
//			}
//			
//			tmpHasnPractise.clear();
//			hashPayer.clear();
//			
//			for (Payer pro : payerList) {
//
//				hashPayer.put(pro.getId(), pro);
//			}
		};
		
		reportService.reloadPracticeReportData(callBack, type);
	}

	public String viewRepTransac() {
		cleanData();
		loadDataByReportType(ReportType.ReportTypeTransaction);
//		payerList = payerService.getAll();
//
//		initHashThree(payerList);
////		savedTransactions = transactionService.findAll();
//		savedTransactions = transactionService.fetchAllByRole();
		
//		transactions = savedTransactions;
		
		return "ReportTransaction";
	}
	
	public String viewRepProvider() {
		
		statusList = Constants.providerStatus;
		cleanData();
//		providerList = providerService.fetchAllByRole();
//		payerList = payerService.getAll();
//		
//		initHashThree(payerList);
//		
//		tmpHasnPractise.clear();
//		providerTransactions = transactionService.fetchAllByRole();

		loadDataByReportType(ReportType.ReportTypeProvider);
//		for (Provider pro : providerList) {
//
//			tmpHasnPractise.put(pro.getId(), pro);
//		}
//		
//		hashPayer.clear();
//		
//		for (Payer pro : payerList) {
//
//			hashPayer.put(pro.getId(), pro);
//		}

		return "ReportProvider";
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

//	public Provider getProviderById(Integer id) {
//
//		return tmpHasnPractise.get(id);
//	}

	public void onPracticeChange() {

		providerList.clear();

		Set<Provider> spayer = new HashSet<Provider>();
		
//		for (Practice practice : selectedPracticeList) {
//			spayer.addAll(practice.getProviders());
//		}
		
		providerList = new ArrayList(spayer);
		
		/*for (Practice practice : selectedPracticeList) {

			providerList.addAll(practice.getProviders());
		}*/

		// providerList = getProvidersFromPractice();

//		tmpHasnPractise.clear();
//
//		for (Provider pro : providerList) {
//
//			tmpHasnPractise.put(pro.getId(), pro);
//		}
	}

	public void searchPayerData() {

//		payerProvider.clear();

		if (currentPracticeId != null)
		{
			/*transactions = practiceTransactions.stream()
					.filter(f -> selectedPracticeList.stream().filter(p -> p.getId() == f.getPractice().getId()).count() > 0)
					.collect(Collectors.toList());*/
			
			transactions = practiceTransactions.stream()
					.filter(f -> (f.getPractice() != null && f.getPractice().getId() == currentPracticeId))
					.collect(Collectors.toList());
			//************************************************************************************************************************
			if (selectedPayerIdList.size() > 0)
			{
				/*transactions = transactions.stream()
						.filter(f -> selectedPayerList.stream().filter(p -> p.getId() == f.getPayer().getId()).count() > 0)
						.collect(Collectors.toList());*/
				
				transactions = new ArrayList<ProviderTransaction>(getMatchedData(transactions, selectedPayerIdList));
				
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
			if(!currentPayerStatus.isEmpty())
			{
				transactions = practiceTransactions.stream()
						.filter(f -> f.getParStatus().equals(currentPayerStatus))
						.collect(Collectors.toList());
				
				transactions = transactions.stream()
						.filter(f -> (f.getPractice() != null))
						.collect(Collectors.toList());
			}
			else
			{
				transactions = practiceTransactions.stream()
					.filter(f -> (f.getPractice() != null))
					.collect(Collectors.toList());
			}
		}
		
		//.filter(f -> (f.getPractice().getName() != null))

	}

	// ---------------------------------------TRANSACTION REPORT-----------------------------------------------


	
	Set<ProviderTransaction> getMatchedData(List<ProviderTransaction> tmpTransactions, List<Integer> tmpSelectedPayers)
	{
		Set<ProviderTransaction> setTrans = new HashSet<ProviderTransaction>();
		
		for (ProviderTransaction ptran : tmpTransactions) {
			
			for (Payer tpayer : ptran.getPayerList()) {
				
				for (Integer spayerId : tmpSelectedPayers) {
					
					if(spayerId == tpayer.getId())
						setTrans.add(ptran);
				}
				
			}
			
		}
		
		return setTrans;
		
	}

	// submit clicked
	public void getData() {
		if (selectedPayerIdList.size() > 0)
		{
			
			transactions = new ArrayList<ProviderTransaction>(getMatchedData(savedTransactions, selectedPayerIdList));
			
			/*transactions = savedTransactions.stream()
					.filter(f -> selectedPayers.stream().filter(p -> p.getId() == f.getPayer().getId()).count() > 0)
					.collect(Collectors.toList());*/
		}
		else
			transactions = savedTransactions;

		//.filter(f -> selectedPayers.stream().filter(p -> p.getId() == f.getPayer().getId()).count() > 0)
		// List<ProviderTransaction> tlist = savedTransactions.stream().filter(f
		// ->
		// selectedPayers.contains(f.getPayer())).collect(Collectors.toList());
		// List<ProviderTransaction> intersect =
		// savedTransactions.stream().filter(selectedPayers::contains).collect(Collectors.toList());

	}

	// ---------------------------------------PROVIDER REPORT-----------------------------------------------



	/*public void onEntityChange() {
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

	}*/

	public void getPayerData() {

//		payerProvider.clear();
		
//		List<ProviderTransaction> transac = providerTransactions.stream()
//				.filter(f -> (f.getParStatus().equals(currentPayerStatus)) && selectedPayerList.stream().filter(p -> p.getId() == f.getId()).count() > 0 ).collect(Collectors.toList());

//		List<ProviderTransaction> transac = providerTransactions.stream()
//				.filter(f -> (f.getParStatus().equals(currentPayerStatus)) ).collect(Collectors.toList());
//		
//		List<Payer> pay = selectedPayerList.stream()
//				.filter(f -> (providerTransactions.stream().filter(p -> p.getPayer().getId() == f.getId())).count() > 0 ).collect(Collectors.toList());
		
		if (currentProviderId != null)
		{
			/*transactions = providerTransactions.stream()
					.filter(f -> selectedProviderList.stream().filter(p -> p.getId() == currentProvider.getId()).count() > 0)
					.collect(Collectors.toList());*/
			
			transactions = providerTransactions.stream()
					.filter(f -> (f.getProvider() != null && f.getProvider().getId() == currentProviderId))
					.collect(Collectors.toList());
			//************************************************************************************************************************
			if (selectedPayerIdList.size() > 0)
			{
				/*transactions = transactions.stream()
						.filter(f -> selectedPayerList.stream().filter(p -> p.getId() == f.getPayer().getId()).count() > 0)
						.collect(Collectors.toList());*/
				
				transactions = new ArrayList(getMatchedData(transactions, selectedPayerIdList));
				
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
			
			if(!currentPayerStatus.isEmpty())
			{
				transactions = providerTransactions.stream()
						.filter(f -> f.getParStatus().equals(currentPayerStatus))
						.collect(Collectors.toList());
				
				transactions = transactions.stream()
						.filter(f -> (f.getProvider() != null))
						.collect(Collectors.toList());
			}
			else
			{
				transactions = providerTransactions.stream()
					.filter(f -> (f.getProvider() != null))
					.collect(Collectors.toList());
			}
		}
		
		//.filter(f -> (f.getProvider().getFirstName() != null))

	}

	// -------------------------

	private void cleanData() {
		// Transaction
		transactions = new ArrayList<ProviderTransaction>();
		savedTransactions = new ArrayList<ProviderTransaction>();

		// Payer
		payerList = new ArrayList<Payer>();

		// Provider
		providerList = new ArrayList<Provider>();
		currentProviderId = null;

		// Practice
		practiceList = new ArrayList<Practice>();
		currentPracticeId = null;
	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("");
	}

}
