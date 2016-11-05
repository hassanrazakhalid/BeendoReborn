package com.Beendo.Entities;

import java.util.HashSet;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "practice")
public class Practice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;	
	
	private String groupPtan;
	private String tin;
	private String groupNpi;
	
	@OneToOne
	@Cascade( {org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE })
	private PracticeAddress practiceAddress = new PracticeAddress();
	
	@ManyToOne
//	@JoinColumn(name="ENTITY_ID") just to change the column name
	private CEntitiy entity;
	
//	, cascade={CascadeType.REMOVE,CascadeType.REFRESH}
//	,
	@ManyToMany(fetch = FetchType.EAGER,cascade={CascadeType.REMOVE})
	@JoinTable(name="practice_provider",
	joinColumns=@JoinColumn(name="practice_id"),
	inverseJoinColumns=@JoinColumn(name="provider_id")
	) // this is required otherwise on bothside so no one can be owner and hibernate creates one JOIN table
//	@Cascade(org.hibernate.annotations.CascadeType.DELETE)
	private Set<Provider> providers = new HashSet<Provider>();

	
	@OneToMany(mappedBy="practice", cascade={CascadeType.REMOVE})
	private Set<Transaction> transactions = new HashSet<>();
	
	
	
	public void removeAllProviderDocumentsOnDisk(){
		
		Set<Provider> list = getProviders();
		for (Provider provider : list) {
			
			provider.removeAllDocumentOnDisk();
			
		}
	}



	public PracticeAddress getPracticeAddress() {
		
		if (practiceAddress == null)
			practiceAddress = new PracticeAddress();
		
		return practiceAddress;
	}
}
