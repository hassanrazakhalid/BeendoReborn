package com.Beendo.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
	
}
