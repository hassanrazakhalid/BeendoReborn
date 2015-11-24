package com.Beendo.Controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Practice;

public class RootController {

	private HashMap<Integer, CEntitiy > hashOne;
	private HashMap<Integer, Practice > hashTwo;
	
	public void initHashOne(List<CEntitiy> list){
		
			hashOne = new HashMap<>();
		
		updateHashOne(list);
	}
	
	public void updateHashOne(List<CEntitiy> list){
		
		for (CEntitiy object : list) {
			
			hashOne.put(object.getId(), object);
		}
	}
	
	public List<CEntitiy> getAllHashOne(){
		
		Collection<CEntitiy> col =  hashOne.values();
		List<CEntitiy> list = new ArrayList(col);
		return list;
	}
	
	public CEntitiy getEntityById(Integer id){
	
		return hashOne.get(id);
		}
// Practise Has Map
	
	public void initHashTwo(List<Practice> list){
		
			hashTwo = new HashMap<>();
		
		updateHashTwo(list);
	}
	
	public void updateHashTwo(List<Practice> list){
		
		for (Practice object : list) {
			
			hashTwo.put(object.getId(), object);
		}
	}
	
	public List<Practice> getAllHashTwo(){
		
		Collection<Practice> col =  hashTwo.values();
		List<Practice> list = new ArrayList(col);
		return list;
	}
	
	public void clearHashTwo(){
		hashTwo.clear();
	}
	
	public CEntitiy getPractiseById(Integer id){
	
		return hashOne.get(id);
		}
}
