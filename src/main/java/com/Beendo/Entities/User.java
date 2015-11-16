package com.Beendo.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
	private Integer id;
	private String name;
	private String email;
	private String password;
	private Boolean canCreateUser;
	
	@OneToOne
	private Role_Permission roles;
	
}
