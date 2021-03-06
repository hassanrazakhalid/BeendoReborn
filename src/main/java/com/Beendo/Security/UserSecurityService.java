package com.Beendo.Security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Beendo.Entities.Permission;
import com.Beendo.Utils.SharedData;


/*@Service
@Transactional
public class UserSecurityService implements UserDetailsService {

	static private UserSecurityService instance = null;
	
	 public UserSecurityService() {
	
		 if(instance == null)
			 instance = this;
	}
	 
	 public static UserSecurityService sharedInstance(){
		 return instance;
	 }
	
	@Override
	public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		com.Beendo.Entities.User domainUser = SharedData.getSharedInstace().currentUser;
        
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
 
        return new User(
                domainUser.getEmail(), 
                domainUser.getPassword(), 
                enabled, 
                accountNonExpired, 
                credentialsNonExpired, 
                accountNonLocked,
                getAuthorities(domainUser.getRole())
        );
	}

	public List<String> getRoles(Role_Permission role) {
		 
        List<String> roles = new ArrayList<String>();
        roles.add(role.getName());
//        roles.add("ROLE_MODERATOR");
        if (role.intValue() == 1) {
            roles.add("ROLE_MODERATOR");
            roles.add("ROLE_ADMIN");
        } else if (role.intValue() == 2) {
            roles.add("ROLE_MODERATOR");
        }
        return roles;
    }
     
	public Collection<? extends GrantedAuthority> getAuthorities(Role_Permission role) {
        List<GrantedAuthority> authList = getGrantedAuthorities(getRoles(role));
        return authList;
    }
	
    public static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
         
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
    
    // Logic Code
}*/