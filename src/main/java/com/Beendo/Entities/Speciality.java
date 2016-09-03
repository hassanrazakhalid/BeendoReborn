package com.Beendo.Entities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.Beendo.CustomJSON.JSONUserType;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@JsonSerialize(using=SpecialitySerializer.class)
public class Speciality {

	private SpecialityInfo specialityInfo = new SpecialityInfo();
	private BoardInfo boardInfo = new BoardInfo();
	
	public Speciality() {
		// TODO Auto-generated constructor stub
	}
//	HashMap<String, HashMap<String, String>>
	@JsonCreator
	public Speciality(Object info) {
		
		if (info instanceof Map) {
			
			Map<String,Map<String,String>> tmpMap = (Map<String,Map<String,String>> )info;
			Map<String,String> obj1 = tmpMap.get("boardInfo");
			Map<String,String> obj2 = tmpMap.get("specinityInfo");
			this.specialityInfo = deserializeSpeciality(obj1);
			this.boardInfo = deserializeBoardInfo(obj2);
		}
		if (info instanceof ArrayList) {
			
			ArrayList<HashMap<String, HashMap<String, String>>> list = (ArrayList<HashMap<String, HashMap<String, String>>>) info;
			
			for (HashMap<String, HashMap<String, String>> hashMap : list) {
				
				Map<String,String> obj1 = hashMap.get("specinityInfo");
				Map<String,String> obj2 = hashMap.get("boardInfo");
				
				this.specialityInfo = deserializeSpeciality(obj1);
				this.boardInfo = deserializeBoardInfo(obj2);

				System.out.println("");
			}	
		}
	}
	
	private SpecialityInfo deserializeSpeciality(Map<String,String> obj){
		
		ObjectMapper mapper = JSONUserType.MAPPER;
		SpecialityInfo specialityInfo = null;
		try {
			String src = mapper.writeValueAsString(obj);
			specialityInfo = mapper.readValue(src, SpecialityInfo.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return specialityInfo;
	}
	
	private BoardInfo deserializeBoardInfo(Map<String,String> obj){
		
		ObjectMapper mapper = JSONUserType.MAPPER;
		BoardInfo boardInfo = null;
		try {
			String src = mapper.writeValueAsString(obj);
			boardInfo = mapper.readValue(src, BoardInfo.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return boardInfo;
	}
	public SpecialityInfo getSpecialityInfo() {
		if (specialityInfo == null){
			specialityInfo = new SpecialityInfo();
		}
		return specialityInfo;
	}
	public BoardInfo getBoardInfo() {
		if(boardInfo == null){
			boardInfo = new BoardInfo();
		}
		return boardInfo;
	}
	
	
}
