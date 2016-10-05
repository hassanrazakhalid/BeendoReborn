package com.Beendo.Controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.Beendo.Dto.LazyTransactionModel;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.ProviderTransaction;
import com.Beendo.Entities.User;
import com.Beendo.Services.IPayerService;
import com.Beendo.Services.IPractiseService;
import com.Beendo.Services.IProviderService;
import com.Beendo.Services.IReportCallback;
import com.Beendo.Services.IReportService;
import com.Beendo.Services.ITransactionCallback;
import com.Beendo.Services.ITransactionService;
import com.Beendo.Services.PayerService;
import com.Beendo.Services.PractiseService;
import com.Beendo.Services.ProviderService;
import com.Beendo.Services.TransactionService;
import com.Beendo.Utils.Constants;
import com.Beendo.Utils.ReportType;
import com.Beendo.Utils.SharedData;
import com.github.javaplugs.jsf.SpringScopeView;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
@Scope(value = "session")
// @SpringScopeView
public class ReportsController implements DisposableBean, Serializable {
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
	// private List<ProviderTransaction> transactions = new ArrayList<>() ;
	private List<ProviderTransaction> filteredTransactions = new ArrayList<>();
	// private List<ProviderTransaction> providerTransactions = new
	// ArrayList<>();
	// private List<ProviderTransaction> practiceTransactions = new
	// ArrayList<>();
	// private List<Integer> selectedPayerIds;

	// Payer
	private List<Payer> payerList = new ArrayList<>();
	private Payer currentPayer = new Payer();
	public String currentPayerStatus = "";
	private List<Integer> selectedPayerIdList = new ArrayList<>();

	// Provider
	private List<Provider> providerList = new ArrayList<>();
	// private List<Provider> selectedProviderList;
	private Integer currentProviderId;
	// private List<Payer> payerProvider;

	// Practice
	private List<Practice> practiceList = new ArrayList<>();
	// private List<Practice> selectedPracticeList;
	private Integer currentPracticeId;
	private String[] statusList;
	private ReportType reportType;
	// ---------------------------------------PRACTICE
	// REPORT-----------------------------------------------

	public String viewRepPractice() {

		statusList = Constants.practiceStatus;
		cleanData();

		loadDataByReportType(ReportType.ReportTypePractise);
		return "ReportPractice";
	}

	/**
	 * Load Data For all reports using type
	 * 
	 * @param type
	 */
	private void loadDataByReportType(ReportType type) {

		this.reportType = type;
		Integer entityId = SharedData.getSharedInstace().getCurrentUser().getEntity().getId();

		// transactionService.getLatestTransactions(entityId);
		//
		// ITransactionCallback callBack = (User user,
		// List<ProviderTransaction>transactions, List<Payer>payerList,
		// List<Practice>practiceList, List<Provider>providerList,
		// Map<String,Object>otherInfo)->{
		//
		// tmpUser = user;
		// this.lazyModel = new
		// LazyTransactionModel(transactionService,entityId);
		// this.lazyModel.setRowCount((Integer)otherInfo.get("count"));
		//// this.lazyModel.setRowCount(20);
		//// this.transactions = transactions;
		// this.payerList = payerList;
		// this.practiceList = practiceList;
		// this.providerList = providerList;
		// this.filterTransactions.clear();
		// this.filterTransactions.addAll(transactions);
		// };
		//
		// transactionService.refreshAllData(0, 10, entityId, callBack);

		Consumer<Map<String, Object>> sender = (obj) -> {

			// this.providerTransactions = (List<ProviderTransaction>)
			// obj.get("transactions");
			this.payerList = (List<Payer>) obj.get("payerList");
			

			this.providerList = (List<Provider>) obj.get("providerList");
			this.practiceList = (List<Practice>) obj.get("practiseList");

			Predicate<ProviderTransaction> predicate = null;

			switch (reportType) {
			case ReportTypeProvider:
				predicate = filterByProvider();
				break;
			case ReportTypePractise:
				predicate = filterByPractise();
				break;

			case ReportTypeTransaction:
				predicate = filterByTransaction();
				break;

			default:
				break;
			}

			this.lazyModel = new LazyTransactionModel(transactionService, entityId, predicate);
			this.lazyModel.setRowCount((Integer) obj.get("count"));
		};
		reportService.reloadPracticeReportData(sender, 0, 10, entityId, type);
		// reportService.reloadPracticeReportData(callBack, type);
	}

