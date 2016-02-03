package com.Beendo.Dao;

import java.util.List;

import com.Beendo.Entities.ProviderTransaction;

public interface ITransaction extends ICRUD<ProviderTransaction, Integer> {

	public List<ProviderTransaction> findTransactionsByEntity(Integer id);
	
	public void deleteTransactionByProvider(Integer id);
	public void deleteTransactionByPractics(Integer id);
}
