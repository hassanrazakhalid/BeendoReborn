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
import javax.persistence.Table;

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
//	@JoinColumn(name="ENTITY_ID") just to change the column name
	private CEntitiy entity;
	
//	, cascade={CascadeType.REMOVE,CascadeType.REFRESH}
//	,
	@ManyToMany(fetch = FetchType.EAGER,cascade={CascadeType.REMOVE})
	@JoinTable(name="PRACTISE_PROVIDER",
	joinColumns=@JoinColumn(name="PRACTISE_ID"),
	inverseJoinColumns=@JoinColumn(name="PROVIDER_ID")
	) // this is required otherwise on bothside so no one can be owner and hibernate creates one JOIN table
//	@Cascade(org.hibernate.annotations.CascadeType.DELETE)
	private Set<Provider> providers = new HashSet<Provider>();

	
	@OneToMany(mappedBy="practice", cascade={CascadeType.REMOVE})
	private Set<ProviderTransaction> transactions = new HashSet<>();
	
	public void removeAllProviderDocumentsOnDisk(){
		
		Set<Provider> list = getProviders();
		for (Provider provider : list) {
			
			provider.removeAllDocumentOnDisk();
			
		}
	}
}
