package com.Beendo.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Beendo.Dao.IUserDao;
import com.Beendo.Entities.User;

@Service
public class UserService {

	@Autowired
	private IUserDao iUserDao;
	
	public void save(User user){
		
		iUserDao.save(user);
	}
	
	public List<User> fetchAll(){
		
		return iUserDao.findAll();
	}
	
	public User isUserValid(String email, String password){
	
		User user =	iUserDao.isUserValid(email, password);
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
}
