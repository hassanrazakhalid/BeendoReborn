package com.Beendo.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Beendo.Dao.IUserDao;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Permission;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.User;
import com.Beendo.Utils.Role;
import com.Beendo.Utils.SharedData;

@Service
public class UserService extends GenericServiceImpl<User, Integer> implements UserDetailsService,IUserService {

	@Autowired
	private IUserDao iUserDao;
	
	
	@Transactional(readOnly=true)
	public User findById(Integer id, boolean shouldRedirect){

		User user = iUserDao.find(id);
		if (user.getPermission() == null) {
			user.setPermission(new Permission());
		}
		
		if(user == null &&
		   shouldRedirect)
		{
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ex = fc.getExternalContext();
			try {
				ex.redirect("logout");
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		
		return user;
	}
	
	@Transactional(readOnly=true)
	public List<User> fetchAllByRole(){
		
		String userRole = SharedData.getSharedInstace().getCurrentUser().getRoleName();
		List<User> userList = new ArrayList<>();
		
		if(userRole.equalsIgnoreCase(Role.ROOT_ADMIN.toString()))
		{
			userList.addAll(iUserDao.findAll());
			userList = removeSelf(userList);
		}
		else if(userRole.equalsIgnoreCase(Role.ROOT_USER.toString()))
		{
			userList.addAll(iUserDao.findUserOtherThanRoot());
		}
		else if(userRole.equalsIgnoreCase(Role.ENTITY_ADMIN.toString()))
		{
			List<User> list = findUsersByEntityId(SharedData.getSharedInstace().getCurrentUser().getEntity().getId());
				userList.addAll(list);
				userList = removeSelf(userList);
		}
		
		return userList;
	}
	
	private List<User> removeSelf(List<User> list){
		
		for(int i=0; i < list.size(); i++)
		{
			User tmpUser = list.get(i);
			if(tmpUser.getId().compareTo(SharedData.getSharedInstace().getCurrentUser().getId()) == 0)
			{
				list.remove(i);
				break;
			}
		}
		
		return list;
	}
	
	@Transactional(readOnly=true)
	public User isUserValid(String appUserName, String password){
	
		User user =	iUserDao.isUserValid(appUserName, password);
		return user;
	}
	
	@Transactional(readOnly=true)
	public User isUserValid(String userName){
		
		User user =	iUserDao.isUserValid(userName);
		return user;
	}

////	@Transactional
//	public void update(User user) {
//		// TODO Auto-generated method stub
//		iUserDao.update(user);
//	}

//	public void remove(User sender) {
//		// TODO Auto-generated method stub
//		iUserDao.remove(sender);
//	}
	
//	@Transactional(readOnly=true)
	public List<User> findUsersByEntityId(Integer id){
		
		return iUserDao.findUsersByEntityId(id);
	}
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
//		User user =	iUserDao.finsUserByUserName(username);
//		User user =	SharedData.getSharedInstace().getCurrentUser();
		User user = isUserValid(userName);
		return user;
	}
	// Security Methods

	@Transactional(readOnly=true)
	public String isEntityAdminExist(Integer id){
		
		return iUserDao.isEntityAdminExist(id);
	} 	
	
	@Transactional(readOnly=true)
	public String isUsernameExist(String userName){
		
		return iUserDao.isUsernameExist(userName);
	}
	
	@Transactional(readOnly=true)
	public String isEmailExist(String email){
		
		return iUserDao.isEmailExist(email);
	}
	
	@Transactional(readOnly=true)
	public List<Practice> findPracticesByUserId(Integer id){
		
		return iUserDao.findPracticesByUserId(id);
	}
	
	@Transactional(readOnly=true)
	public boolean isUserExistForPractice(Integer practiceId){
		
		return iUserDao.isUserExistForPractice(practiceId);
	}
}
