package com.Beendo.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Practices")
public class Practices {

	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	
}
