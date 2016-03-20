package com.Beendo.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Beendo.Dao.ICRUD;
import com.Beendo.Dao.IProvider;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.User;
import com.Beendo.Utils.Role;
import com.Beendo.Utils.SharedData;

@Service
public class ProviderService extends GenericServiceImpl<Provider, Integer> implements IProviderService {

	@Autowired
	private IProvider service;
	
	@Override
	@Transactional(readOnly=true)
	public List<Provider> fetchAllByRole(){
		
		String userRole = SharedData.getSharedInstace().getCurrentUser().getRoleName();
		List<Provider> dataList = new ArrayList<>();
		
		if(SharedData.getSharedInstace().shouldReturnFullList())
		{
			dataList.addAll(getAll());
		}
		else
		{
			CEntitiy entity = SharedData.getSharedInstace().getCurrentUser().getEntity();
			
			List<Provider> tmpList = new ArrayList<Provider>();
			List<Practice> practiseList = new ArrayList<Practice>();
			
			tmpList.addAll(findProvidersByEntity(SharedData.getSharedInstace().getCurrentUser().getEntity().getId()));
//			practiseList.addAll(SharedData.getSharedInstace().getCurrentUser().getEntity().getPracticeList());
			
//			for (Practice practice : practiseList) {
//			
//				tmpList.addAll(practice.getProviders());
//			}
			
			dataList = tmpList;
		}
		
		return dataList;
	}
	
	public String isNameExist(String name, String lname, String npi){
		
		return service.isNameExist(name, lname, npi);
	}
	
	public void updateProviderList(Set<Provider>list){
		
		service.updateProviderList(list);
	}
	
	public List<Provider> findProvidersByEntity(Integer id){
		
		return service.findProvidersByEntity(id);
	}
	
/*	public static List<Provider> isNameExist(List<Provider> entities, String name, String npi){
		
		return filterData(entities, getNamePredicate(name, npi));
	}
	
	private static List<Provider> filterData (List<Provider> list, Predicate<Provider> predicate) {
		
		List<Provider> lst = new ArrayList();
		
		try
		{
			lst = list.stream().filter( predicate ).collect(Collectors.<Provider>toList());
		}
		catch(Exception ex)
		{}
		finally
		{
			return lst;
		}
    }
	
	private static Predicate<Provider> getNamePredicate(String name, String npi){

		 return p -> p.getName().equalsIgnoreCase(name) && p.getName().equalsIgnoreCase(npi);
	 }*/
	
}
