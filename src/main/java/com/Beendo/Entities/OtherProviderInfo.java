package com.Beendo.Entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.Beendo.CustomJSON.JSONUserType;
import com.Beendo.Dto.SlotCell;
import com.Beendo.Utils.Weekdays;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="other_ProviderInfo")
public class OtherProviderInfo extends BaseEntity {

	@Id
	@GeneratedValue
	private Integer id;
	
	@OneToOne(mappedBy="otherInfo")
	private Provider provider;
	
	private Integer oldestPatientAgeLimit;
	private boolean panelStatus;
	private String remarks;
	
	@Type(type=JSONUserType.JSON_List, parameters = {@Parameter(name=JSONUserType.CLASS_TYPE, value="com.Beendo.Entities.Language")})
	private List<Language> languagesList = new ArrayList<>();
	@Type(type=JSONUserType.JSON_Map, parameters = {@Parameter(name=JSONUserType.CLASS_TYPE, value="com.Beendo.Entities.Slot")})
	private Map<String,Slot> slots = new HashMap<>(); 
	
	

	public List<SlotCell> getSlotCells() {
		
		List<SlotCell> list = new ArrayList<>();
		for (Weekdays day : Weekdays.values()) {
			  
			String dayStr = day.toString();
			Slot slot = slots.get(dayStr);
			
			if (slot == null) {
				slot = new Slot();
			}
			
			SlotCell cell = new SlotCell();
			cell.setSlot(slot);
			cell.setDay(dayStr);
			list.add(cell);
		}
		
		return list;
	}



	public List<Language> getLanguagesList() {
		
		if (languagesList == null) {
			languagesList = new ArrayList<>();
		}
		return languagesList;
	}



	public Map<String, Slot> getSlots() {
		
		if (slots == null){
			slots = new HashMap<>();
		}
		return slots;
	}
	
	
}
