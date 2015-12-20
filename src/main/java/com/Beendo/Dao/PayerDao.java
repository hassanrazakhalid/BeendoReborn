package com.Beendo.Dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.Beendo.Entities.Payer;

@Repository
public class PayerDao implements ICRUD<Payer, Integer> {

	@Autowired
    private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public void save(Payer entity) {
		
		this.sessionFactory.getCurrentSession().save(entity);
	}

	@Transactional
	@Override
	public void update(Payer entity) {
		
		this.sessionFactory.getCurrentSession().update(entity);	
	}

	@Override
	public void update(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Payer findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public void delete(Payer entity) {
		
		this.sessionFactory.getCurrentSession().delete(entity);
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		
	}

	@Transactional
	@Override
	public List<Payer> findAll() {
		
		return this.sessionFactory.getCurrentSession().createQuery("FROM Payer").list();
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

}
