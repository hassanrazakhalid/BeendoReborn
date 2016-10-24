package com.Beendo.Controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Beendo.Dto.LazyTransactionModel;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.Transaction;
import com.Beendo.Services.IReportService;
import com.Beendo.Services.ITransactionService;
import com.Beendo.Utils.Constants;
import com.Beendo.Utils.ReportFilter;
import com.Beendo.Utils.ReportType;
import com.Beendo.Utils.SharedData;
import com.github.javaplugs.jsf.SpringScopeView;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@SpringScopeView
@Controller
//@Scope(value = "session")
public class ReportsController extends BaseViewController implements DisposableBean, Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = -3324193225905098097L;
	// extends RootController
	
	private LazyTransactionModel lazyModel;
	@Autowired
	IReportService reportService;
	@Autowired
	ITransactionService transactionService;
	// Transaction
	private List<Transaction> filteredTransactions = new ArrayList<>();

	// Payer
	private List<Payer> payerList = new ArrayList<>();
	private Payer currentPayer = new Payer();
	
	private List<Integer> selectedPayerIdList = new ArrayList<>();

	public List<String> selectedStatus = new ArrayList<>();
	
	// Provider
	private List<Provider> providerList = new ArrayList<>();
	private List<Integer> selectedProviderIds = new ArrayList<>();

	// Practice
	private List<Practice> practiceList = new ArrayList<>();
	private List<Integer> selectedPracticeIds = new ArrayList<>();
	private String[] statusList;
	private ReportType reportType;
	// ---------------------------------------PRACTICE
	// REPORT-----------------------------------------------

	private ReportFilter reportFilter = new ReportFilter();
	
	public String titleFunc(){
		return "Select Parameters";
	}
	
	public String viewRepPractice() {

		clearData();
		statusList = Constants.practiceStatus;
		reportType = ReportType.ReportTypePractise;
		loadDataByReportType(reportType);
		
		return "ReportPractice";
	}
	public String viewRepTransac() {
		
		clearData();
		loadDataByReportType(ReportType.ReportTypeTransaction);
//		reportType = ;
		return "ReportTransaction";
	}

	public String viewRepProvider() {

		clearData();
		statusList = Constants.providerStatus;
		loadDataByReportType(ReportType.ReportTypeProvider);
//		reportType = ;
		return "ReportProvider";
	}
	
	private void clearData(){
		statusList = new String[]{};
		selectedPracticeIds = new ArrayList<>();
		selectedPayerIdList = new ArrayList<>();
		selectedProviderIds = new ArrayList<>();
		
	}
	
	public void onLoad(){
		
		System.out.println("");
//		loadDataByReportType(reportType);
	}

	/**
	 * Load Data For all reports using type
	 * 
	 * @param type
	 */
	private void loadDataByReportType(ReportType type) {

		this.reportType = type;
		Integer entityId = SharedData.getSharedInstace().getCurrentUser().getEntity().getId();
		
		reportFilter.setEntityId(entityId);
		reportFilter.setReportType(type);
		
		Consumer<Map<String, Object>> sender = (obj) -> {

			// this.providerTransactions = (List<ProviderTransaction>)
			// obj.get("transactions");
			this.payerList = (List<Payer>) obj.get("payerList");

			this.providerList = (List<Provider>) obj.get("providerList");
			this.practiceList = (List<Practice>) obj.get("practiseList");

			Predicate<Transaction> predicate = null;

//			switch (reportType) {
//			case ReportTypeProvider:
//				predicate = filterByProvider();
//				break;
//			case ReportTypePractise:
//				predicate = filterByPractise();
//				break;
//
//			case ReportTypeTransaction:
//				predicate = filterByTransaction();
//				break;
//
//			default:
//				break;
//			}

			this.lazyModel = new LazyTransactionModel(reportService, entityId, reportFilter);
			this.lazyModel.setRowCount((Integer) obj.get("count"));
		};		
		reportService.reloadPracticeReportData(sender, reportFilter, type);
		// reportService.reloadPracticeReportData(callBack, type);
	}

	public void searchButtonPressed(){
		
		seachResult();
	}
	
	private void seachResult(){
		
		reportFilter.setProviderIds(selectedProviderIds);
		reportFilter.setPayerIds(selectedPayerIdList);
		reportFilter.setPracticeIds(selectedPracticeIds);
		reportFilter.setStatusList(selectedStatus);
		
		
//		List<Transaction> res = reportService.getTransactionByProvider(reportFilter);
//		lazyModel.datasource = res;
	}
	
