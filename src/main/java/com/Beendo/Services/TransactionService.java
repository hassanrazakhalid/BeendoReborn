package com.Beendo.Services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Beendo.Dao.ICRUD;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.ProviderTransaction;
import com.Beendo.Utils.SharedData;

@Service
public class TransactionService {

	@Autowired
	private ICRUD<ProviderTransaction, Integer> service;
	
	
	public void save(ProviderTransaction entity)
	{
		service.save(entity);
	}
	
	public void update(ProviderTransaction entity)
	{
		service.update(entity);
	}
	
	public List<ProviderTransaction> findAll()
	{
		return service.findAll();
	}
	
	public List<ProviderTransaction> findAllByUser()
	{
		List<ProviderTransaction> tmpList = new ArrayList<ProviderTransaction>();
		tmpList.addAll(SharedData.getSharedInstace().getCurrentUser().getEntity().getTransactionList());
		
		return tmpList;
	}
	
}
