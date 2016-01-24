package com.Beendo.Dao;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.Beendo.Entities.Practice;
import com.Beendo.Services.IPractise;

@Repository
public class PractiseDao implements IPractise {

	
	@Autowired
    private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public void save(Practice entity) {
		
		this.sessionFactory.getCurrentSession().save(entity);
	}

	@Override
	@Transactional
	public void update(Practice entity) {
		
		this.sessionFactory.getCurrentSession().update(entity);
	}

	@Override
	public void update(int id) {
		// TODO Auto-generated method stub
	
	}

	@Override
	public Practice findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public void delete(Practice entity) {
		// TODO Auto-generated method stub
		this.sessionFactory.getCurrentSession().delete(entity);
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Transactional
	public List<Practice> findAll() {
		// TODO Auto-generated method stub
		return this.sessionFactory.getCurrentSession().createQuery("FROM Practice").list();
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Transactional
	@Override
	public String checkDuplicateUsername(String name){
		
		String error = null;
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Practice P where P.name = :name");
		query.setParameter("name", name);
		
		List<Practice> result = query.list();
		if(result.size() > 0)
			error =  "Practise already exist with duplcate name";
		return error;
	}
	
	@Transactional
	@Override
	public void updatePractiseList(Set<Practice>list){
		 
		for (Practice practice : list) {
			
			this.sessionFactory.getCurrentSession().update(practice);
		}
	}

	@Override
	public Practice refresh(Practice sender) {
		// TODO Auto-generated method stub
		return null;
	}
}
