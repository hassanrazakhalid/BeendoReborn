package com.Beendo.Entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.Beendo.CustomJSON.JSONUserType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="provider_qualification")
public class ProviderQualification extends BaseEntity {

	@Id
	@GeneratedValue
	private int id;
	private String medicalCollege;
	private String nameOfDegreeEarned;
	private Date graduationDate;
	
	@OneToOne(mappedBy="qualitication")
	private Provider provider;
	
	@Type(type = JSONUserType.JSON_Normal, parameters = {@Parameter(name = "classType", value = "java.lang.String"),@Parameter(name="type", value="List")})
	private List<String> otherQualification;
	
	@Type( type = JSONUserType.JSON_Normal, parameters = {@Parameter(name = "classType", value = "com.Beendo.Entities.Experience")})
	private Experience residencyInfo;
	@Type(type = JSONUserType.JSON_Normal, parameters = {@Parameter(name = "classType", value = "com.Beendo.Entities.Experience")})
	private Experience internshipInfo;
	@Type(type = JSONUserType.JSON_Normal, parameters = {@Parameter(name = "classType", value = "com.Beendo.Entities.Speciality")})
	private Speciality primarySpeciality;
	@Type(type = JSONUserType.JSON_List, parameters = {@Parameter(name = "classType", value = "com.Beendo.Entities.Speciality"),@Parameter(name="type", value="List")})
	private List<Speciality> secondarySpeciality;
}
