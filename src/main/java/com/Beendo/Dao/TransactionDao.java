package com.Beendo.Dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import static java.lang.Math.toIntExact;

import java.math.BigInteger;

import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Plan;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.Transaction;
import com.Beendo.Entities.Transaction_;
import com.Beendo.Utils.Constants;
import com.Beendo.Utils.ReportFilter;
import com.Beendo.Utils.SharedData;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.Builder;

@Repository
public class TransactionDao extends GenericDao<Transaction, Integer> implements ITransaction {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@Transactional(readOnly = true)
	public void findTransactionsByEntity(int start, int end, Integer entityId, TransactionPaginationCallback callback) {

		// " LEFT JOIN FETCH P.payerList Payer" +
		Session session = this.sessionFactory.getCurrentSession();

		String queryString = "SELECT P FROM Transaction P" + " LEFT JOIN P.entity E";
		Query query = null;
		if (entityId == Constants.RootEntityId) {

			query = session.createQuery(queryString);
			query.setFirstResult(start);
			query.setMaxResults(end);
		} else {

			queryString += " WHERE E.id = :id";
			query = session.createQuery(queryString);
			query.setFirstResult(start);
			query.setMaxResults(end);
			query.setParameter("id", entityId);
		}

		List<Transaction> result = query.getResultList();
		callback.getPaginationResponse(result);
		// return result;
	}

	public Integer getTotalTransactionCount(Integer entityId) {

		// Total count
		String countQ = null;
		Query countQuery = null;
		if (entityId == Constants.RootEntityId) {

			countQ = "SELECT count (P.id) FROM Transaction P";
			countQuery = sessionFactory.getCurrentSession().createQuery(countQ);
		} else {

			countQ = "SELECT count (P.id) FROM Transaction P" + " LEFT JOIN P.entity E" + " WHERE E.id = :id";
			countQuery = sessionFactory.getCurrentSession().createQuery(countQ);
			countQuery.setParameter("id", entityId);
		}

		Long countResult = (Long) countQuery.getSingleResult();
		return toIntExact(countResult);
		// Last Page
		// int pageSize = 10;
		// int lastPageNumber = (int) ((countResult / pageSize) + 1);
	}

	@Override
	public List<Transaction> findTransactionsByEntity(Integer id) {
		// TODO Auto-generated method stub
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session
				.createQuery("SELECT P FROM Transaction P" + " LEFT JOIN P.entity E" + " WHERE E.id = :id");
		// Query query = session.createQuery("FROM CEntitiy E"
		// + " LEFT JOIN E.transactionList T"
		// + " E.id = :id");
		query.setParameter("id", id);
		List<Transaction> result = query.getResultList();
		return result;
	}

	@Override
	public void deleteTransactionByPractice(List<Integer> ids) {
		// TODO Auto-generated method stub
		Session session = this.sessionFactory.getCurrentSession();
	
		String queryStr = "DELETE FROM Transaction T" + " WHERE T.practice.id " + SharedData.getInString(ids);
		Query query = session.createQuery(queryStr);

		for (int i = 0; i < ids.size(); i++) {

			Integer integer = ids.get(i);
			query.setParameter("arg" + i, integer);
		}
		int result = query.executeUpdate();
		System.out.println("Affected Row: " + result);
	}

	@Override
	public void deleteTransactionByProvider(List<Integer> ids) {
		// TODO Auto-generated method stub
		Session session = this.sessionFactory.getCurrentSession();
	
		String queryStr = "DELETE FROM Transaction T" + " WHERE T.provider.id " + SharedData.getInString(ids);
		Query query = session.createQuery(queryStr);

		for (int i = 0; i < ids.size(); i++) {

			Integer integer = ids.get(i);
			query.setParameter("arg" + i, integer);
		}
		// query.setParameter("id", id);
		int result = query.executeUpdate();
		System.out.println("Affected Row: " + result);
	}

	@Override
	public List<Transaction> getLatestTransactions(Integer id) {

		String queryStr = "Select * from transaction t"
				+ " inner join (SELECT tt.provider_id,MAX(transactionDate) as max_date"
				+ " FROM transaction tt where tt.entity_id = " + id
				+ " GROUP BY tt.provider_id)a on a.provider_id = t.provider_id and a.max_date = transactionDate";

		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createNativeQuery(queryStr, Transaction.class);

		List<Transaction> res = query.getResultList();
		return res;
	}

