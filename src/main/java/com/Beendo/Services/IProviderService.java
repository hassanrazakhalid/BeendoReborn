package com.Beendo.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Beendo.Dao.GenericDao;
import com.Beendo.Dao.ICRUD;
import com.Beendo.Dao.IProvider;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.User;
import com.Beendo.Utils.Role;
import com.Beendo.Utils.SharedData;

public interface IProviderService extends GenericService<Provider, Integer> {

	public List<Provider> fetchAllByRole();
	
	public String isNameExist(String name, String lname, String npi);
	
	public void updateProviderList(Set<Provider>list);
	
	public List<Provider> findProvidersByEntity(Integer id);
}
