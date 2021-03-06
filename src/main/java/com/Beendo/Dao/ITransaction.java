package com.Beendo.Dao;

import java.util.List;

import com.Beendo.Entities.Transaction;
import com.Beendo.Utils.ReportFilter;

public interface ITransaction extends ICRUD<Transaction, Integer> {

	public List<Transaction> findTransactionsByEntity(Integer id);
	public void findTransactionsByEntity( int start, int end, Integer entityId, TransactionPaginationCallback callback); 
	
	public Integer getTotalTransactionCount(Integer entityId);
	public List<Transaction> getLatestTransactions(Integer id);
//	, Integer count
	
	public void deleteTransactionByProvider(List<Integer> ids);
	public void deleteTransactionByPractice(List<Integer> ids);
	
	public List<Transaction> getTransactionByProvider(ReportFilter filter);
	public Integer getPageSize(ReportFilter filter);
	
//	public Transaction getTransactionWithExtra(Integer id, List<String> profiles);
}
