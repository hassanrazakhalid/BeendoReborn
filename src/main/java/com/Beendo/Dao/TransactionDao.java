package com.Beendo.Dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.Beendo.Entities.ProviderTransaction;

@Repository
public class TransactionDao implements ICRUD<ProviderTransaction, Integer> {

	@Autowired
    private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public void save(ProviderTransaction entity) {
		
		this.sessionFactory.getCurrentSession().save(entity);
	}

	@Override
	@Transactional
	public void update(ProviderTransaction entity) {
		
		this.sessionFactory.getCurrentSession().update(entity);
	}

	@Override
	public void update(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ProviderTransaction findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(ProviderTransaction entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Transactional
	public List<ProviderTransaction> findAll() {
		// TODO Auto-generated method stub
		return this.sessionFactory.getCurrentSession().createQuery("FROM ProviderTransaction").list();
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ProviderTransaction refresh(ProviderTransaction sender) {
		// TODO Auto-generated method stub
		return null;
	}

}
