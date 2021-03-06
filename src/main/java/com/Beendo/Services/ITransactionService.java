package com.Beendo.Services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Beendo.Controllers.TransactionViewModel;
import com.Beendo.Dao.ICRUD;
import com.Beendo.Dao.ITransaction;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.Transaction;
import com.Beendo.Utils.SharedData;

public interface ITransactionService extends GenericService<Transaction, Integer> {

	public void deleteTransactionByPractics(List<Integer> ids);

	public void deleteTransactionByProvider(List<Integer> ids);
	public void refreshAllData(int start, int end, int entityId, ITransactionCallback callBack) ;
	public List<Transaction> fetchAllByRole(int start, int end, int entityId);
	
	public List<Transaction> getLatestTransactions(Integer id);
	
	public void fetchDataForTransactionCreation(Consumer<Map<String, Object>> sender, Integer entityId);
	
	public Transaction getEntityByProfiles(Integer id, List<String> profiles);
	
	public void saveTransactions(List<Transaction> list);
	
	public void enterStartUpTransaction(Provider provider);
	public void enterStartUpTransaction(Practice practise);
}
