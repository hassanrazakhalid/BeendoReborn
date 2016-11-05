package com.Beendo.Dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.Beendo.Entities.Transaction;

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
	public T getEntityByProfiles(Integer id, List<String> profiles){
		
	Session session = this.sessionFactory.getCurrentSession();
		for (String profile : profiles) {
			session.enableFetchProfile(profile);
		}
		T transaction = session.get(daoType, id);
		return transaction;
	}
	
	@Override
	public List<T> getEntitiesByProfiles(List<String> profiles){
		
	Session session = this.sessionFactory.getCurrentSession();
		for (String profile : profiles) {
			session.enableFetchProfile(profile);
		}
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery query = builder.createQuery(daoType);
		Root<T> root = query.from(daoType);
//		root.fetch("plans", JoinType.LEFT);
		query.select(root);
		List<T> result = session.createQuery(query).getResultList();
//		List<T> result = session.createCriteria(daoType).list();
		return result;
	}
	
	@Override
	public void remove(ID id){
		
	  T obj = (T) this.sessionFactory.getCurrentSession().get(daoType, id);
	  this.remove(obj);
	}
	
	@Override
	public List<T> findAll(){
		
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(daoType);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return  criteria.list();
	}
	
	public List<T> executeListQuery(String query) {
		
		Session session = sessionFactory.getCurrentSession();		
		return session.createQuery(query).getResultList();
	}
	
    public T executeSingleResultQuery(String query) {
    	
		Session session = sessionFactory.getCurrentSession();		
		return (T) session.createQuery(query).getSingleResult();
    }
}
