package com.Beendo.Entities;

import java.util.ArrayList;
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
	private Integer id;
	
	@Type(type = JSONUserType.JSON_Normal, parameters = {@Parameter(name = JSONUserType.CLASS_TYPE, value = "com.Beendo.Entities.DegreeInfo")})
	private DegreeInfo primaryDegree =  new DegreeInfo();
	@Type(type = JSONUserType.JSON_List, parameters = {@Parameter(name = JSONUserType.CLASS_TYPE, value = "com.Beendo.Entities.DegreeInfo")})
	private List<DegreeInfo> otherDegrees = new ArrayList<>();
	
	@OneToOne(mappedBy="qualitication")
	private Provider provider;
	
	@Type( type = JSONUserType.JSON_Normal, parameters = {@Parameter(name = "classType", value = "com.Beendo.Entities.Experience")})
	private Experience residencyInfo = new Experience();
	
	@Type(type = JSONUserType.JSON_Normal, parameters = {@Parameter(name = "classType", value = "com.Beendo.Entities.Experience")})
	private Experience internshipInfo = new Experience();
	
	@Type(type = JSONUserType.JSON_Normal, parameters = {@Parameter(name = "classType", value = "com.Beendo.Entities.Speciality")})
	private Speciality primarySpeciality = new Speciality();
	@Type(type = JSONUserType.JSON_List, parameters = {@Parameter(name = "classType", value = "com.Beendo.Entities.Speciality")})
	private List<Speciality> otherSpecialities = new ArrayList<>();
	
	
	public DegreeInfo getPrimaryDegree() {
		if (primaryDegree == null){
			primaryDegree = new DegreeInfo();
		}
		
		return primaryDegree;
	}
	public List<DegreeInfo> getOtherDegrees() {
		if (otherDegrees == null){
			otherDegrees = new ArrayList<>();
		}
		return otherDegrees;
	}
	public Experience getResidencyInfo() {
		if (residencyInfo == null) {
			residencyInfo = new Experience();
		}
		
		return residencyInfo;
	}
	public Experience getInternshipInfo() {
		
		if (internshipInfo == null) {
			internshipInfo = new Experience();
		}
		return internshipInfo;
	}
	public Speciality getPrimarySpeciality() {
		
		if (primarySpeciality == null){
			primarySpeciality = new Speciality();
		}
		return primarySpeciality;
	}
	public List<Speciality> getOtherSpecialities() {
		
		if (otherSpecialities == null){
			otherSpecialities = new ArrayList<>();
		}
		return otherSpecialities;
	}
	
	
	
}