//	public void filterReport() {
//
//		switch (reportType) {
//		case ReportTypeProvider:
//
//			filterByProvider();
//			break;
//		case ReportTypePractise:
//			filterByPractise();
//			break;
//
//		case ReportTypeTransaction:
//			filterByTransaction();
//			break;
//		default:
//			break;
//		}
//	}
//
//	private Predicate<Transaction> filterByPractise() {
//
//		Predicate<Transaction> predicate = (t) -> {
//
//			boolean isPractiseOk = true;
//			boolean isPayerOk = true;
//			boolean isStatusOk = true;
//
//			if (currentPracticeId != null) {
//				if (t.getPractice() == null)
//					isPractiseOk = false;
//				else if (currentPracticeId != t.getPractice().getId())
//					isPractiseOk = false;
//			}
//
//	
//			if (selectedPayerIdList != null && selectedPayerIdList.size() > 0) {
//				isPayerOk = isTransactionExistInPayer(t);
//			}
//
//			if (!currentPayerStatus.isEmpty() && !currentPayerStatus.equals(t.getParStatus())) {
//				isStatusOk = false;
//			}
//
//			if (isPayerOk && isPractiseOk && isStatusOk)
//				return true;
//			else
//				return false;
//		};
//
//		return predicate;
//	}
//
//	private Predicate<Transaction> filterByProvider() {
//
//		Predicate<Transaction> predicate = (t) -> {
//
//			boolean isProviderOk = true;
//			boolean isPayerOk = true;
//			boolean isStatusOk = true;
//
//			if (currentProviderId != null) {
//				if (t.getProvider() == null)
//					isProviderOk = false;
//				else if (currentProviderId != t.getProvider().getId())
//					isProviderOk = false;
//			}
//
//			if (selectedPayerIdList != null && selectedPayerIdList.size() > 0) {
//				isPayerOk = isTransactionExistInPayer(t);
//			}
//
//			if (!currentPayerStatus.isEmpty() && !currentPayerStatus.equals(t.getParStatus())) {
//				isStatusOk = false;
//			}
//
//			if (isPayerOk && isProviderOk && isStatusOk)
//				return true;
//			else
//				return false;
//
//		};
//		return predicate;
//	}
//
//	private Predicate<Transaction> filterByTransaction() {
//
//		Predicate<Transaction> predicate = (t) -> {
//
//			// boolean isProviderOk = true;
//			boolean isPayerOk = true;
//			boolean isStatusOk = true;
//
//			if (selectedPayerIdList != null && selectedPayerIdList.size() > 0) {
//				isPayerOk = isTransactionExistInPayer(t);
//			}
//
//			if (isPayerOk && isStatusOk)
//				return true;
//			else
//				return false;
//		};
//
//		return predicate;
//	}

	private boolean isTransactionExistInPayer(Transaction sender) {

	Optional<Integer> res =	selectedPayerIdList.stream().filter( payerId -> sender.getId() == payerId).findFirst();
		
//		Set<Integer> ids = tmpPayerList.stream().map(Payer::getId).collect(Collectors.toSet());
//
//		Set<Integer> res = ids.stream().filter(selectedPayerIdList::contains).collect(Collectors.toSet());
	return res.isPresent();
//		if ()
//			return true;
//		else
//			return false;
	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("");
	}

}
