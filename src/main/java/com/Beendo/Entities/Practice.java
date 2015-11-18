package com.Beendo.Entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "PRACTISE")
public class Practice {

	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	
	@ManyToOne
	private CEntitiy entity;
	
	@ManyToOne
	private Provider provider;

	@OneToMany(cascade = CascadeType.ALL)
	private Set<User> users = new HashSet<>(0);
}
