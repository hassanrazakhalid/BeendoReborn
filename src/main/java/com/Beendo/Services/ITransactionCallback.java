package com.Beendo.Services;

import java.util.List;
import java.util.Map;

import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.ProviderTransaction;
import com.Beendo.Entities.User;

public interface ITransactionCallback {

	public void refreshAllData(User user, List<ProviderTransaction>transactions, List<Payer>payerList, List<Practice>practiceList, List<Provider>providerList, Map<String,Object>otherInfo);
}
