package com.Beendo.Entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "payer")
public class Payer {

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String companyName;
//	private String planName;
	private String phoneNumber;
	private String street;
	private String city;
	private String state;
	private String zip;
	
	private String par;
	
	@OneToMany(mappedBy="payer", fetch=FetchType.EAGER)
	@Cascade( {org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE })
	private List<Plan> plans = new ArrayList<>();
	
	@OneToMany(mappedBy="payer")
	@Cascade( {org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE })
	private List<Transaction> transactions = new ArrayList<>();
//	@ManyToOne
//	private Provider provider;
	
	
}
