package com.Beendo.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Beendo.Dao.IEntity;
import com.Beendo.Dao.IProvider;
import com.Beendo.Dao.ITransaction;
import com.Beendo.Dao.IUserDao;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.User;
import com.Beendo.Utils.Screen;
import com.Beendo.Utils.SharedData;

@Service
public class PractiseService extends GenericServiceImpl<Practice, Integer> implements IPractiseService {

	@Autowired
	private IPractise practiseDao;
	
	@Autowired
	private IEntity entityDao;
	@Autowired
	private IEntityService entityService;
	@Autowired
	private IUserDao userDao;
	@Autowired
	private IUserService userService;

	@Autowired
	private IProvider providerDao;

	@Autowired
	private ITransaction transactionDao;
		
	public List<Practice> fetchAllByUser(){
		
		List<Practice> resultList = null;
		if(SharedData.getSharedInstace().shouldReturnFullList())
			resultList = practiseDao.findAll();
		else
		{
			List<Practice> tmpList = new ArrayList<Practice>();
			tmpList.addAll(SharedData.getSharedInstace().getCurrentUser().getPractises());
			resultList = tmpList;
		}
		
		return resultList;
	}
	
	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	public void refreshAllData(IPracticeCallBack callBack){
		
		User tmpUser = userService.findById(SharedData.getSharedInstace().getCurrentUser().getId(), false);
		List<CEntitiy> listEntities = entityService.fetchAllByRole(Screen.Screen_Practice);
		List<Practice> listPractise = this.fetchAllByRole();
		callBack.refreshAllData(tmpUser, listEntities, listPractise);
	} 
	
	@Transactional(readOnly=true)
	public List<Practice> fetchAllByRole(){
		
		String userRole = SharedData.getSharedInstace().getCurrentUser().getRoleName();
		List<Practice> practiseList = new ArrayList<>();
		
		if(SharedData.getSharedInstace().shouldReturnFullList())
		{
			practiseList.addAll(practiseDao.findAll());
		}
		else
		{
//			if(userRole.equalsIgnoreCase(Role.ENTITY_ADMIN.toString()))
			{
//				CEntitiy entity = iEntity.refresh(SharedData.getSharedInstace().getCurrentUser().getEntity());
//				SharedData.getSharedInstace().getCurrentUser().setEntity(entity);
				List<Practice> result = practiseDao.findAllByEntity(SharedData.getSharedInstace().getCurrentUser().getEntity().getId());
				practiseList.addAll(result);
			}
//			else if(userRole.equalsIgnoreCase(Role.ENTITY_USER.toString()))
			{
//				List<Practice> result = iUser.findPracticesByUserId(SharedData.getSharedInstace().getCurrentUser().getId());
//				Set<Practice> res = new HashSet<Practice>(result);
//				SharedData.getSharedInstace().getCurrentUser().setPractises(res);
				
//				List<Practice> result = getPracticeByUser(SharedData.getSharedInstace().getCurrentUser().getId());
//				practiseList.addAll(result);
			}
		}
		
		return practiseList;
	}

	@Transactional(readOnly=true)
	public String checkDuplicateUsername(String name){
		
		return practiseDao.checkDuplicateUsername(name);
	}
	
	@Transactional(readOnly=true)
	public List<Practice> getPracticeByUser(Integer userId){
		
		return practiseDao.getPracticeByUser(userId);
	}
	
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	@Override
	public void remove(Practice entity) {
		
		
		try {
			
			super.remove(entity);
			
			//deleting files physcically from disk
			for (Provider provider : entity.getProviders()) {
				
				provider.removeAllDocumentOnDisk();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	// GET practise ID From List
	
/*	public static Practice getPractiseIdByList(List<Practice> practises, Integer name){
		
		return filtePractiseId(practises, getPractiseIdPredicate(name));
	}
	
	private static Practice filtePractiseId (List<Practice> practiseList, Predicate<Practice> predicate) {
        return practiseList.stream().filter( predicate ).collect(Collectors.<Practice>toList());
    }
	
	private static Predicate getPractiseIdPredicate(Integer name){

		 return p -> p.getId().compareTo(name);
	 }
*/}
