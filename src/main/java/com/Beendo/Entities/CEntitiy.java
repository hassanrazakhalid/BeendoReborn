package com.Beendo.Entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.Beendo.Utils.ICache;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "Entities")
public class CEntitiy implements ICache<CEntitiy> {

	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="entity", fetch = FetchType.EAGER)
	private Set<Practice> practiceList = new HashSet<Practice>();
	
	/*@ManyToMany
	private List<Provider> providerList = new ArrayList<Provider>();*/

	/*@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.valueOf(getId());
	}*/

	@Override
	public Integer getCacheId(){
		
		return getId();
	}
	
	@Override
	public CEntitiy getObject(Integer id){
		
		return null;
	}

}
