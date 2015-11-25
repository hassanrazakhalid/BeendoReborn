package com.Beendo.Entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "USERS")
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "User_Id")
	private Integer id;
	private String name;
	private String appUserName;
	private String email;
	private String password;
	private Boolean canCreateUser;

	@OneToOne(fetch = FetchType.EAGER)
	private Role_Permission role;

	@OneToOne(fetch = FetchType.EAGER)
	private CEntitiy entity;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Practice> practises = new HashSet<>(0);

	public static User copy(User sender) {

		User copyUser = new User();
		copyUser.setEmail(sender.email);
		copyUser.setName(sender.name);
		copyUser.setCanCreateUser(sender.canCreateUser);
		copyUser.setEntity(sender.entity);
		copyUser.setPractises(sender.practises);
		return copyUser;
	}

	// User Security
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub

		String role = this.getRole().getName();

//		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//		authorities.add(new SimpleGrantedAuthority("ROLE_QA"));
//		return authorities;
		
		Collection<Role_Permission> roleList = new ArrayList<Role_Permission>();
		roleList.add(getRole());
		return roleList;
	}

	public void logout() {

		System.out.println("In user login");

	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return getAppUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.valueOf(getId());
	}
}
