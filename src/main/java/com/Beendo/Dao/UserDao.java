 package com.Beendo.Dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.User;
import com.Beendo.Services.EntityService;
import com.Beendo.Utils.Role;


@Repository
public class UserDao implements IUserDao {

	@Autowired
    private SessionFactory sessionFactory;

	@Autowired
	private EntityService entityService;
	
	@Transactional
	public User isUserValid(String appUserName, String password){
		
		User user = null;
	
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM User U where U.appUserName = :appUserName AND U.password = :password");
		query.setParameter("appUserName", appUserName);
		query.setParameter("password", password);
		
		List<User> result = query.list();
		if(!result.isEmpty())
			user = result.get(0);
		
		return user;	
	}
	
	@Override
	@Transactional
	public User isUserValid(String userName){
		
		User user = null;
		
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM User U where U.appUserName = :appUserName");
		query.setParameter("appUserName", userName);
		
		List<User> result = query.list();
		if(!result.isEmpty())
			user = result.get(0);
		
		return user;
	}
	
	@Transactional
	public User finsUserByUserName(String email){
		
		User user = null;
		
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM User U where U.email = :email");
		query.setParameter("email", email);
		
		List<User> result = query.list();
		if(!result.isEmpty())
			user = result.get(0);
		
		return user;	
	}

	@Transactional
	public void save(User entity) {
		// TODO Auto-generated method stub
		this.sessionFactory.getCurrentSession().persist(entity);
//		this.sessionFactory.getCurrentSession().save(entity);
	}

	@Transactional
	public void update(User entity) {
		
		this.sessionFactory.getCurrentSession().merge(entity);
//		this.sessionFactory.getCurrentSession().update(entity);
	}

	public User findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public void delete(User entity) {
		// TODO Auto-generated method stub
	
		sessionFactory.getCurrentSession().delete(entity);
	}

	@Transactional()
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

	@Transactional
	@Override
	public List<User> findUserOtherThanRoot() {
		// TODO Auto-generated method stub
		
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM User U where U.roleName  not in ( :arg1, :arg2)");
		query.setParameter("arg1", Role.ROOT_ADMIN.toString());
		query.setParameter("arg2", Role.ROOT_USER.toString());
		
		List<User> result = query.list();
		return result;	
	}
	
	@Transactional
	public String isEntityAdminExist(Integer id){
		
		String error = null;
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM User U JOIN U.entity E WHERE U.roleName = :role AND E.id = :id");
		
		query.setParameter("id", id);
		query.setParameter("role", Role.ENTITY_ADMIN.toString());
		
		List<User> result = query.list();
		if(result.size() > 0)// means OK
		{
			error = "Admin already exist";
		}
		return error;
	}
	
	@Transactional
	@Override
	public String isUsernameExist(String userName){
		
		String error = null;
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM User U WHERE U.appUserName = :userName");
		
		query.setParameter("userName", userName.toLowerCase());
		
		List<User> result = query.list();
		if(result.size() > 0)// means OK
		{
			error = "Username already exist";
		}
		return error;
	}
	
	@Transactional
	@Override
	public String isEmailExist(String email){
		
		String error = null;
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM User U WHERE U.email = :email");
		
		query.setParameter("email", email.toLowerCase());
		
		List<User> result = query.list();
		if(result.size() > 0)// means OK
		{
			error = "Email already exist";
		}
		return error;
		
	}
	
/*	public List<User> getList(int page){
		
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class);
		criteria.add(criterion)
	}*/
}
