package com.Beendo.Dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public abstract class GenericDao<T, ID extends Serializable> implements ICRUD<T, ID> {

	@Autowired
	private SessionFactory sessionFactory;
	
	protected Class<? extends T> daoType;
	
	@Override
	public void save(T obj){
		
		this.sessionFactory.getCurrentSession().saveOrUpdate(obj);
	}
	
	@Override
	public void update(T obj){
		
		this.sessionFactory.getCurrentSession().saveOrUpdate(obj);
	}
	
	@Override
	public void update(int id){
		
	}
	
	@Override
	public T findById(ID id){
		
		return (T) this.sessionFactory.getCurrentSession().get(daoType, id);
	}
	
	@Override
	public void delete(T obj){
		
		this.sessionFactory.getCurrentSession().delete(obj);
	}
	
	public void delete(int id){
		
	  T obj = (T) this.sessionFactory.getCurrentSession().get(daoType, id);
	  this.delete(obj);
	}
	
	@Override
	public List<T> findAll(){
		
		return  this.sessionFactory.getCurrentSession().createCriteria(daoType).list();
	}
	
	@Override
	public void deleteAll(){
		
		
	}
	
	@Override
	public T refresh(T sender){
		
		 this.sessionFactory.getCurrentSession().refresh(sender);
		 return sender;
	}
}
