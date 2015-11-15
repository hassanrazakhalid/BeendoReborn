package com.Beendo.Dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.Beendo.Entities.Practice;

@Repository
public class PractiseDao implements ICRUD<Practice, Integer> {

	
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

}
