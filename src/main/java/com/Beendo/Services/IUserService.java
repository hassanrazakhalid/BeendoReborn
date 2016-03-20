
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

import com.Beendo.Dao.ICRUD;
import com.Beendo.Dao.IUserDao;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.User;
import com.Beendo.Utils.Role;
import com.Beendo.Utils.SharedData;


public interface IUserService extends GenericService<User, Integer>{
	
//	public List<User> fetchAll();
	public User findById(Integer id, boolean shouldRedirect);
	public List<User> fetchAllByRole();	
	public User isUserValid(String appUserName, String password);
	public User isUserValid(String userName);
	public List<User> findUsersByEntityId(Integer id);
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException;
	// Security Methods
	public String isEntityAdminExist(Integer id);
	public String isUsernameExist(String userName);
	public String isEmailExist(String email);
	public List<Practice> findPracticesByUserId(Integer id);
	public boolean isUserExistForPractice(Integer practiceId);
}
