package com.Beendo.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Beendo.Dao.EntityDao;
import com.Beendo.Dao.ICRUD;
import com.Beendo.Entities.CEntitiy;



@Service
public class EntityService {

	@Autowired
	private ICRUD<CEntitiy, Integer> _service;
	
	public void save(CEntitiy entity)
	{
		_service.save(entity);
	}
	
	public void update(CEntitiy entity)
	{
		_service.update(entity);
	}
	
	public List<CEntitiy> findAll()
	{
		return _service.findAll();
	}
	
}
