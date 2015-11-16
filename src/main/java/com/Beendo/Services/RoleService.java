package com.Beendo.Services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Beendo.Dao.IRole;
import com.Beendo.Entities.Role_Permission;

@Service
public class RoleService {

	private HashMap<Integer, Role_Permission> hashRole = new HashMap<>();;
	
	@Autowired
	private IRole service;
	
	@PostConstruct
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
	
}
