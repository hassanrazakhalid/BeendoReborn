package com.Beendo.Controllers;

import java.util.List;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.Transaction;
import com.Beendo.Entities.User;

public interface ProviderCallback {

	public void getProviderData(User user, List<Provider>providerList);
}
