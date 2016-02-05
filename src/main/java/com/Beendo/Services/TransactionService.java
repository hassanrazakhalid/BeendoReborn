package com.Beendo.Services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.Beendo.Dao.ICRUD;
import com.Beendo.Dao.ITransaction;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.ProviderTransaction;
import com.Beendo.Utils.SharedData;

@Service
public class TransactionService {

	@Autowired
	@Qualifier("transactionDao")
	private ITransaction service;

	public void save(ProviderTransaction entity) {
		service.save(entity);
	}

	public void update(ProviderTransaction entity) {
		service.update(entity);
	}

	public List<ProviderTransaction> findAll() {
		return service.findAll();
	}

	public List<ProviderTransaction> findAllByUser() {
		List<ProviderTransaction> tmpList = new ArrayList<ProviderTransaction>();
		tmpList.addAll(SharedData.getSharedInstace().getCurrentUser().getEntity().getTransactionList());

		return tmpList;
	}

	public List<ProviderTransaction> fetchAllByRole() {

		String userRole = SharedData.getSharedInstace().getCurrentUser().getRoleName();
		List<ProviderTransaction> dataList = new ArrayList<>();

		if (SharedData.getSharedInstace().shouldReturnFullList()) {
			dataList.addAll(findAll());
		} else {
			List<ProviderTransaction> tmpList = new ArrayList<ProviderTransaction>();
			List<ProviderTransaction> result = service
					.findTransactionsByEntity(SharedData.getSharedInstace().getCurrentUser().getEntity().getId());
			tmpList.addAll(result);

			dataList = tmpList;
		}

		return dataList;
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
