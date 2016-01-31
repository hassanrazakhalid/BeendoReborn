package com.Beendo.Services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Beendo.Dao.IEntity;
import com.Beendo.Dao.IUserDao;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Utils.Role;
import com.Beendo.Utils.SharedData;

@Service
public class PractiseService {

	@Autowired
	private IPractise iPractise;
	
	@Autowired
	private IEntity iEntity;
	
	@Autowired
	private IUserDao iUser;
	
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
	
	public List<Practice> fetchAllByRole(){
		
		String userRole = SharedData.getSharedInstace().getCurrentUser().getRoleName();
		List<Practice> practiseList = new ArrayList<>();
		
		if(SharedData.getSharedInstace().shouldReturnFullList())
		{
			practiseList.addAll(iPractise.findAll());
		}
		else
		{
			if(userRole.equalsIgnoreCase(Role.ENTITY_ADMIN.toString()))
			{
//				CEntitiy entity = iEntity.refresh(SharedData.getSharedInstace().getCurrentUser().getEntity());
//				SharedData.getSharedInstace().getCurrentUser().setEntity(entity);
				List<Practice> result = iPractise.findAllByEntity(SharedData.getSharedInstace().getCurrentUser().getEntity().getId());
				practiseList.addAll(result);
			}
			else if(userRole.equalsIgnoreCase(Role.ENTITY_USER.toString()))
			{
				List<Practice> result = iUser.findPracticesByUserId(SharedData.getSharedInstace().getCurrentUser().getId());
				Set<Practice> res = new HashSet<Practice>(result);
				SharedData.getSharedInstace().getCurrentUser().setPractises(res);
				practiseList.addAll(SharedData.getSharedInstace().getCurrentUser().getPractises());
			}
		}
		
		return practiseList;
	}


	public void update(Practice practise) {
		// TODO Auto-generated method stub
		iPractise.update(practise);
	}

	public String checkDuplicateUsername(String name){
		
		return iPractise.checkDuplicateUsername(name);
	}
	
/*	public static List<Practice> isNameExist(List<Practice> practises,String name){
		
		return filterEmployees(practises, getUserNamePredicate(name));
	}*/
	
	private static List<Practice> filterEmployees (List<Practice> practiseList, Predicate<Practice> predicate) {
        return practiseList.stream().filter( predicate ).collect(Collectors.<Practice>toList());
    }
	
/*	private static Predicate<Practice> getUserNamePredicate(String name){

		 return p -> p.getName().equalsIgnoreCase(name);
	 }*/

	public void remove(Practice sender) {
		// TODO Auto-generated method stub
		iPractise.delete(sender);
	}
	
	public void updatePractiseList(Set<Practice>list){
		
		iPractise.updatePractiseList(list);
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
