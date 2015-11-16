package com.Beendo.Entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
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
@Table(name = "PROVIDER")
public class Provider {

	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	private String npiNum;
	
	
	@OneToOne()
	private CEntitiy centity = new CEntitiy();
	
	@OneToMany( mappedBy = "provider")
	private List<Payer> payerList = new ArrayList<Payer>();
	
	@OneToMany( mappedBy = "practise")
	private List<Practice> practiceList = new ArrayList<Practice>();
	
}
