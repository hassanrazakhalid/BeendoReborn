package com.Beendo.Entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "PRACTISE")
public class Practice {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private String name;	
	
	@ManyToOne
	private CEntitiy entity;
	
//	, cascade={CascadeType.REMOVE,CascadeType.REFRESH}
	@ManyToMany(fetch = FetchType.EAGER,cascade={CascadeType.REMOVE})
	@JoinTable(name="PRACTISE_PROVIDER",
	joinColumns=@JoinColumn(name="PRACTISE_ID"),
	inverseJoinColumns=@JoinColumn(name="PROVIDER_ID")
	)
//	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private Set<Provider> providers = new HashSet<Provider>();

}
