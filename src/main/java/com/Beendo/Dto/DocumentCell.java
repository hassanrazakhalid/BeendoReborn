package com.Beendo.Dto;

import java.util.Calendar;

import com.Beendo.Entities.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentCell {

	private Document document;
	private String lbName;

	public String getColorClassName(){
		
		if(this.document.getOrignalName().length() > 0)
			return "green";
		else
			return "red";
	}
}
