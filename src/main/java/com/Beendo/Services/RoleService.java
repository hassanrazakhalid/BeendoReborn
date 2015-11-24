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
import com.Beendo.Entities.Role_Permission;

@Service
public class RoleService {

	private HashMap<Integer, Role_Permission> hashRole = new HashMap<>();;
	
	@Autowired
	private IRole service;
		
//	@PostConstruct
	public void init(){
		 
		List<Role_Permission> list = service.findAll();
		
		for(int i =0; i < list.size(); i++)
		{
			Role_Permission role = list.get(i);
			hashRole.put(role.getId(), role);
		}
	}
	
	public List<Role_Permission> getRoleList(){
		
		Collection<Role_Permission> col =  hashRole.values();
		List<Role_Permission> list = new ArrayList(col);
		return list;
	}
	
	public void save(Role_Permission entity)
	{
		service.save(entity);
		hashRole.put(entity.getId(), entity);
	}
	
	public void update(Role_Permission entity)
	{
		service.update(entity);
	}
	
	public List<Role_Permission> findAll()
	{
		return service.findAll();
	}

	public Role_Permission getEntityById(int parseInt) {
		// TODO Auto-generated method stub
		return hashRole.get(parseInt);
	}
	
	
	public static List<Role_Permission> isNameExist(List<Role_Permission> entities, String name){
		
		return filterData(entities, getNamePredicate(name));
	}
	
	private static List<Role_Permission> filterData (List<Role_Permission> list, Predicate<Role_Permission> predicate) {
        return list.stream().filter( predicate ).collect(Collectors.<Role_Permission>toList());
    }
	
	private static Predicate<Role_Permission> getNamePredicate(String name){

		 return p -> p.getName().equalsIgnoreCase(name);
	 }
	
}
