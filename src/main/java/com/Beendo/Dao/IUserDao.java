package com.Beendo.Dao;

import java.io.Serializable;
import java.util.List;

//import org.springframework.security.core.userdetails.UserDetails;

import org.hibernate.Session;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.User;

public interface IUserDao extends ICRUD <User, Integer> {

	public User isUserValid(String email, String password);
	public User finsUserByUserName(String email);
	
	public List<User> findUserOtherThanRoot();
	public String isUsernameExist(String userName);
	public String isEntityAdminExist(Integer entity);
	
//	public UserDetails loadUserByUsername(String username);
}
