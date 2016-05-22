package com.Beendo.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Beendo.Dao.IEntity;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Utils.Role;
import com.Beendo.Utils.Screen;
import com.Beendo.Utils.SharedData;

public interface IEntityService extends GenericService<CEntitiy, Integer> {


	public List<CEntitiy> fetchAllEntitiesByUse();
	public List<CEntitiy> fetchAllByRole(Screen screen);
	public String isUsernameExist(String name);
	public CEntitiy findEntityWithTransaction(Integer id);
}