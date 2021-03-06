package com.Beendo.Dao;

import java.util.List;
import java.util.Set;

import com.Beendo.Controllers.ProviderCallback;
import com.Beendo.Entities.Provider;

public interface IProvider extends ICRUD<Provider, Integer> {
	

	public String isNPIExist(String npi);
	public void updateProviderList(Set<Provider>list);
	public List<Provider> findProvidersByEntity(Integer id);
	public Provider getProviderDetails(Integer id);
	public Provider getProviderDetailsNoFiles(Integer id);
}
