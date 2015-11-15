package com.Beendo.Services;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Beendo.Dao.ICRUD;
import com.Beendo.Entities.Practice;

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

	public void update(Practice practise) {
		// TODO Auto-generated method stub
		iPractise.update(practise);
	}

	public List<Practice> isNameExist(List<Practice> practises,String name){
		
		return filterEmployees(practises, getUserNamePredicate(name));
	}
	
	private static List<Practice> filterEmployees (List<Practice> practiseList, Predicate<Practice> predicate) {
        return practiseList.stream().filter( predicate ).collect(Collectors.<Practice>toList());
    }
	
	private static Predicate<Practice> getUserNamePredicate(String name){

		 return p -> p.getName().equalsIgnoreCase(name);
	 }
}
