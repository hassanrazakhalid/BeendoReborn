package com.Beendo.Entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Provider")
public class Provider {

	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	private String npiNum;
	
}
