package com.Beendo.Services;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Beendo.Dao.GenericDao;
import com.Beendo.Dao.ICRUD;

@Service
public abstract class GenericServiceImpl <E, K extends Serializable> implements GenericService<E, K> {

	@Autowired
	private ICRUD<E, K> genericDao;
	
    public GenericServiceImpl(GenericDao<E,K> genericDao) {
        this.genericDao=genericDao;
    }
    
    public GenericServiceImpl() {
    }
	
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	@Override
	public void saveOrUpdate(E entity) {
		// TODO Auto-generated method stub
		genericDao.saveOrUpdate(entity);
	}

	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	@Override
	public List<E> getAll() {
		// TODO Auto-generated method stub
		return genericDao.findAll();
	}

	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	@Override
	public E get(K id) {
		// TODO Auto-generated method stub
		return genericDao.find(id);
	}

	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	@Override
	public void add(E entity) {
		// TODO Auto-generated method stub
		genericDao.saveOrUpdate(entity);
	}

	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	@Override
	public void update(E entity) {
		// TODO Auto-generated method stub
		genericDao.saveOrUpdate(entity);
	}

	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	@Override
	public void remove(E entity) {
		// TODO Auto-generated method stub
		genericDao.remove(entity);
	}

}