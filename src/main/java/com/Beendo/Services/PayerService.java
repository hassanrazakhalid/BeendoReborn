package com.Beendo.Services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Beendo.Dao.ICRUD;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Payer;

@Service
public class PayerService {

	@Autowired
	private ICRUD<Payer, Integer> service;	
	
	public void save(Payer entity)
	{
		service.save(entity);
	}
	
	public void update(Payer entity)
	{
		service.update(entity);
	}
	
	public List<Payer> findAll()
	{
		return service.findAll();
	}
	
	public static List<Payer> isNameExist(List<Payer> entities, String name, String planName, String city, String state, String zip, String street){
		
		return filterData(entities, getNamePredicate(name, planName, city, state, zip, street));
	}
	
	private static List<Payer> filterData (List<Payer> list, Predicate<Payer> predicate) {
        return list.stream().filter( predicate ).collect(Collectors.<Payer>toList());
    }
	
	private static Predicate<Payer> getNamePredicate(String name, String planName, String city, String state, String zip, String street){

		 return p -> p.getName().equalsIgnoreCase(name) && p.getPlanName().equalsIgnoreCase(planName) && p.getCity().equalsIgnoreCase(city) && p.getState().equalsIgnoreCase(state) && p.getZip().equalsIgnoreCase(zip) && p.getStreet().equalsIgnoreCase(street);
	 }
	
}
