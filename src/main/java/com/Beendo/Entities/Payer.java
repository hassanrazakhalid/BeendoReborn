package com.Beendo.Entities;

import java.io.Serializable;
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
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.FetchProfiles;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "payer")

@FetchProfiles(
		{
			@FetchProfile(name="planList",fetchOverrides = {
					@FetchProfile.FetchOverride(entity=Payer.class, association="plans", mode=FetchMode.JOIN)		
			})
			
		}
		)
public class Payer implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -980211077285605975L;
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
	private String fax;
	
//	private String par;
	
	@OneToMany(mappedBy="payer")
	@Cascade( {org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE })
//	private Set<Plan> plans = new HashSet<>();
	private List<Plan> plans = new ArrayList<>();
	
	@OneToMany(mappedBy="payer")
	@Cascade( {org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE })
	private List<Transaction> transactions = new ArrayList<>();
//	@ManyToOne
//	private Provider provider;
	
	public String getDisplayName(){
		return name + " " + state; 
	}
	
	public void addNewPlan(){
		
		Plan plan = new Plan();
		plan.setCity(city);
		plan.setStreet(street);
		plan.setState(state);
		plan.setPhone(phoneNumber);
		plan.setFax(fax);
		plan.setZip(zip);
		
		plan.setPayer(this);
		
		getPlans().add(plan);
	}
}
