package com.Beendo.Entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Practice practise;
	
}
