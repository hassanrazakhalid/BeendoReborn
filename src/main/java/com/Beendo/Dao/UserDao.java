 package com.Beendo.Dao;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Practice;
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
	@Transactional(readOnly=true)
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

	@Transactional
	public User findById(Integer id) {
		// TODO Auto-generated method stub
		Session session = this.sessionFactory.getCurrentSession();
		User user = (User)session.get(User.class, id);
		return user;
	}

	@Transactional
	public void delete(User entity) {
		// TODO Auto-generated method stub
	
		sessionFactory.getCurrentSession().delete(entity);
	}

	@Transactional()
	public List<User> findAll() {
		return sessionFactory.getCurrentSession().createQuery("SELECT DISTINCT U From User U"
				+ " LEFT JOIN FETCH U.practises"
				+ " LEFT JOIN FETCH U.entity E"
				+ " LEFT JOIN FETCH E.practiceList").list();
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
		Query query = session.createQuery("FROM User U"
				+ " LEFT JOIN FETCH U.practises"
				+ " WHERE U.roleName  not in ( :arg1, :arg2)");
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

	@Override
	@Transactional
	public User refresh(User sender) {
		
		Session session = this.sessionFactory.getCurrentSession();
		session.refresh(sender);
		return sender;
	}
	
	@Transactional
	@Override
	public List<Practice> findPracticesByUserId(Integer id) {
		
		Session session = this.sessionFactory.getCurrentSession();
		Query query  = session.createQuery("SELECT U.practises FROM User U"
				+ " JOIN U.practises P"
				+ " WHERE U.id =:id");
		query.setParameter("id", id);
		
		List<Practice> list = query.list();
		
//		for (Practice practice : list) {
//			
//			practice.getEntity();
//		}
		
//		criteria.add(Restrictions.eq("practice.id", id));
//		return criteria.list();
		
		return list;
	}
/*	public List<User> getList(int page){
		
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class);
		criteria.add(criterion)
	}*/

	@Transactional
	@Override
	public List<User> findUsersByEntityId(Integer id) {
		
		Session session = this.sessionFactory.getCurrentSession();
		
//		Query query  = session.createQuery("SELECT distinct E.users"
//				+ " FROM CEntitiy E"
//				+ " JOIN FETCH E.users U"
////				+ "	JOIN FETCH U.practises"
//				+ " WHERE U.entity.id =:id");
		Query query  = session.createQuery("SELECT DISTINCT U FROM User U"
//				+ " fetch all properties"
				+ " LEFT JOIN FETCH U.entity E"
				+ "	LEFT JOIN FETCH U.practises"
				+ " LEFT JOIN FETCH E.practiceList"
				+ " WHERE U.entity.id =:id");

		
		query.setParameter("id", id);
		List<User> list = query.list();
		
//		for (User user : list) {
//		
//			Set<Practice>pList =  user.getEntity().getPracticeList();
//			System.out.println("");
//		}
		 
		
		return list;
	}
	
	@Transactional
	@Override
	public boolean isUserExistForPractice(Integer practiceId) {
		
		Session session = this.sessionFactory.getCurrentSession();
		boolean isExist = false;

		Query query  = session.createQuery("SELECT DISTINCT U FROM User U"
				+ " JOIN U.practises P"
				+ " WHERE P.id =:id");
		
		query.setParameter("id", practiceId);
		List<User> list = query.list();
		if(list.size() > 0)
			isExist = true;
		return isExist;
	}
}
