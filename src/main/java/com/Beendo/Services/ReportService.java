package com.Beendo.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.ProviderTransaction;
import com.Beendo.Utils.ReportType;
import com.Beendo.Utils.SharedData;

@Service
public class ReportService extends GenericServiceImpl<ProviderTransaction, Integer> implements IReportService {

	@Autowired
	private IPayerService payerService;

	@Autowired
	private ITransactionService transactionService;

	@Autowired
	private IProviderService providerService;

	@Autowired
	private IPractiseService practiseService;
	
	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	@Override
	public void reloadPracticeReportData(IReportCallback callBack, ReportType type) {
	
//		practiceList = practiseService.fetchAllByRole();
//		hashPractice.clear();
//
//		List<Provider> provList = providerService.fetchAllByRole();
//		practiceTransactions = transactionService.fetchAllByRole();
		List<Practice> listPractise = null;
		List<Provider> listProvider = null;
		
		Integer entityId = SharedData.getSharedInstace().getCurrentUser().getEntity().getId();
		
		List<ProviderTransaction> listTransaction = null;// transactionService.refreshAllData(0, 10, entityId, callBack);;
		List<Payer> listPayer = payerService.getAll();
		switch (type) {
		case ReportTypePractise:
			
			listPractise = practiseService.fetchAllByRole();
			
			break;
		case ReportTypeProvider:
			
			listProvider = providerService.fetchAllByRole();
			
			break;
		case ReportTypeTransaction:
			
			break;

		default:
			break;
		}
//		public void reloadPracticeReportData(List<Practice>practiseList, List<Provider>providerList, List<Payer>payerist, List<ProviderTransaction>listTransaction, ReportType type);
	
		callBack.reloadPracticeReportData(listPractise,listProvider,listPayer,listTransaction,type);
	}

}
