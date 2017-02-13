package com.Beendo.Entities;

import java.io.File;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.Beendo.Utils.Constants;
import com.Beendo.Utils.SharedData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="document")
public class Document {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String orignalName;
	private String nameOnDisk;
//	private String type;
	private Date  expireDate;
	private Date  effectiveDate;
	private Integer reminderDays;
	private Integer reminderStatus;
	
	@ManyToOne
	private Provider provider;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private DocumentType docType = new DocumentType();
	
	public String getFullPath(){
		
		String path = provider.getFolderPath()+"/" + getNameOnDisk();
		return path;
//		return provider.getFolderPath()+"\\" + getNameOnDisk();
//		return SharedData.getSharedInstace().getDocumentRootPath() + getNameOnDisk();
//		return Constants.PROVIDER_FOLDER_PATH + getNameOnDisk();
	}
	
	public void updateReminderCount(int active){
		
		int i =0;
		
		this.setReminderStatus(active);
		
//		if(this.reminderDate.after(new Date()))
//		{
//			this.setReminderStatus(0);
//		}
//		else
//		{
//			this.setReminderStatus(1);
//		}
	}
	
	public boolean getReminderBooleanValue(){
	
		if(getReminderStatus() == null)
			return false;
		
		 return getReminderStatus() <= 0 ? true : false;
	}
	
	public void removeFileOnDisk(){
		
		File file = new File(getFullPath());

		if (file.delete()) {
			System.out.println(file.getName() + " is deleted!");
		} else {
			System.out.println("Delete operation is failed.");
		}
	}
	
	public boolean shouldRenderDocumentLink(){
		
		if(this.id == null)
			return false;
		else
			return true;
	}
}
