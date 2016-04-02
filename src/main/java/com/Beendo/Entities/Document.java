package com.Beendo.Entities;

import java.io.File;

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
	
	@ManyToOne
	private Provider provider;
	
	public String getFullPath(){
		
		return Constants.PROVIDER_FOLDER_PATH + getNameOnDisk();
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
