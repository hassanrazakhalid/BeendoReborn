package com.Beendo.Services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Beendo.Dao.ITransaction;
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
	private ITransaction transactionDao;

	@Autowired
	private IProviderService providerService;

	@Autowired
	private IPractiseService practiseService;

	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	@Override
	public void reloadPracticeReportData(Consumer<Map<String,Object>> sender,int start, int end, int entityId, ReportType type) {
	
//		practiceList = practiseService.fetchAllByRole();
//		hashPractice.clear();
//
//		List<Provider> provList = providerService.fetchAllByRole();
//		practiceTransactions = transactionService.fetchAllByRole();

		
		Map<String, Object> result = new HashMap<>();
				
//		List<ProviderTransaction> listTransaction = transactionService.fetchAllByRole(start, end, entityId);
		List<Payer> listPayer = payerService.getAll();
//		result.put("transactions", listTransaction);
		result.put("payerList", listPayer);
		
		Integer totalRows = transactionDao.getTotalTransactionCount(entityId);
		result.put("count", totalRows);
		switch (type) {
		case ReportTypePractise:
			
			Object listPractise = practiseService.fetchAllByRole();
			result.put("practiseList", (List<Object>) listPractise);
			
			break;
		case ReportTypeProvider:
			
			Object listProvider = providerService.fetchAllByRole();
			result.put("providerList", (List<Object>) listProvider);
			
			break;
		case ReportTypeTransaction:
			
			break;

		default:
			break;
		}
		
//		List<Practice> listPractise = practiseService.getAll();
//		List<Provider> listProvider = providerService.getAll();
//		public void reloadPracticeReportData(List<Practice>practiseList, List<Provider>providerList, List<Payer>payerist, List<ProviderTransaction>listTransaction, ReportType type);
//		result.put("providerList", listProvider);
		
		sender.accept(result);
//		callBack.reloadPracticeReportData(listPractise,listProvider,listPayer,listTransaction,type);
	}

}
