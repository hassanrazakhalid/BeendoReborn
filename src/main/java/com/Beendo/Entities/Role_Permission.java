package com.Beendo.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "ROLE_PERMISSION")
public class Role_Permission {

	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	private boolean canProviderAdd;
	private boolean canProviderEdit;
	private boolean canPayerAdd;
	private boolean canPayerEdit;
	private boolean canPayerTransactionAdd;
	private boolean canPayerTransactionEdit;
	
	@OneToOne(mappedBy = "role")
	private User user;

	public static Role_Permission copy(Role_Permission sender){
		
		Role_Permission rol = new Role_Permission();
		rol.setCanPayerAdd(sender.isCanPayerAdd());
		rol.setCanPayerEdit(sender.isCanPayerEdit());
		rol.setCanProviderAdd(sender.isCanProviderAdd());
		rol.setCanProviderEdit(sender.isCanProviderEdit());
		rol.setCanPayerTransactionAdd(sender.isCanPayerTransactionAdd());
		rol.setCanPayerTransactionEdit(sender.isCanPayerTransactionEdit());
		rol.setName(sender.getName());
		
		return rol;
		
	}
}
