package com.Beendo.Entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "Entities")
public class Entities {

	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	
	@OneToMany
	private List<Practices> practiceList = new ArrayList<Practices>();
	
	@OneToMany
	private List<Provider> providerList = new ArrayList<Provider>();
	
}
