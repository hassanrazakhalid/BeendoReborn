package com.Beendo.Services;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Beendo.Dao.ICRUD;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Provider;

@Service
public class ProviderService {

	@Autowired
	private ICRUD<Provider, Integer> service;
	
	public void save(Provider entity)
	{
		service.save(entity);
	}
	
	public void update(Provider entity)
	{
		service.update(entity);
	}
	
	public Provider findById(Integer id)
	{
		return service.findById(id);
	}
	
	public List<Provider> findAll()
	{
		return service.findAll();
	}
	
	
	public static List<Provider> isNameExist(List<Provider> entities, String name, String npi){
		
		return filterData(entities, getNamePredicate(name, npi));
	}
	
	private static List<Provider> filterData (List<Provider> list, Predicate<Provider> predicate) {
        return list.stream().filter( predicate ).collect(Collectors.<Provider>toList());
    }
	
	private static Predicate<Provider> getNamePredicate(String name, String npi){

		 return p -> p.getName().equalsIgnoreCase(name) && p.getName().equalsIgnoreCase(npi);
	 }
	
}
