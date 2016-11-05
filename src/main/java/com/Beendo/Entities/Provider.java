package com.Beendo.Entities;

import java.io.File;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.FileSystemUtils;

import com.Beendo.CustomJSON.JSONUserType;
import com.Beendo.Dto.DocumentCell;
import com.Beendo.Utils.SharedData;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "provider")
public class Provider extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String firstName;
	private String lastName;
	private String npiNum;
 	
	private String CAQHId;
	private String CAQHPassword;
	
	@Type(type="date")
	private Date dob;
	private String socialSecurityNumber;
	private String degree;
	private String medicareId;
	
	@OneToOne(fetch=FetchType.LAZY)
	@Cascade( {org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE })
	private ProviderQualification qualitication = new ProviderQualification();
	
	
	@OneToOne(fetch=FetchType.LAZY)
	@Cascade( {org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE })
	private OtherProviderInfo otherInfo = new OtherProviderInfo();
	
	@Type(type = JSONUserType.JSON_List, parameters = {@Parameter(name = JSONUserType.CLASS_TYPE, value = "com.Beendo.Entities.Email")})
	private ArrayList<Email> emails = new ArrayList<>();
	@Type(type = JSONUserType.JSON_List, parameters = {@Parameter(name = JSONUserType.CLASS_TYPE, value = "com.Beendo.Entities.PhoneNumber")})
	private List<PhoneNumber> phoneNumbers = new ArrayList<>();
	@Type(type = JSONUserType.JSON_List, parameters = {@Parameter(name = JSONUserType.CLASS_TYPE, value = "com.Beendo.Entities.FaxNumber")})
	private List<FaxNumber> faxNumbers = new ArrayList<>();
	
	@OneToMany(mappedBy="provider",cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private Set<Document> documents = new HashSet<>();
	
	@ManyToOne
	private CEntitiy centity;
	
	public ProviderQualification getQualitication(){
		
		if (qualitication == null){	
			qualitication = new ProviderQualification();
		}
		return qualitication;
	}
	
		public OtherProviderInfo getOtherInfo(){
		
		if (otherInfo == null){	
			otherInfo = new OtherProviderInfo();
		}
		return otherInfo;
	}
	
	/*@ManyToMany(fetch = FetchType.EAGER)
	private Collection<Payer> payerList = new HashSet<Payer>();*/
	
//	, cascade={CascadeType.REMOVE}
//	,mappedBy="providers"
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="practice_provider",
	joinColumns=@JoinColumn(name="provider_id"),
	inverseJoinColumns=@JoinColumn(name="practice_id"))
//	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private Set<Practice> practiceList = new HashSet<Practice>();
	
	@OneToMany(mappedBy="provider", cascade={CascadeType.REMOVE})
	private Set<Transaction> transactions = new HashSet<>();
	
/*	@Transient
	public String fullName;*/
	
	public String getNameWithNPI(){
		
		return firstName+" " + lastName+ " " + npiNum;
	}
	
	public String getFullName() {
		return firstName+" " + lastName;
	}
//	public String getFullName(){
//		
//		return firstName+" " + lastName;
//	}
	
	public Document getFilenameByType(Integer fileId){
				
	Optional<Document>res = this.getDocuments().stream()
				.filter(doc -> doc.getDocType().getId() == fileId)
				.findFirst();
	if(res.isPresent())
		return res.get();
	else
		return null;
	}
	
//	public String getFolderForDocument(Document doc){
//		
//		String path = SharedData.getSharedInstace().getDocumentRootPath()+getFullName();
//		File dir = new File(path);
//        if (!dir.exists()) {
//            if (dir.mkdir()) {
//                System.out.println("Directory is created!");
//            } else {
//                System.out.println("Failed to create directory!");
//            }
//        }
//        
//        return path;
//	}
	

	public void removeAllDocumentOnDisk(){
		
	for (Document doc : getDocuments()) {
		
		doc.removeFileOnDisk();
	}	
	}
	
	public String getFolderPath(){

		return SharedData.getSharedInstace().getDocumentRootPath()+ getId();
	}

	public void makeFolderIfNotExist(){
		
	     File file = new File(getFolderPath());
	        if (!file.exists()) {
	            if (file.mkdir()) {
	            	
	            	System.out.println("Directory is created For:"+ getId());
	            } else {
	            	
	            	System.out.println("Failed to create directory!"+ getId());
	            }
	        }
	}
	
	public void deleteDocumentFolder(){
		
	 	File directory = new File(getFolderPath());
	 	FileSystemUtils.deleteRecursively(directory);
    	//make sure directory exists
  /*  	if(!directory.exists()){
 
           System.out.println("Directory does not exist.");
           System.exit(0);
 
        }else{
 
           try{
        	   
        	   directory.delete();
//               delete(directory);
        	
           }catch(IOException e){
               e.printStackTrace();
               System.exit(0);
           }
        }
 
    	System.out.println("Done");
    }*/
	}

	public ArrayList<Email> getEmails() {
		if (emails == null){
			emails = new ArrayList<>();
		}
		return emails;
	}

	public List<PhoneNumber> getPhoneNumbers() {
		if (phoneNumbers == null){
			phoneNumbers = new ArrayList<>();
		}
		return phoneNumbers;
	}

	public List<FaxNumber> getFaxNumbers() {
		if (faxNumbers == null){
			faxNumbers = new ArrayList<>();
		}
		return faxNumbers;
	}


	
	
}