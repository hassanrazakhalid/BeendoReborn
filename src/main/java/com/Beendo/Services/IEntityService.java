package com.Beendo.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
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
	
	public void getEntityWithPractises(Integer entityId, Consumer<List<CEntitiy>> res);
	public void getEntityWithPractisesAndBoardSpeciality(Integer entityId, Consumer<Map<String,Object>> res);
	
}
