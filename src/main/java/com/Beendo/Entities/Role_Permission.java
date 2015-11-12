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
@Table(name = "Role_Permission")
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
	
}
