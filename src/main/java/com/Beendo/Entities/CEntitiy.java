package com.Beendo.Entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="entity", fetch = FetchType.EAGER)
	private List<Practice> practiceList = new ArrayList<Practice>();
	
	/*@OneToOne
	private User user;*/
	/*@OneToMany
	private List<Provider> providerList = new ArrayList<Provider>();
*/
}
