package com.Beendo.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Beendo.Dao.ITransaction;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.ProviderTransaction;
import com.Beendo.Entities.User;
import com.Beendo.Utils.SharedData;

@Service
public class TransactionService extends GenericServiceImpl<ProviderTransaction, Integer>
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

	@Transactional(readOnly = true)
	public List<ProviderTransaction> fetchAllByRole(int start, int end, int entityId) {

		String userRole = SharedData.getSharedInstace().getCurrentUser().getRoleName();
		final List<ProviderTransaction> dataList = new ArrayList<>();

		if (SharedData.getSharedInstace().shouldReturnFullList()) {
			dataList.addAll(getAll());
		} else {
			// List<ProviderTransaction> tmpList = new
			// ArrayList<ProviderTransaction>();
			service.findTransactionsByEntity(start, end, entityId, (List<ProviderTransaction> list) -> {

				dataList.addAll(list);

			});
			// tmpList.addAll(result);

			// dataList = tmpList;
		}

		return dataList;
	}

	@Override
	@Transactional(readOnly = true)
	public void refreshAllData(int start, int end, int entityId,ITransactionCallback callBack) {

		User user = SharedData.getSharedInstace().getCurrentUser();
		user = userService.findById(user.getId(), false);

		List<ProviderTransaction> transactions =  new ArrayList<>();//this.fetchAllByRole(start, end, entityId);
		Integer totalRows = service.getTotalTransactionCount(entityId);
		List<Payer> payerList = payerService.getAll();
		List<Practice> practiceList = practiseService.fetchAllByRole();
		List<Provider> providerList = providerService.fetchAllByRole();

		Map<String,Object> otherInfo = new HashMap<>();
		otherInfo.put("count", totalRows);
		callBack.refreshAllData(user, transactions, payerList, practiceList, providerList, otherInfo);
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
