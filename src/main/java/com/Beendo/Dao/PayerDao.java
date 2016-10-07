package com.Beendo.Dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.Beendo.Entities.Payer;

@Repository
public class PayerDao extends GenericDao<Payer, Integer> implements IPayer {

	@Autowired
    private SessionFactory sessionFactory;
	


	public boolean isPayerNameExist(String name){
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("SELECT P From Payer P"
				+ " WHERE P.name = :name");
		query.setParameter("name", name);
		
		List<Payer> payer = (List<Payer>)query.getResultList();
		
		if (payer != null &&
			!payer.isEmpty()	){
			
			return true;
		}
		else {
			return false;
		}
		
		
		
	}
}
