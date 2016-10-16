package com.Beendo.Entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.mysql.fabric.xmlrpc.base.Array;

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
	
	@ManyToOne
	private Payer payer;
	@OneToMany(mappedBy="plan")
	private List<Transaction> transaction = new ArrayList<>();
}
