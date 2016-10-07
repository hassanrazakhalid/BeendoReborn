package com.Beendo.Dao;

import java.util.List;

import com.Beendo.Entities.ProviderTransaction;

public interface ITransaction extends ICRUD<ProviderTransaction, Integer> {

	public List<ProviderTransaction> findTransactionsByEntity(Integer id);
	public void findTransactionsByEntity( int start, int end, Integer entityId, TransactionPaginationCallback callback); 
	
	public Integer getTotalTransactionCount(Integer entityId);
	public List<ProviderTransaction> getLatestTransactions(Integer id);
//	, Integer count
	
	public void deleteTransactionByProvider(List<Integer> ids);
	public void deleteTransactionByPractice(List<Integer> ids);
	
}
