package com.Beendo.Entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "USERS")
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="User_Id")
	private Integer id;
	private String name;
	private String email;
	private String password;
	private Boolean canCreateUser;
	
	@OneToOne(fetch=FetchType.EAGER)
	private Role_Permission role;
	
	@OneToOne(fetch=FetchType.EAGER)
	private CEntitiy entity;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	private Set<Practice> practises = new HashSet<>(0);
	
	
	public static User copy(User sender){
		
		User copyUser = new User();
		copyUser.setEmail(sender.email);
		copyUser.setName(sender.name);
		copyUser.setCanCreateUser(sender.canCreateUser);
		copyUser.setEntity(sender.entity);
		copyUser.setPractises(sender.practises);
		return copyUser;
	}
}
