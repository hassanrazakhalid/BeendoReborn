package com.Beendo.Security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class CustomRoleHierachy implements RoleHierarchy {

	@Override
	public Collection<? extends GrantedAuthority> getReachableGrantedAuthorities(
			Collection<? extends GrantedAuthority> authorities) {
		// TODO Auto-generated method stub
		List<SimpleGrantedAuthority> grantedAuthority = new ArrayList<>();
		
		SimpleGrantedAuthority adminAuth = new SimpleGrantedAuthority("ROLE_ADMIN");
		SimpleGrantedAuthority adminAuth1 = new SimpleGrantedAuthority("ROLE_GUEST");
		SimpleGrantedAuthority adminAuth2 = new SimpleGrantedAuthority("ROLE_USER");
		
		grantedAuthority.add(adminAuth);
		grantedAuthority.add(adminAuth1);
		grantedAuthority.add(adminAuth2);
		
		return grantedAuthority;
	}

}
