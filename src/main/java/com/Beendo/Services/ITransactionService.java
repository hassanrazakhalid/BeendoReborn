package com.Beendo.Services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Beendo.Dao.ICRUD;
import com.Beendo.Dao.ITransaction;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.ProviderTransaction;
import com.Beendo.Utils.SharedData;

public interface ITransactionService extends GenericService<ProviderTransaction, Integer> {

	public List<ProviderTransaction> findAllByUser();

	@Transactional(readOnly=true)
	public List<ProviderTransaction> fetchAllByRole();

	public void deleteTransactionByPractics(List<Integer> ids);

	public void deleteTransactionByProvider(List<Integer> ids);
}
