package com.Beendo.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Beendo.Dao.ICRUD;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Provider;

@Service
public class ProviderService {

	@Autowired
	private ICRUD<Provider, Integer> service;
	
	public void save(Provider entity)
	{
		service.save(entity);
	}
	
	public void update(Provider entity)
	{
		service.update(entity);
	}
	
	public List<Provider> findAll()
	{
		return service.findAll();
	}
	
}
