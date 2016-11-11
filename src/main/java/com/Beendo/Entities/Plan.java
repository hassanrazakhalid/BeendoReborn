package com.Beendo.Entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="plan")
public class Plan {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String phone;
	private String fax;
	private String street;
	private String city;
	private String state;
	private String zip;
	
	@ManyToOne
	private Payer payer;
	@ManyToMany()
	@JoinTable(name="transaction_plan",
	joinColumns=@JoinColumn(name="plan_id"),
	inverseJoinColumns=@JoinColumn(name="transaction_id")
			)
	private List<Transaction> transaction = new ArrayList<>();
}
