package com.Beendo.Services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Beendo.Dao.ICRUD;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Payer;

@Service
public class PayerService {

	@Autowired
	private ICRUD<Payer, Integer> service;
	
	private HashMap<Integer, Payer> hashEntities = new HashMap<Integer, Payer>();
	
	
	@PostConstruct
	private void init(){
		
	List<Payer>	listEntities = service.findAll();
		for(int i=0; i < listEntities.size(); i++)
		{
			Payer entity = listEntities.get(i);
			hashEntities.put(entity.getId(), entity);
		}
		
	}
	
	
	public void save(Payer entity)
	{
		service.save(entity);
		hashEntities.put(entity.getId(), entity);
	}
	
	public void update(Payer entity)
	{
		service.update(entity);
	}
	
	public List<Payer> findAll()
	{
		Collection<Payer> col =  hashEntities.values();
		List<Payer> list = new ArrayList(col);
		return list;
		//return service.findAll();
	}

	public Payer getEntityById(int id) {
		
		return hashEntities.get(id);
	}
	
}
