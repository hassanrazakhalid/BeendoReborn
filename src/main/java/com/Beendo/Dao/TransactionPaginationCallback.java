package com.Beendo.Dao;

import java.util.List;

import com.Beendo.Entities.Transaction;

@FunctionalInterface
public interface TransactionPaginationCallback {

	public void getPaginationResponse(List<Transaction> list);
	
}
