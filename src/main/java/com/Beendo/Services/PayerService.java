package com.Beendo.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Beendo.Dao.ICRUD;
import com.Beendo.Entities.Payer;

@Service
public class PayerService {

	@Autowired
	private ICRUD<Payer, Integer> service;
	
	public void save(Payer entity)
	{
		service.save(entity);
	}
	
	public void update(Payer entity)
	{
		service.update(entity);
	}
	
	public List<Payer> findAll()
	{
		return service.findAll();
	}
	
}
