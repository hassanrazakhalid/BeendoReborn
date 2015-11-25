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

import com.Beendo.Utils.ICache;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "PRACTISE")
public class Practice implements ICache<Practice> {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private String name;
	
	@ManyToOne
	private CEntitiy entity;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="PRACTISE_PROVIDER",
	joinColumns=@JoinColumn(name="PRACTISE_ID"),
	inverseJoinColumns=@JoinColumn(name="PROVIDER_ID")
	)
	private Set<Provider> providers = new HashSet<Provider>();

	@Override
	public Integer getCacheId(){
		
		return getId();
	}
	
	@Override
	public Practice getObject(Integer id){
		
		return null;
	}

	public boolean isEqual(Practice sender){
	
		if(this.id == sender.id)
			return true;
		else
			return false;
}
}
