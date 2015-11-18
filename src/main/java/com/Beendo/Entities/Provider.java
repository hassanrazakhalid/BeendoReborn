package com.Beendo.Entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "PROVIDER")
public class Provider {

	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	private String npiNum;
	
	
	@OneToOne(cascade=CascadeType.ALL)
	private CEntitiy centity = new CEntitiy();
	
	//@JoinColumn(name = "id")
	//@OneToMany(cascade=CascadeType.ALL, mappedBy = "PROVIDER")
	//@OneToMany(fetch = FetchType.EAGER, mappedBy = "provider") 
	@OneToMany(cascade=CascadeType.ALL)
	private List<Payer> payerList = new ArrayList<Payer>();
	
	//@OneToMany(cascade=CascadeType.ALL, mappedBy = "PROVIDER")
	//@OneToMany(fetch = FetchType.EAGER, mappedBy = "provider") 
	@OneToMany(cascade=CascadeType.ALL)
	private List<Practice> practiceList = new ArrayList<Practice>();
	
}
