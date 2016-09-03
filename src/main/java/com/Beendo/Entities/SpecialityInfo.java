package com.Beendo.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="specialityInfo")
public class SpecialityInfo {

	@Id
	@GeneratedValue
	private Integer id;
	private String name;
}
