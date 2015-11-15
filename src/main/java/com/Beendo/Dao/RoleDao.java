package com.Beendo.Dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.Beendo.Entities.Role_Permission;

@Repository
public class RoleDao implements ICRUD<Role_Permission, Integer> {

	@Autowired
    private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public void save(Role_Permission entity) {
		
		this.sessionFactory.getCurrentSession().save(entity);
	}

	@Transactional
	@Override
	public void update(Role_Permission entity) {
		
		this.sessionFactory.getCurrentSession().update(entity);
	}

	@Override
	public void update(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Role_Permission findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Role_Permission entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		
	}

	@Transactional
	@Override
	public List<Role_Permission> findAll() {
		
		return this.sessionFactory.getCurrentSession().createQuery("FROM Role_Permission").list();
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

}