	@Override
	public Integer getPageSize(ReportFilter filter) {
		
		Session session = this.sessionFactory.getCurrentSession();
		
		String subQuery = "SELECT *" +
				" FROM transaction AS T"
					;
		switch (filter.getReportType()) {
		case ReportTypeProvider:
			subQuery += " GROUP BY T.provider_id";
			break;
		case ReportTypePractise:
			subQuery += " GROUP BY T.practice_id";
			break;
		case ReportTypeTransaction:
			break;
		}
		filter.setBaseQuery(subQuery);
		String queryString = "SELECT COUNT(*) FROM (" + filter.getQueryString() + ") t";
		
		Query query = session.createNativeQuery(queryString);
//				+ " ORDER BY t.transactionDate";
//		Query query = session.createQuery(queryString);

		BigInteger count = (BigInteger )query.getSingleResult();
//		Object res = query.getSingleResult();
//		Object res = query.getResultList();
		return count.intValue();
	}
	
//	@Override
//	public Integer getPageSize(ReportFilter filter) {
//		
//		Session session = this.sessionFactory.getCurrentSession();
//		
//		String subQuery = "SELECT COUNT(T)"
//				+ " FROM Transaction T"
////				+ " LEFT JOIN T.payer Pay"
////				+ " JOIN FETCH T.entity E";
//					;
//		switch (filter.getReportType()) {
//		case ReportTypeProvider:
////			subQuery += " LEFT JOIN T.provider Pro";
//			subQuery += " GROUP BY T.provider.id";
//			break;
//		case ReportTypePractise:
//			subQuery += " LEFT JOIN T.practice Pra";
//			subQuery += " GROUP BY Pra.id";
//			break;
//		case ReportTypeTransaction:
//			break;
//		}
////		filter.setBaseQuery(subQuery);
//		String queryString = subQuery;//filter.getQueryString();
//		
//		Query query = session.createQuery(queryString);
////				+ " ORDER BY t.transactionDate";
////		Query query = session.createQuery(queryString);
//
////		Long count = (Long )query.getSingleResult();
////		Object res = query.getSingleResult();
//		Object res = query.getResultList();
////		return count.intValue();
//		return 0;
//	}
//	@Override
//	public Transaction getTransactionWithProfiles(Integer id, List<String> profiles){
//		
//		Session session = this.sessionFactory.getCurrentSession();
//		
//		for (String profile : profiles) {
//			session.enableFetchProfile(profile);
//		}
//		Transaction transaction = session.get(Transaction.class, id);
//		return transaction;
//	}
	
	@Override
	public List<Transaction> getTransactionByProvider(ReportFilter filter) {
		
		Session session = this.sessionFactory.getCurrentSession();
		
		String baseQuery = "SELECT *" +
				" FROM transaction AS T"
				+ " LEFT JOIN payer AS P ON P.id = T.payer_id"
				+ " LEFT JOIN  transaction_plan AS planIds ON planIds.transaction_id = T.id"
				+ " LEFT JOIN plan AS PL ON PL.id = planIds.plan_id"
//				+ " WHERE T.id = 5"
					;
		
		switch (filter.getReportType()) {
		case ReportTypeProvider:
			baseQuery += " GROUP BY T.provider_id";
			break;
		case ReportTypePractise:
			baseQuery += " GROUP BY T.practice_id";
			break;
		case ReportTypeTransaction:
			break;
		}
		
		filter.setBaseQuery(baseQuery);
		String queryString =  filter.getQueryString();
		queryString += " ORDER BY t.transactionDate";
//		String queryString = "SELECT * FROM transaction T"
//				+ " INNER JOIN ( " + filter.getQueryString() + ")"
//				+ " subT ON subT.payer_id = T.";
	
		
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		
//		final CriteriaQuery<Long> mainQuery = builder.createQuery(Long.class);
//		Root<Transaction> mainRoot = mainQuery.from(Transaction.class);
//		Subquery<Transaction> subQuery = mainQuery.subquery(Transaction.class);
////		CriteriaQuery<Transaction> subQuery = builder.createQuery(Transaction.class);
//		Root<Transaction> root = subQuery.from( Transaction.class );
//		subQuery.select(root.get("id"));
//		builder.greatest(root.get( Transaction_.transactionDate ));
//		Path<Object> providerGroupBy = root.get("provider");
//		subQuery.groupBy(providerGroupBy);		
//////		if (!filter.getProviderIds().isEmpty()) {
////			
//			List<Integer>providersIds = new ArrayList<>();
//			providersIds.add(71);
//			providersIds.add(3);
//			
//			Expression<Integer> exp = root.get("provider").get("id");
//			Predicate providerPredicate = exp.in(providersIds);
//			subQuery.where(providerPredicate);
////		}
////		criteria.where(builder.equal(root.get("entity").get("id"), 2));
//		
////		criteria.select(root);
//
////		countCriteria.groupBy(rootCount.get("provider"));
//		mainQuery.select(builder.count(root));
		
//		queryString = "SELECT T FROM Transaction T LEFT JOIN FETCH T.plans WHERE T.id = 5";
//		Query query = session.createQuery(queryString, Transaction.class);
		Query query = session.createNativeQuery(queryString)
				.addEntity("T",Transaction.class)
				.addJoin("PL", "T.plans")
				.addEntity("T",Transaction.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		query.setFirstResult(filter.getStart());
		query.setMaxResults(filter.getMaxResults());
//				+ " ORDER BY t.transactionDate";
//		Query query = session.createQuery(queryString);

		List<Transaction> res = query.getResultList();
//		Object res = query.getSingleResult();
//		Object res = query.getResultList();
		return res;
	}   
	
//	@Override
//	public Transaction getEntityByProfiles(Integer id, List<String> profiles) {
//		// TODO Auto-generated method stub
//		Session session = this.sessionFactory.getCurrentSession();
//		
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<Transaction> query = builder.createQuery(Transaction.class);
//		Root<Transaction> root = query.from(Transaction.class);
//		query.select(root).where(
//				builder.equal(root.get("id"), id)
//				);
//	Fetch<Transaction, Payer> payerFetch =	root.fetch("payer", JoinType.LEFT);
//	Fetch<Payer, Plan> payerPlanFetch =	payerFetch.fetch("plans", JoinType.LEFT);
//		root.fetch("plans", JoinType.LEFT);
//		
//		
//		Transaction transaction = session.createQuery(query).getSingleResult();
//		return transaction;
//	}
}
