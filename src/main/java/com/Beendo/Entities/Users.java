package com.Beendo.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Users")
public class Users {

	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	private String email;
	private String password;
	
	@OneToOne
	private Role_Permission roles;
	
}
