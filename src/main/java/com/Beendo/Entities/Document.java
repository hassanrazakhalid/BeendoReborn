package com.Beendo.Entities;

import java.io.File;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.Beendo.Utils.Constants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="document")
public class Document {

	@Id
	@GeneratedValue
	private Integer id;
	
	private String orignalName;
	private String nameOnDisk;
	private String type;
	private Date  expireDate;
	private Date  effectiveDate;
	private Date reminderDate;
	private Integer reminderStatus;
	
	@ManyToOne
	private Provider provider;
	
	public String getFullPath(){
		
		return Constants.PROVIDER_FOLDER_PATH + getNameOnDisk();
	}
	
	public void updateReminderCount(){
		
		if(this.reminderDate.after(new Date()))
		{
			this.setReminderStatus(0);
		}
		else
		{
			this.setReminderStatus(1);
		}
	}
	
	public void removeFileOnDisk(){
		
		File file = new File(getFullPath());

		if (file.delete()) {
			System.out.println(file.getName() + " is deleted!");
		} else {
			System.out.println("Delete operation is failed.");
		}
	}
}
