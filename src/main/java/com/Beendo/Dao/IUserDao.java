package com.Beendo.Dao;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

//import org.springframework.security.core.userdetails.UserDetails;

import org.hibernate.Session;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.User;

public interface IUserDao extends ICRUD <User, Integer> {

	public User isUserValid(String email, String password);
	public User isUserValid(String userName);
	public User finsUserByUserName(String email);
	
	public List<User> findUserOtherThanRoot();
	public String isUsernameExist(String userName);
	public String isEmailExist(String email);
	public String isEntityAdminExist(Integer entity);
	
	public Set<Practice> findPracticesByUserId(Integer id);
	
//	public UserDetails loadUserByUsername(String username);
}
