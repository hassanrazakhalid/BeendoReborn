package com.Beendo.Dao;

import java.util.List;

import com.Beendo.Entities.ProviderTransaction;

@FunctionalInterface
public interface TransactionPaginationCallback {

	public void getPaginationResponse(List<ProviderTransaction> list);
	
}