	public String viewRepTransac() {
		cleanData();
		loadDataByReportType(ReportType.ReportTypeTransaction);
		return "ReportTransaction";
	}

	public String viewRepProvider() {

		statusList = Constants.providerStatus;
		cleanData();
		loadDataByReportType(ReportType.ReportTypeProvider);
		return "ReportProvider";
	}

	public void filterReport() {

		switch (reportType) {
		case ReportTypeProvider:

			filterByProvider();
			break;
		case ReportTypePractise:
			filterByPractise();
			break;

		case ReportTypeTransaction:
			filterByTransaction();
			break;
		default:
			break;
		}
	}

	private Predicate<ProviderTransaction> filterByPractise() {

		Predicate<ProviderTransaction> predicate = (t) -> {

			boolean isPractiseOk = true;
			boolean isPayerOk = true;
			boolean isStatusOk = true;

			if (currentPracticeId != null) {
				if (t.getPractice() == null)
					isPractiseOk = false;
				else if (currentPracticeId != t.getPractice().getId())
					isPractiseOk = false;
			}

			if (selectedPayerIdList != null && selectedPayerIdList.size() > 0) {
				isPayerOk = isTransactionExistInPayer(t.getPayerList());
			}

			if (!currentPayerStatus.isEmpty() && !currentPayerStatus.equals(t.getParStatus())) {
				isStatusOk = false;
			}

			if (isPayerOk && isPractiseOk && isStatusOk)
				return true;
			else
				return false;
		};

		return predicate;
	}

	private Predicate<ProviderTransaction> filterByProvider() {

		Predicate<ProviderTransaction> predicate = (t) -> {

			boolean isProviderOk = true;
			boolean isPayerOk = true;
			boolean isStatusOk = true;

			if (currentProviderId != null) {
				if (t.getProvider() == null)
					isProviderOk = false;
				else if (currentProviderId != t.getProvider().getId())
					isProviderOk = false;
			}

			if (selectedPayerIdList != null && selectedPayerIdList.size() > 0) {
				isPayerOk = isTransactionExistInPayer(t.getPayerList());
			}

			if (!currentPayerStatus.isEmpty() && !currentPayerStatus.equals(t.getParStatus())) {
				isStatusOk = false;
			}

			if (isPayerOk && isProviderOk && isStatusOk)
				return true;
			else
				return false;

		};
		return predicate;
	}

	private Predicate<ProviderTransaction> filterByTransaction() {

		Predicate<ProviderTransaction> predicate = (t) -> {

			// boolean isProviderOk = true;
			boolean isPayerOk = true;
			boolean isStatusOk = true;

			if (selectedPayerIdList != null && selectedPayerIdList.size() > 0) {
				isPayerOk = isTransactionExistInPayer(t.getPayerList());
			}

			// if(currentPayerStatus != null &&
			// !currentPayerStatus.isEmpty() &&
			// !currentPayerStatus.equals(t.getParStatus()))
			// {
			// isStatusOk = false;
			// }

			if (isPayerOk && isStatusOk)
				return true;
			else
				return false;
		};

		return predicate;
	}

	private boolean isTransactionExistInPayer(Set<Payer> tmpPayerList) {

		Set<Integer> ids = tmpPayerList.stream().map(Payer::getId).collect(Collectors.toSet());

		Set<Integer> res = ids.stream().filter(selectedPayerIdList::contains).collect(Collectors.toSet());

		if (res.size() > 0)
			return true;
		else
			return false;
	}

	public void onPracticeChange() {

		providerList.clear();
	}
	// -------------------------

	private void cleanData() {
		// Transaction
		// transactions = new ArrayList<ProviderTransaction>();
		filteredTransactions = new ArrayList<ProviderTransaction>();

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
