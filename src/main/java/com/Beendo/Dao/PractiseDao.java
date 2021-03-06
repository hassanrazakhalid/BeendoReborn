package com.Beendo.Dao;

import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.Beendo.Entities.Practice;
import com.Beendo.Entities.User;
import com.Beendo.Services.IPractise;
import com.Beendo.Utils.Constants;

@Repository
public class PractiseDao extends GenericDao<Practice, Integer> implements IPractise {

	
	@Autowired
    private SessionFactory sessionFactory;
	
/*	@Override
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
	public void delete(Practice practice) {
		// TODO Auto-generated method stub
		this.sessionFactory.getCurrentSession().delete(practice);
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		
	}*/

//	@Override
//	@Transactional
//	public List<Practice> findAll() {
//		// TODO Auto-generated method stub
//		return this.sessionFactory.getCurrentSession().createQuery("FROM Practice").list();
//	}

	@Override
	public List<Practice> findAllByEntity(Integer id) {
		// TODO Auto-generated method stub
		Session session = this.sessionFactory.getCurrentSession();
		
		Query query = null;
		
		if (id == Constants.RootEntityId) {
			query =  session.createQuery("FROM Practice P");
		}
		else {
			query =  session.createQuery("FROM Practice P"
//					+ " JOIN FETCH P."
					+ " WHERE P.entity.id =:id");
			query.setParameter("id", id);
		}
		
		
		
		return query.getResultList();
		
	}
	
//	@Override
//	public void deleteAll() {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public String checkDuplicateUsername(String name){
		
		String error = null;
//		sessionFactory.getCurrentSession().createNamedQuery(name)
		Query query = sessionFactory.getCurrentSession().createNamedQuery("FROM Practice P where P.name = :name");
		query.setParameter("name", name);
		
		List<Practice> result = query.getResultList();
		if(result.size() > 0)
			error =  "Practise already exist with duplcate name";
		return error;
	}
	
	@Override
	public void updatePractiseList(Set<Practice>list){
		 
		for (Practice practice : list) {
			
			this.sessionFactory.getCurrentSession().update(practice);
		}
	}

//	@Override
//	public Practice refresh(Practice sender) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	@Override
	public List<Practice> getPracticeByUser(Integer userId) {
		
		Session session = this.sessionFactory.getCurrentSession();
		boolean isExist = false;

		Query query  = session.createQuery("SELECT P FROM User U"
				+ " JOIN U.practises P"
				+ " WHERE U.id =:id");
		
		query.setParameter("id", userId);
		List<Practice> list = query.getResultList();
		
		return list;
	}

}
