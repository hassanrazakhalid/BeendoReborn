package com.Beendo.Dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

import com.Beendo.Entities.User;

public interface ICRUD <T , Id extends Serializable> {

	public void save(T entity);
	
	public void update(T entity);
	
	public void update(int id);
	
	public T findById(Id id);
	
	public void delete(T entity);
	
	public void delete(int id);
	
	public List<T> findAll();
	
	public void deleteAll();

}
