package com.Beendo.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Beendo.Dao.IDocument;
import com.Beendo.Dao.IDocumentType;
import com.Beendo.Entities.Document;
import com.Beendo.Entities.DocumentType;

import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
public class DocumentService extends GenericServiceImpl<Document, Integer> implements IDocumentService {

	@Autowired
	private IDocument docDao;
	
	@Autowired
	private IDocumentType documentTypeDao;
	
	private List<DocumentType> sharedDocTypeList = new ArrayList<>();
	
	@Override
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	public int markDocumentRead(Integer id){
		
		return docDao.markDocumentRead(id);
	}
	
	@Transactional(readOnly=true)
	public List<DocumentType> getDocumentTypeList(){
		
		return documentTypeDao.findAll();
	}

	public List<DocumentType> getSharedList() {
		
		if (sharedDocTypeList.isEmpty()){	
			sharedDocTypeList = getDocumentTypeList();
		}
		return sharedDocTypeList;
	}
	
	
}
