package com.Beendo.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "PAYER")
public class Payer {

	
	@Id
	@GeneratedValue
	private Integer id;
	private String companyName;
	private String planName;
	private String phoneNumber;
	private String street;
	private String city;
	private String state;
	private String zip;
	
}
