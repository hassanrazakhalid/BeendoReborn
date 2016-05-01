package com.Beendo.Dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.Beendo.Entities.Document;

@Repository
public class DocumentDao extends GenericDao<Document, Integer> implements IDocument {

	@Autowired
    private SessionFactory sessionFactory;
	
	@Override
	public List<Document> getDocumentByEmail(){
		
	
		Session session = this.sessionFactory.getCurrentSession();
		Query query =  session.createQuery("SELECT DISTINCT D FROM Document D"
				+ " WHERE D.reminderDate >= :currentDate AND D.reminderStatus <= 0");
		
		query.setParameter("currentDate", new Date());
		List<Document> result = query.list();
		
		return result;
	}
}
