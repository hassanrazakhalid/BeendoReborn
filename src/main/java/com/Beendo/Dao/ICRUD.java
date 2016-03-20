package com.Beendo.Dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.User;

public interface ICRUD <E , K extends Serializable> {

    public void saveOrUpdate(E entity) ;
    public void update(E entity) ;
    public void remove(E entity);
    public void remove(K id);
    public E find(K key);
    public List<E> findAll() ;

}
