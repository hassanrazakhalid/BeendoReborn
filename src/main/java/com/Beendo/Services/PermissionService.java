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

import com.Beendo.Dao.IRole;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Permission;

//@Service
public class PermissionService {

	private HashMap<Integer, Permission> hashRole = new HashMap<>();;
	
	@Autowired
	private IRole service;
		
//	@PostConstruct
	public void init(){
		 
		List<Permission> list = service.findAll();
		
		for(int i =0; i < list.size(); i++)
		{
			Permission role = list.get(i);
			hashRole.put(role.getId(), role);
		}
	}
	
	public List<Permission> getRoleList(){
		
		/*Collection<Role_Permission> col =  hashRole.values();
		List<Role_Permission> list = new ArrayList(col);
		return list;*/
		
		return service.findAll();
	}
	
	public void save(Permission entity)
	{
//		service.save(entity);
		hashRole.put(entity.getId(), entity);
	}
	
	public void update(Permission entity)
	{
		service.update(entity);
	}
	
	public List<Permission> findAll()
	{
		return service.findAll();
	}

	public Permission getEntityById(int parseInt) {
		// TODO Auto-generated method stub
		return hashRole.get(parseInt);
	}
	
	
/*	public static List<Permission> isNameExist(List<Permission> entities, String name){
		
		return filterData(entities, getNamePredicate(name));
	}
	
	private static List<Permission> filterData (List<Permission> list, Predicate<Permission> predicate) {
        return list.stream().filter( predicate ).collect(Collectors.<Permission>toList());
    }
	
	private static Predicate<Permission> getNamePredicate(String name){

		 return p -> p.getName().equalsIgnoreCase(name);
	 }*/
	
}
