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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.Beendo.Utils.Role;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 935900234703373675L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String appUserName;
	private String email;
	private String password;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Permission permission = new Permission();

	private String roleName;

	/*
	 * @OneToOne(fetch = FetchType.EAGER) private CEntitiy entity;
	 */
	// , cascade={CascadeType.REFRESH,CascadeType.MERGE,CascadeType.PERSIST}
	@ManyToOne(fetch = FetchType.EAGER)
	private CEntitiy entity;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,
			CascadeType.DETACH }, fetch = FetchType.LAZY)
	@JoinTable(name = "users_practice", joinColumns = @JoinColumn(name = "User_id"), inverseJoinColumns = @JoinColumn(name = "practises_id"))
	private Set<Practice> practises = new HashSet<>(0);

	public static User copy(User sender) {

		User copyUser = new User();
		copyUser.setEmail(sender.email);
		copyUser.setName(sender.name);
		copyUser.setEntity(sender.entity);
		copyUser.setPractises(sender.practises);
		return copyUser;
	}

	// User Security
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub

		String roleStr = this.getRoleName();

		// SimpleGrantedAuthority simpleRole = new
		// SimpleGrantedAuthority(roleStr);
		SimpleGrantedAuthority simpleRole = new SimpleGrantedAuthority("ROLE_" + roleStr);
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		// authorities.add(new SimpleGrantedAuthority("ROLE_ENTITY_USER"));
		// return authorities;
		authorities.add(simpleRole);
		return authorities;
	}

	public void logout() {

		System.out.println("In user login");

	}

	public Role getRole() {

		Role r = Role.getRole(roleName);
		return r;
	}

	public boolean canCreateTransaction() {

		if (roleName.equalsIgnoreCase(Role.ROOT_ADMIN.toString())
				|| roleName.equalsIgnoreCase(Role.ROOT_USER.toString())
				|| roleName.equalsIgnoreCase(Role.ENTITY_ADMIN.toString())
				|| getPermission().isCanPayerTransactionAdd()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean canEditTransaction() {

		if (roleName.equalsIgnoreCase(Role.ROOT_ADMIN.toString())
				|| roleName.equalsIgnoreCase(Role.ROOT_USER.toString())
				|| roleName.equalsIgnoreCase(Role.ENTITY_ADMIN.toString())
				|| getPermission().isCanPayerTransactionEdit()) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isSuperAdmin() {

		if (this.getAppUserName().equalsIgnoreCase("SuperAdmin")
				&& this.getAppUserName().equalsIgnoreCase("Password")) {
			return true;
		} else
			return false;
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
