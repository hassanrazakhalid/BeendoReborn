package com.Beendo.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Beendo.Dao.IUserDao;
import com.Beendo.Entities.User;
import com.Beendo.Utils.SharedData;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private IUserDao iUserDao;
	
	public void save(User user){
		
		iUserDao.save(user);
	}
	
	public List<User> fetchAll(){
		
		return iUserDao.findAll();
	}
	
	public User isUserValid(String appUserName, String password){
	
		User user =	iUserDao.isUserValid(appUserName, password);
		return user;
	}

	public void update(User user) {
		// TODO Auto-generated method stub
		iUserDao.update(user);
	}

	public void remove(User sender) {
		// TODO Auto-generated method stub
		iUserDao.delete(sender);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
//		User user =	iUserDao.finsUserByUserName(username);
		User user =	SharedData.getSharedInstace().getCurrentUser();
		return user;
	}	
	// Security Methods
	
}
