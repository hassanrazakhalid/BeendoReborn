package com.Beendo.Dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.User;

@Repository
public class EntityDao extends GenericDao<CEntitiy, Integer> implements IEntity {

	
	@Autowired
    private SessionFactory sessionFactory;
	
	@Transactional
	@Override
	public List<CEntitiy> findAllPropertiesId(Integer id) {
		// TODO Auto-generated method stub
		Session session = this.sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("SELECT DISTINCT E FROM CEntitiy E"
				+ " LEFT JOIN FETCH E.practiceList"
				+ " LEFT JOIN FETCH E.users"
				+ " WHERE E.id=:id");
		query.setParameter("id", id);
		
		List<CEntitiy> result = query.list();
//		if(result.size() > 0)
			return result;
//		else
//			return null;
	}

	@Transactional
	@Override
	public CEntitiy findEntityById(Integer id) {
		// TODO Auto-generated method stub
		
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("SELECT DISTINCT E FROM CEntitiy E"
				+ " LEFT JOIN FETCH E.users"
				+ " LEFT JOIN FETCH E.practiceList"
				+ " where E.id = :id");
		query.setParameter("id", id);
		List<CEntitiy> result = query.list();
		if(result.size() > 0)
			return result.get(0);
		else
			return null;
	}

	@Transactional
	@Override
	public CEntitiy findEntityWithTransaction(Integer id){
		
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("SELECT DISTINCT E FROM CEntitiy E"
				+ " LEFT JOIN FETCH E.transactionList"
				+ " where E.id = :id");
		query.setParameter("id", id);
		List<CEntitiy> result = query.list();
		if(result.size() > 0)
			return result.get(0);
		else
			return null;
	}
	
	@Transactional
	@Override
	public List<CEntitiy> fetchAllExcept(Integer id) {
		// TODO Auto-generated method stub
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("SELECT DISTINCT E FROM CEntitiy E"
				+ " LEFT JOIN FETCH E.users"
				+ " LEFT JOIN FETCH E.practiceList"
				+ " where E.id != :id");
		query.setParameter("id", id);
		List<CEntitiy> result = query.list();
		return result;
	}
	
	@Transactional
	@Override
	public String isEntitynameExist(String name){
		
		String error = null;
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM CEntitiy U WHERE U.name = :name");
		
		query.setParameter("name", name);
		
		List<User> result = query.list();
		if(result.size() > 0)// means OK
		{
			error = "Entity name already exists!";
		}
		return error;
	}
	
	public CEntitiy findEntityWithPractisesById(Integer id) {
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(""
				+ " FROM CEntitiy E"
				+ " LEFT JOIN FETCH E.practiceList P"
				+ " WHERE E.id = :id");
		query.setParameter("id", id);
		CEntitiy result = (CEntitiy) query.uniqueResult();
		return result;
	}
	
	public List<CEntitiy> findEntityListWithPractises() {
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(""
				+ "SELECT DISTINCT E"
				+ " FROM CEntitiy E"
				+ " LEFT JOIN FETCH E.practiceList P"
				+ " WHERE E.id != 1");
//		query.setParameter("id", id);
		List<CEntitiy> result = query.list();
		return result;
	}
	
/*	@Transactional
	@Override
	public CEntitiy refresh(CEntitiy sender) {
		// TODO Auto-generated method stub
		Session session = this.sessionFactory.getCurrentSession();
		int count = sender.getPracticeList().size();
		sender = (CEntitiy) session.get(CEntitiy.class, sender.getId());
//		session.refresh(sender);
		count = sender.getPracticeList().size();
		System.out.println("");
		return sender;
	}*/
}
