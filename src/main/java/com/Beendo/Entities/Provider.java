package com.Beendo.Entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.Beendo.Dto.DocumentCell;
import com.Beendo.Utils.ProviderFile;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "PROVIDER")
public class Provider {

	@Id
	@GeneratedValue
	private Integer id;
	private String firstName;
	private String lastName;
	private String npiNum;
 	
	@OneToMany(mappedBy="provider",cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private Set<Document> documents = new HashSet<>();
	
	@ManyToOne
	private CEntitiy centity;
	
	/*@ManyToMany(fetch = FetchType.EAGER)
	private Collection<Payer> payerList = new HashSet<Payer>();*/
	
//	, cascade={CascadeType.REMOVE}
//	,mappedBy="providers"
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="PRACTISE_PROVIDER",
	inverseJoinColumns=@JoinColumn(name="PRACTISE_ID"),
	joinColumns=@JoinColumn(name="PROVIDER_ID"))
//	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private Set<Practice> practiceList = new HashSet<Practice>();
	
	@OneToMany(mappedBy="provider", cascade={CascadeType.REMOVE})
	private Set<ProviderTransaction> transactions = new HashSet<>();
	
	public String getNameWithNPI(){
		
		return firstName+" " + lastName+ " " + npiNum;
	}
	
	public String getFullName(){
		
		return firstName+" " + lastName;
	}
	
	public Document getFilenameByType(String fileType){
	
//	for (Document doc : this.getDocuments()) {
//			
//			System.out.println(doc.getType());
//		}
	Optional<Document>res = this.getDocuments().stream()
				.filter(doc -> doc.getType().equalsIgnoreCase(fileType))
				.findFirst();
	if(res.isPresent())
		return res.get();
	else
		return null;
	
	}
	
	public List<DocumentCell> getDocumentCellList(){
		
		List<DocumentCell> cells = new ArrayList<>();
		
		for (ProviderFile file : ProviderFile.values()) {

			DocumentCell cell = new DocumentCell();
			Document doc = getFilenameByType(file.getFileType());
			
			if(doc == null)	
			{
				doc = new Document();
				doc.setOrignalName("");
				doc.setType(file.getFileType());
			}
			
			cell.setDocument(doc);
			
			cell.setLbName(file.toString());
			cells.add(cell);
		}
		
		return cells;
	}
	
	public void removeAllDocumentOnDisk(){
		
	for (Document doc : getDocuments()) {
		
		doc.removeFileOnDisk();
	}
}
}