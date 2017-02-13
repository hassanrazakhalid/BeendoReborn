
package com.Beendo.Dto;

import com.Beendo.Entities.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentCell {

	private Document document;
	private String lbName;

	private boolean alarmEnabled; 
	
	public String getColorClassName(){
		
		if(this.document.getOrignalName().length() > 0)
			return "green";
		else
			return "red";
	}
	
	 public void updateReminderStatus(){
		 
		 if(alarmEnabled)
		 {
			 Document doc = this.getDocument();
			 
			 doc.updateReminderCount(alarmEnabled == true ? 0 : 1);
//			 System.out.println("");			 
		 }
	 }
	 
	 public boolean isLinkDisabled(){
		 if (document.getNameOnDisk() == null) {
			 return true;
		 }
		 return document.getNameOnDisk().isEmpty();
	 }
	 
	 public String getDocViewLink(){
		 
		 if (isLinkDisabled()){
			 return "";
		 }
		 String fullLink = "https://docs.google.com/viewer?url=" + "http://documentsDev.sypore.net:8081/" + "providers/" +document.getProvider().getId() +"/" + document.getNameOnDisk();
		 return fullLink;
	 }
}
