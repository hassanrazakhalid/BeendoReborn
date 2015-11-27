package com.Beendo.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Beendo.Dao.ICRUD;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Practice;
import com.Beendo.Utils.SharedData;

@Service
public class PractiseService {

	@Autowired
	private ICRUD<Practice, Integer> iPractise;
	
	public void save(Practice user){
		
		iPractise.save(user);
	}
	
	public List<Practice> fetchAll(){
		
		return iPractise.findAll();
	}
	
	public List<Practice> fetchAllByUser(){
		
		List<Practice> resultList = null;
		if(SharedData.getSharedInstace().shouldReturnFullList())
			resultList = iPractise.findAll();
		else
		{
			List<Practice> tmpList = new ArrayList<Practice>();
			tmpList.addAll(SharedData.getSharedInstace().getCurrentUser().getPractises());
			resultList = tmpList;
		}
		
		return resultList;
	}

	public void update(Practice practise) {
		// TODO Auto-generated method stub
		iPractise.update(practise);
	}

	public static List<Practice> isNameExist(List<Practice> practises,String name){
		
		return filterEmployees(practises, getUserNamePredicate(name));
	}
	
	private static List<Practice> filterEmployees (List<Practice> practiseList, Predicate<Practice> predicate) {
        return practiseList.stream().filter( predicate ).collect(Collectors.<Practice>toList());
    }
	
	private static Predicate<Practice> getUserNamePredicate(String name){

		 return p -> p.getName().equalsIgnoreCase(name);
	 }

	public void remove(Practice sender) {
		// TODO Auto-generated method stub
		iPractise.delete(sender);
	}
	
	// GET practise ID From List
	
/*	public static Practice getPractiseIdByList(List<Practice> practises, Integer name){
		
		return filtePractiseId(practises, getPractiseIdPredicate(name));
	}
	
	private static Practice filtePractiseId (List<Practice> practiseList, Predicate<Practice> predicate) {
        return practiseList.stream().filter( predicate ).collect(Collectors.<Practice>toList());
    }
	
	private static Predicate getPractiseIdPredicate(Integer name){

		 return p -> p.getId().compareTo(name);
	 }
*/}
