package com.Beendo.Dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.Beendo.Entities.Provider;

@Repository
public class ProviderDao implements ICRUD<Provider, Integer> {

	
	@Autowired
    private SessionFactory sessionFactory;
	
	@Transactional
	@Override
	public void save(Provider entity) {
		
		this.sessionFactory.getCurrentSession().save(entity);
	}

	@Transactional
	@Override
	public void update(Provider entity) {
		
		this.sessionFactory.getCurrentSession().update(entity);	
	}

	@Override
	public void update(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Provider findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Provider entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		
	}

	@Transactional
	@Override
	public List<Provider> findAll() {
		
		return this.sessionFactory.getCurrentSession().createQuery("FROM Provider").list();
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

}
