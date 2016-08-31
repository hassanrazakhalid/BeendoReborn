package com.Beendo.Entities;

import java.util.ArrayList;
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
@Table(name="other_ProviderInfo")
public class OtherProviderInfo extends BaseEntity {

	@Id
	@GeneratedValue
	int id;
	
	@OneToOne(mappedBy="otherInfo")
	private Provider provider;
	
	private int oldestPatientAgeLimit;
	private boolean panelStatus;
	private String remarks;
	
	@Type(type=JSONUserType.JSON_Normal, parameters = {@Parameter(name=JSONUserType.CLASS_TYPE, value="com.Beendo.Entities.Language")})
	private List<Language> languagesList = new ArrayList<>();
}
