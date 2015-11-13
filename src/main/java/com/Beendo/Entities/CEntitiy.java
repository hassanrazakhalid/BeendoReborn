package com.Beendo.Entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "Entities")
public class CEntitiy {

	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	
	@OneToMany
	private List<Practice> practiceList = new ArrayList<Practice>();
	
	@OneToMany
	private List<Provider> providerList = new ArrayList<Provider>();
	
}
