package com.Beendo.Dao;

import java.util.Set;

import com.Beendo.Entities.Provider;

public interface IProvider extends ICRUD<Provider, Integer> {
	

	public String isNameExist(String name, String lname, String npi);
	public void updateProviderList(Set<Provider>list);
}
