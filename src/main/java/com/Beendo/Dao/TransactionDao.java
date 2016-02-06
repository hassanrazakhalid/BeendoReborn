package com.Beendo.Dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.ProviderTransaction;
import com.Beendo.Utils.SharedData;

@Repository
public class TransactionDao implements ITransaction {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public void save(ProviderTransaction entity) {

		this.sessionFactory.getCurrentSession().save(entity);
	}

	@Override
	@Transactional
	public void update(ProviderTransaction entity) {

		this.sessionFactory.getCurrentSession().update(entity);
	}

	@Override
	public void update(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public ProviderTransaction findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public void delete(ProviderTransaction entity) {
		// TODO Auto-generated method stub

		this.sessionFactory.getCurrentSession().delete(entity);
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional
	public List<ProviderTransaction> findAll() {
		// TODO Auto-generated method stub
		return this.sessionFactory.getCurrentSession().createQuery("FROM ProviderTransaction").list();
	}

	@Override
	@Transactional
	public List<ProviderTransaction> findTransactionsByEntity(Integer id) {
		// TODO Auto-generated method stub
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session
				.createQuery("SELECT P FROM ProviderTransaction P" + " LEFT JOIN P.entity E" + " WHERE E.id = :id");
		// Query query = session.createQuery("FROM CEntitiy E"
		// + " LEFT JOIN E.transactionList T"
		// + " E.id = :id");
		query.setParameter("id", id);
		List<ProviderTransaction> result = query.list();
		return result;
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub

	}

	@Override
	public ProviderTransaction refresh(ProviderTransaction sender) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
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
	@Transactional
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
}
