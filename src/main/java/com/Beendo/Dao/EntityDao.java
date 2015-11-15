package com.Beendo.Dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Practice;

@Repository
public class EntityDao implements IEntity {

	
	@Autowired
    private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public void save(CEntitiy entity) {
		
		this.sessionFactory.getCurrentSession().save(entity);
	}

	@Override
	@Transactional
	public void update(CEntitiy entity) {
		
		this.sessionFactory.getCurrentSession().update(entity);
	}

	@Override
	public void update(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CEntitiy findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(CEntitiy entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Transactional
	public List<CEntitiy> findAll() {
		// TODO Auto-generated method stub
		return this.sessionFactory.getCurrentSession().createQuery("FROM CEntitiy").list();
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}
}
