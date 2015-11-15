package com.Beendo.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Beendo.Dao.ICRUD;
import com.Beendo.Entities.Role_Permission;

@Service
public class RoleService {

	@Autowired
	private ICRUD<Role_Permission, Integer> service;
	
	public void save(Role_Permission entity)
	{
		service.save(entity);
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
