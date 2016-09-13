package com.Beendo.Entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name="practice_Address")
public class PracticeAddress extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Type(type = JSONUserType.JSON_List, parameters = {@Parameter(name = JSONUserType.CLASS_TYPE, value = "com.Beendo.Entities.PracticeAddressInfo")})
	private List<PracticeAddressInfo> serviceAddress = new ArrayList<>();
	@Type(type = JSONUserType.JSON_List, parameters = {@Parameter(name = JSONUserType.CLASS_TYPE, value = "com.Beendo.Entities.PracticeAddressInfo")})
	private List<PracticeAddressInfo> billingAddress = new ArrayList<>();
	
	@OneToOne(mappedBy="practiceAddress")
	private Practice practice;

	public List<PracticeAddressInfo> getServiceAddress() {
		
		if (serviceAddress == null)
			serviceAddress = new ArrayList<>();
		
		return serviceAddress;
	}

	public List<PracticeAddressInfo> getBillingAddress() {
		
		if (billingAddress == null)
			billingAddress = new ArrayList<>();
		return billingAddress;
	}
	
	
}
