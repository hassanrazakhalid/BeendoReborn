package com.Beendo.Dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import static java.lang.Math.toIntExact;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.ProviderTransaction;
import com.Beendo.Utils.Constants;
import com.Beendo.Utils.SharedData;

@Repository
public class TransactionDao extends GenericDao<ProviderTransaction, Integer> implements ITransaction {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@Transactional(readOnly=true)
	public void findTransactionsByEntity( int start, int end, Integer entityId, TransactionPaginationCallback callback) {

//		" LEFT JOIN FETCH P.payerList Payer" +
		Session session = this.sessionFactory.getCurrentSession();
		
		String queryString = "SELECT P FROM ProviderTransaction P"
				+ " LEFT JOIN P.entity E";
		Query query = null;
		if (entityId == Constants.RootEntityId) {
			
			query = session
					.createQuery(queryString);
					query.setFirstResult(start);
					query.setMaxResults(end);
		}
		else {
			
			queryString +=" WHERE E.id = :id";
			query = session
					.createQuery(queryString);
					query.setFirstResult(start);
					query.setMaxResults(end);
					query.setParameter("id", entityId);
		}
		
		List<ProviderTransaction> result = query.getResultList();
		callback.getPaginationResponse(result);
//		return result;
	}
	
	public Integer getTotalTransactionCount(Integer entityId){
		
		//Total count
		String countQ = null;
		Query countQuery = null;
		if (entityId == Constants.RootEntityId){
		
			countQ = "SELECT count (P.id) FROM ProviderTransaction P";
			countQuery = sessionFactory.getCurrentSession().createQuery(countQ);
		}
		else {
			
			countQ = "SELECT count (P.id) FROM ProviderTransaction P"
					+ " LEFT JOIN P.entity E"
					+ " WHERE E.id = :id";
			countQuery = sessionFactory.getCurrentSession().createQuery(countQ);
			countQuery.setParameter("id", entityId);
		}
		
		Long countResult = (Long) countQuery.getSingleResult();
		return toIntExact(countResult);
		//Last Page
//		int pageSize = 10;
//		int lastPageNumber = (int) ((countResult / pageSize) + 1);
	}
	
	@Override
	public List<ProviderTransaction> findTransactionsByEntity(Integer id) {
		// TODO Auto-generated method stub
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session
				.createQuery("SELECT P FROM ProviderTransaction P" + " LEFT JOIN P.entity E" + " WHERE E.id = :id");
		// Query query = session.createQuery("FROM CEntitiy E"
		// + " LEFT JOIN E.transactionList T"
		// + " E.id = :id");
		query.setParameter("id", id);
		List<ProviderTransaction> result = query.getResultList();
		return result;
	}

//	@Override
//	public void deleteAll() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public ProviderTransaction refresh(ProviderTransaction sender) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public void deleteTransactionByPractice(List<Integer> ids) {
		// TODO Auto-generated method stub
		Session session = this.sessionFactory.getCurrentSession();
//		Query query = session.createQuery("DELETE FROM ProviderTransaction T" + " WHERE T.practice.id = :id");
		String queryStr = "DELETE FROM ProviderTransaction T" + " WHERE T.practice.id " + SharedData.getInString(ids);
		Query query = session.createQuery(queryStr);

		for (int i = 0; i < ids.size(); i++) {

			Integer integer = ids.get(i);
			query.setParameter("arg"+i,integer);
		}
		int result = query.executeUpdate();
		System.out.println("Affected Row: " + result);
	}

	@Override
	public void deleteTransactionByProvider(List<Integer> ids) {
		// TODO Auto-generated method stub
		Session session = this.sessionFactory.getCurrentSession();
		// Query query = session.createQuery("DELETE FROM ProviderTransaction T"
		// + " WHERE T.provider.id = :id");

		String queryStr = "DELETE FROM ProviderTransaction T" + " WHERE T.provider.id " + SharedData.getInString(ids);
		Query query = session.createQuery(queryStr);

		for (int i = 0; i < ids.size(); i++) {

			Integer integer = ids.get(i);
			query.setParameter("arg"+i,integer);
		}
//		query.setParameter("id", id);
		int result = query.executeUpdate();
		System.out.println("Affected Row: " + result);
	}
	
	@Override
	public List<ProviderTransaction> getLatestTransactions(Integer id) {
		
		String queryStr = "Select * from provider_transaction t"
				+ " inner join (SELECT tt.provider_id,MAX(transactionDate) as max_date"
				+ " FROM provider_transaction tt where tt.entity_id = " + id 
				+ " GROUP BY tt.provider_id)a on a.provider_id = t.provider_id and a.max_date = transactionDate";
		
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createNativeQuery(queryStr, ProviderTransaction.class);
		
		List<ProviderTransaction> res = query.getResultList();
		return res;
	}
	
	
}
