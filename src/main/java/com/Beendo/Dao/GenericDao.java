package com.Beendo.Dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public abstract class GenericDao<T, ID extends Serializable> implements ICRUD<T, ID> {

	@Autowired
	private SessionFactory sessionFactory;
	
	protected Class<? extends T> daoType;
	
    public GenericDao() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        daoType = (Class) pt.getActualTypeArguments()[0];
    }
	
	@Override
	public void saveOrUpdate(T obj){
		
		this.sessionFactory.getCurrentSession().saveOrUpdate(obj);
	}
	
	@Override
	public void update(T obj){
		
		this.sessionFactory.getCurrentSession().saveOrUpdate(obj);
	}
	
	@Override
	public T find(ID id){
		
		return (T) this.sessionFactory.getCurrentSession().get(daoType, id);
	}
	
	@Override
	public void remove(T obj){
		
		this.sessionFactory.getCurrentSession().delete(obj);
	}
	
	@Override
	public void remove(ID id){
		
	  T obj = (T) this.sessionFactory.getCurrentSession().get(daoType, id);
	  this.remove(obj);
	}
	
	@Override
	public List<T> findAll(){
		
		return  this.sessionFactory.getCurrentSession().createCriteria(daoType).list();
	}
}
