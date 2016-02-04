package com.Beendo.Dao;

import java.util.List;

import com.Beendo.Entities.ProviderTransaction;

public interface ITransaction extends ICRUD<ProviderTransaction, Integer> {

	public List<ProviderTransaction> findTransactionsByEntity(Integer id);
	
	public void deleteTransactionByProvider(List<Integer> ids);
	public void deleteTransactionByPractics(Integer id);
	
}
