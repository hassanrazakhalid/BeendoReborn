package com.Beendo.Entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "PROVIDER_TRANSACTION")
public class ProviderTransaction {

	@Id
	@GeneratedValue
	private Integer id;
	private String transactionType;
	private String transactionDate;
	private String comments;
	
	
	@OneToOne(fetch = FetchType.EAGER)
	private Payer payer = new Payer();
	
	@OneToOne(fetch = FetchType.EAGER)
	private Practice practice = new Practice();
	
}
