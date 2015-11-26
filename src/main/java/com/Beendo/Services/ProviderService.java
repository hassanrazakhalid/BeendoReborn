package com.Beendo.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Beendo.Dao.ICRUD;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Utils.SharedData;

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
	
	public List<Provider> fetchAll()
	{
		return service.findAll();
	}
	
	public List<Provider> fetchAllByUser()
	{
		List<Provider> resultList = null;
		if(SharedData.getSharedInstace().shouldReturnFullList())
			resultList = service.findAll();
		else
		{
			List<Provider> tmpList = new ArrayList<Provider>();
			
			List<Practice> practiseList = new ArrayList<Practice>();
			practiseList.addAll(SharedData.getSharedInstace().getCurrentUser().getEntity().getPracticeList());
			
			for (Practice practice : practiseList) {
			
				tmpList.addAll(practice.getProviders());
			}
			resultList = tmpList;
		}
		
		return resultList;
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
