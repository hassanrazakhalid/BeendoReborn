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

@Service
public class EntityService {

	@Autowired
	private IEntity _service;

	// private List<CEntitiy> listEntities;
	/*
	 * private HashMap<Integer, CEntitiy> hashEntities = new HashMap<Integer,
	 * CEntitiy>();
	 * 
	 * @PostConstruct private void init(){
	 * 
	 * List<CEntitiy> listEntities = this.findAll(); for(int i=0; i <
	 * listEntities.size(); i++) { CEntitiy entity = listEntities.get(i);
	 * hashEntities.put(entity.getId(), entity); }
	 * 
	 * }
	 */

	public List<CEntitiy> fetchAll() {
		return _service.findAll();
	}

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
	public void save(CEntitiy entity) {
//		entity.setName(entity.getName().toLowerCase());
		_service.save(entity);
		// hashEntities.put(entity.getId(), entity);
	}

	public void update(CEntitiy entity) {
		_service.update(entity);
	}
	
	public void delete(CEntitiy entity){
		
		_service.delete(entity);
	}
	
	public void refresh(CEntitiy sender){
		
		_service.refresh(sender);
	}
	
	public String isUsernameExist(String name){
		
		return _service.isEntitynameExist(name);
	}
	
	public CEntitiy fetchById(Integer id){
		return _service.findEntityById(id);
	}
	
	public CEntitiy findEntityWithTransaction(Integer id){
		
		return _service.findEntityWithTransaction(id);
	}

/*	public static List<CEntitiy> isNameExist(List<CEntitiy> entities, String name) {

		return filterData(entities, getNamePredicate(name));
	}*/

	private static List<CEntitiy> filterData(List<CEntitiy> list, Predicate<CEntitiy> predicate) {

		List<CEntitiy> result = null;
		try {
			result = list.stream().filter(predicate).collect(Collectors.<CEntitiy> toList());
		} catch (Exception e) {
			System.out.println(e);
			// TODO: handle exception
		}
		return result;
	}

	private static Predicate<CEntitiy> getNamePredicate(String name) {

		return p -> p.getName().equalsIgnoreCase(name);
	}

}
