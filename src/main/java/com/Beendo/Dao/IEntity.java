package com.Beendo.Dao;

import java.util.List;

import com.Beendo.Entities.CEntitiy;

public interface IEntity extends ICRUD <CEntitiy, Integer> {

	public List<CEntitiy> fetchAllExcept(Integer id);
	public String isEntitynameExist(String userName);
	public List<CEntitiy> findAllPropertiesId(Integer id);
}
