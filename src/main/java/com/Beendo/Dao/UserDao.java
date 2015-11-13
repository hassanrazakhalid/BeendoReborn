package com.Beendo.Dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.Beendo.Entities.User;


@Repository
public class UserDao implements IUserDao {

	@Autowired
    private SessionFactory sessionFactory;

	@Transactional
	public User isUserValid(String email, String password){
		
		User user = null;
	
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM User U where U.email = :email AND U.password = :password");
		query.setParameter("email", email);
		query.setParameter("password", password);
		
		List<User> result = query.list();
		if(!result.isEmpty())
			user = result.get(0);
		
		return user;	
	}

	@Transactional
	public void save(User entity) {
		// TODO Auto-generated method stub
		this.sessionFactory.getCurrentSession().save(entity);
	}

	@Transactional
	public void update(User entity) {
		this.sessionFactory.getCurrentSession().update(entity);
	}

	public User findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete(User entity) {
		// TODO Auto-generated method stub
		
	}

	@Transactional
	public List<User> findAll() {
		return sessionFactory.getCurrentSession().createQuery("From User").list();
	}

	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	public void update(int id) {
		// TODO Auto-generated method stub
		
	}

	@Transactional
	public void delete(int id) {
		User tmp = (User) sessionFactory.getCurrentSession().load(User.class, id);
		sessionFactory.getCurrentSession().delete(tmp);
		
	}
}
