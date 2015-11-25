package com.Beendo.Entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
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
	
	
	@OneToOne()
	private CEntitiy centity = new CEntitiy();
	
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Payer> payerList = new ArrayList<Payer>();
	
	@ManyToMany(fetch = FetchType.EAGER,mappedBy="providers")
	private Set<Practice> practiceList = new HashSet<Practice>();
	
}