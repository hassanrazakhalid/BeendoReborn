package com.Beendo.Dao;

import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Utils.Constants;

@Repository
public class ProviderDao extends GenericDao<Provider, Integer> implements IProvider {

	
	@Autowired
    private SessionFactory sessionFactory;
	
//	@Transactional
//	@Override
//	public void save(Provider entity) {
//		
//		this.sessionFactory.getCurrentSession().save(entity);
//	}
//
//	@Transactional
//	@Override
//	public void update(Provider entity) {
//		
//		this.sessionFactory.getCurrentSession().update(entity);	
//	}

//	@Override
//	public void update(int id) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Transactional
//	@Override
//	public Provider findById(Integer id) {
//		
//		//return (Provider)this.sessionFactory.getCurrentSession().createQuery("FROM Provider").list().get(0);
//		
////		Query query = this.sessionFactory.getCurrentSession().createQuery("FROM Provider where id = :code ");
////		query.setParameter("code", id);
////		return (Provider)query.list().get(0);
//		
//		return (Provider)this.sessionFactory.getCurrentSession().get(Provider.class, id);
//	}

//	@Transactional
//	@Override
//	public void delete(Provider entity) {
//		
//		this.sessionFactory.getCurrentSession().delete(entity);	
//	}
//
//	@Override
//	public void delete(int id) {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public List<Provider> findAll() {
		
		return this.sessionFactory.getCurrentSession().createQuery("SELECT DISTINCT P FROM Provider P"
				+ " LEFT JOIN FETCH P.documents D").list();
	}

//	@Override
//	public void deleteAll() {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public String isNPIExist(String npi) {
		// TODO Auto-generated method stub
		String error = null;
		
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM Provider P where P.npiNum = :npi ");
		//Query query = session.createQuery("FROM Provider P where P.firstName = :name and P.npiNum = :npi and P.lastName = :lsname ");
		//query.setParameter("name", name);
		//query.setParameter("lsname", lname);
		query.setParameter("npi", npi); 
		List<Practice> result = query.getResultList();
		if(result.size() > 0)
			error = "Provider NPI already exists!";
		return error;
	}

//	@Override
//	public Provider refresh(Provider sender) {
//		// TODO Auto-generated method stub
//		return  null;
//	}

	@Override
	public void updateProviderList(Set<Provider>list){
		 
		for (Provider practice : list) {
			
			this.sessionFactory.getCurrentSession().update(practice);
		}
	}

	@Override
	public List<Provider> findProvidersByEntity(Integer id) {
		// TODO Auto-generated method stub
		Session session = this.sessionFactory.getCurrentSession();
		
//		Query quey = session.createQuery("SELECT DISTINCT P FROM Provider P"
//				+ " JOIN P.practiceList Pra"
//				+ " JOIN Pra.entity E"
//				+ " WHERE E.id =:id");
	
		Query quey = null;
		if (id == Constants.RootEntityId) {
			
			quey = session.createQuery("SELECT DISTINCT P FROM Provider P"
					+ " JOIN FETCH P.centity E"
					+ " LEFT JOIN FETCH E.practiceList "
					+ " LEFT JOIN FETCH P.documents ");
		}
		else {
			
			 quey = session.createQuery("SELECT DISTINCT P FROM Provider P"
						+ " JOIN FETCH P.centity E"
						+ " LEFT JOIN FETCH E.practiceList "
						+ " LEFT JOIN FETCH P.documents "
						+ " WHERE P.centity.id=:id");
				quey.setParameter("id", id);
		}	
		List<Provider> list = quey.getResultList();	
		return list;
	}
	
	@Override
	public Provider getProviderDetails(Integer id) {
		// TODO Auto-generated method stub
		Session session = this.sessionFactory.getCurrentSession();
		
		Query quey = session.createQuery("SELECT P FROM Provider P"
				+ " JOIN FETCH P.centity E"
				+ " LEFT JOIN FETCH E.practiceList "
				+ " LEFT JOIN FETCH P.documents "
				+ " WHERE P.id=:id");
		quey.setParameter("id", id);
		
		List<Provider> list = quey.getResultList();

		if(list.size() > 0)
			return list.get(0);
		else
			return null;
	}
	
	@Override
	public Provider getProviderDetailsNoFiles(Integer id){
		
		Session session = this.sessionFactory.getCurrentSession();
		
		Query quey = session.createQuery("SELECT P FROM Provider P"
				+ " LEFT JOIN FETCH P.qualitication"
				+ " LEFT JOIN FETCH P.otherInfo"
				+ " WHERE P.id=:id");
		quey.setParameter("id", id);
		
		Provider res = (Provider) quey.getSingleResult();
		return res;
	}
}
