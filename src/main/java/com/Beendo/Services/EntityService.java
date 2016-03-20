package com.Beendo.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Beendo.Dao.IEntity;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Utils.Role;
import com.Beendo.Utils.Screen;
import com.Beendo.Utils.SharedData;

@Service
public class EntityService extends GenericServiceImpl<CEntitiy, Integer> implements IEntityService {

	@Autowired
	private IEntity _service;

	@Transactional(readOnly=true)
	public List<CEntitiy> fetchAllEntitiesByUse() {

		/*
		 * Collection<CEntitiy> col = hashEntities.values(); List<CEntitiy> list
		 * = new ArrayList(col); return list;
		 */
		List<CEntitiy> resultList = null;
		if (SharedData.getSharedInstace().shouldReturnFullList())
			resultList = _service.findAll();
		else {
			List<CEntitiy> tmpList = new ArrayList<CEntitiy>();
			tmpList.add(SharedData.getSharedInstace().getCurrentUser().getEntity());
			resultList = tmpList;
		}
		return resultList;
		// return (List<CEntitiy>) hashEntities.values();

	}

	@Transactional(readOnly=true)
	public List<CEntitiy> fetchAllByRole(Screen screen) {

		String userRole = SharedData.getSharedInstace().getCurrentUser().getRoleName();
		List<CEntitiy> entityList = new ArrayList<>();
		
		switch (screen) {
		
		case Screen_Entity:
		case Screen_Payer:
		case Screen_Practice:
		case Screen_Transaction:
		case Screen_User:
		case Screen_Provider:
		{
			if (SharedData.getSharedInstace().shouldReturnFullList()) {
				entityList.addAll(_service.fetchAllExcept(1));
			} else {
//				if (userRole.equalsIgnoreCase(Role.ENTITY_ADMIN.toString()))
				{
				entityList = _service.findAllPropertiesId(SharedData.getSharedInstace().getCurrentUser().getEntity().getId());
//					entityList.add(SharedData.getSharedInstace().getCurrentUser().getEntity());
				}
//				else if (userRole.equalsIgnoreCase(Role.ENTITY_USER.toString())) {
//					if (SharedData.getSharedInstace().getCurrentUser().getPermission().isCanProviderAdd())
//					{
//						CEntitiy entity = SharedData.getSharedInstace().getCurrentUser().getEntity();						
//						entityList.add(entity);
//					}
//				}
			}
		}
		default:
			break;
		}

		return entityList;
	}

	/*
	 * public CEntitiy getEntityById(Integer id) { return hashEntities.get(id);
	 * }
	 */

	@Transactional(readOnly=true)
	public String isUsernameExist(String name){
		
		return _service.isEntitynameExist(name);
	}
	
	
	@Transactional(readOnly=true)
	public CEntitiy findEntityWithTransaction(Integer id){
		
		return _service.findEntityWithTransaction(id);
	}
}
