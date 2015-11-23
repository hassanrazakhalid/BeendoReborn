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

import com.Beendo.Dao.ICRUD;
import com.Beendo.Dao.IEntity;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Practice;



@Service
public class EntityService {

	@Autowired
	private IEntity _service;
	
//	private List<CEntitiy> listEntities;
/*	private HashMap<Integer, CEntitiy> hashEntities = new HashMap<Integer, CEntitiy>();
	
	@PostConstruct
	private void init(){
		
	List<CEntitiy>	listEntities = this.findAll();
		for(int i=0; i < listEntities.size(); i++)
		{
			CEntitiy entity = listEntities.get(i);
			hashEntities.put(entity.getId(), entity);
		}
		
	}*/
	
	public List<CEntitiy> getAllEntities(){
		
/*		Collection<CEntitiy> col =  hashEntities.values();
		List<CEntitiy> list = new ArrayList(col);
		return list;*/
		return _service.findAll();
//		return (List<CEntitiy>) hashEntities.values();
		
	}

/*	public CEntitiy getEntityById(Integer id)
	{
		return hashEntities.get(id);
	}
*/	
	public void save(CEntitiy entity)
	{
		_service.save(entity);
//		hashEntities.put(entity.getId(), entity);
	}
	
	public void update(CEntitiy entity)
	{
		_service.update(entity);
	}
	
	public List<CEntitiy> findAll()
	{
		return _service.findAll();
	}
	
	public static List<CEntitiy> isNameExist(List<CEntitiy> entities, String name){
		
		return filterData(entities, getNamePredicate(name));
	}
	
	private static List<CEntitiy> filterData (List<CEntitiy> list, Predicate<CEntitiy> predicate) {
        return list.stream().filter( predicate ).collect(Collectors.<CEntitiy>toList());
    }
	
	private static Predicate<CEntitiy> getNamePredicate(String name){

		 return p -> p.getName().equalsIgnoreCase(name);
	 }
	
}
