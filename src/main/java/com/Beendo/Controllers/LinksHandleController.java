package com.Beendo.Controllers;

import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Beendo.Services.IDocumentService;
import com.github.javaplugs.jsf.SpringScopeRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Controller
@SpringScopeRequest
public class LinksHandleController {

	@Autowired
	private IDocumentService docService;
	
	private int result;
	
	public void onLoad(){
		
		docReadLogic();
	}
	
	private void docReadLogic(){

		Map<String,String> parms = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		
		Integer docID = Integer.valueOf(parms.get("id"));
		
		result = docService.markDocumentRead(docID);

	}
	
	public String view(){
		
		docReadLogic();
			return "DocReminder";
	}
	
	public String getDocStatus(){
		
		if(result > 0) //  means success
		{
			return "Reminder Sucessfully stopped";
		}
		else
		{
			return "No document found";
		}
		
	}
}
