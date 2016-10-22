package com.Beendo.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Beendo.Dao.ITransaction;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.Transaction;
import com.Beendo.Entities.User;
import com.Beendo.Utils.SharedData;

@Service
public class TransactionService extends GenericServiceImpl<Transaction, Integer>
		implements ITransactionService {

	@Autowired
	@Qualifier("transactionDao")
	private ITransaction service;
	@Autowired
	private IPayerService payerService;
	@Autowired
	private IPractiseService practiseService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IProviderService providerService;

	@Autowired
	private IPractise practiseDao;
	
	@Transactional(readOnly = true)
	public List<Transaction> fetchAllByRole(int start, int end, int entityId) {

		String userRole = SharedData.getSharedInstace().getCurrentUser().getRoleName();
		final List<Transaction> dataList = new ArrayList<>();

//		if (SharedData.getSharedInstace().shouldReturnFullList()) {
//			dataList.addAll(getAll());
//		} else {
			service.findTransactionsByEntity(start, end, entityId, (List<Transaction> list) -> {

				dataList.addAll(list);
			});
//		}
		return dataList;
	}
	
	@Transactional(readOnly=true)
	public void fetchDataForTransactionCreation(Consumer<Map<String, Object>> sender, Integer entityId) {
		
		List<Provider> providerList = providerService.findProvidersByEntity(entityId);
		List<Practice> practiceList = practiseDao.findAllByEntity(entityId);
		List<Payer> payerList = payerService.getAll();
		
		Map<String, Object> response = new HashMap<>();
		response.put("practiceList", practiceList);
		response.put("providerList", providerList);
		response.put("payerList", payerList);
		
		sender.accept(response);
	}

	@Override
	@Transactional(readOnly = true)
	public void refreshAllData(int start, int end, int entityId,ITransactionCallback callBack) {

		User user = SharedData.getSharedInstace().getCurrentUser();
		user = userService.findById(user.getId(), false);

		List<Transaction> transactions =  new ArrayList<>();//this.fetchAllByRole(start, end, entityId);
		Integer totalRows = service.getTotalTransactionCount(entityId);
		List<Payer> payerList = payerService.getAll();
		List<Practice> practiceList = new ArrayList<>();//practiseService.fetchAllByRole();
		List<Provider> providerList = new ArrayList<>();//providerService.fetchAllByRole();

		Map<String,Object> otherInfo = new HashMap<>();
		otherInfo.put("count", totalRows);
		callBack.refreshAllData(user, transactions, payerList, practiceList, providerList, otherInfo);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Transaction> getLatestTransactions(Integer id) {
		
		return service.getLatestTransactions(id);
	}

	public void deleteTransactionByPractics(List<Integer> ids) {

		if (ids.size() > 0)
			service.deleteTransactionByPractice(ids);
	}

	public void deleteTransactionByProvider(List<Integer> ids) {

		if (ids.size() > 0)
			service.deleteTransactionByProvider(ids);
	}
}
