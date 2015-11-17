package com.Beendo.Entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
	@GeneratedValue
	@Column(name="User_Id")
	private Integer id;
	private String name;
	private String email;
	private String password;
	private Boolean canCreateUser;
	
	@OneToOne(mappedBy = "user")
	private Role_Permission role;
	
	@OneToOne
	private CEntitiy entity;
	
	@OneToMany(cascade = CascadeType.ALL)
	private Set<Practice> practises = new HashSet<>(0);
	
}
