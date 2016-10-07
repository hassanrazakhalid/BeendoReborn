package com.Beendo.Services;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Beendo.Dao.IPayer;
import com.Beendo.Entities.Payer;
@Service
public class PayerService extends GenericServiceImpl<Payer, Integer> implements IPayerService {

	@Autowired
//	@Qualifier("payerDao")
	private IPayer service;	
	
	@Transactional(readOnly=true)
	public boolean isNameExist(String name) {
		
		return service.isPayerNameExist(name);
	}
/*	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	public List<Payer> isNameExist(List<Payer> entities, String name, String planName, String city, String state, String zip, String street){
		
		return filterData(entities, getNamePredicate(name, planName, city, state, zip, street));
	}
	
	private List<Payer> filterData (List<Payer> list, Predicate<Payer> predicate) {
        return list.stream().filter( predicate ).collect(Collectors.<Payer>toList());
    }
	
	private static Predicate<Payer> getNamePredicate(String name, String planName, String city, String state, String zip, String street){

		return p -> p.getName().equalsIgnoreCase(name) && p.getPlanName().equalsIgnoreCase(planName);
		 //return p -> p.getName().equalsIgnoreCase(name) && p.getPlanName().equalsIgnoreCase(planName) && p.getCity().equalsIgnoreCase(city) && p.getState().equalsIgnoreCase(state) && p.getZip().equalsIgnoreCase(zip) && p.getStreet().equalsIgnoreCase(street);
	 }*/
	
}
