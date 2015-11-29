package com.Beendo.Dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.Beendo.Entities.Permission;

@Repository
public class RoleDao implements IRole {

	@Autowired
    private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public void save(Permission entity) {
		
		this.sessionFactory.getCurrentSession().save(entity);
	}

	@Transactional
	@Override
	public void update(Permission entity) {
		
		this.sessionFactory.getCurrentSession().update(entity);
	}

	@Override
	public void update(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Permission findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Permission entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		
	}

	@Transactional
	@Override
	public List<Permission> findAll() {
		
		return this.sessionFactory.getCurrentSession().createQuery("FROM Role_Permission").list();
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

}
