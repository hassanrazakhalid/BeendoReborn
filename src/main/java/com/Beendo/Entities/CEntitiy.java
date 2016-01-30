package com.Beendo.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
public class CEntitiy implements ICache<CEntitiy>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="entity", fetch = FetchType.LAZY)
	private Set<Practice> practiceList = new HashSet<Practice>();
	
	//@OneToMany(cascade=CascadeType.ALL, mappedBy="centity", fetch = FetchType.EAGER)
	//private Set<Provider> providerList = new HashSet<Provider>();
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="entity", fetch = FetchType.LAZY)
	private Set<ProviderTransaction> transactionList = new HashSet<ProviderTransaction>();

	@OneToMany(cascade=CascadeType.ALL, mappedBy="entity", fetch = FetchType.LAZY)
	private Set<User> users = new HashSet<User>();
	
	@Override
	public Integer getCacheId(){
		
		return getId();
	}
	
	@Override
	public CEntitiy getObject(Integer id){
		
		return null;
	}

	public void removeUserById(Integer id){
		
		for (User user : users) {
			
			if(user.getId().compareTo(id) == 0)
			{
				users.remove(user);
				return ;
			}
		}
	}
	
}
