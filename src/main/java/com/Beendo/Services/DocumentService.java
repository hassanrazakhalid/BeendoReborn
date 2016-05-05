package com.Beendo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Beendo.Dao.IDocument;
import com.Beendo.Entities.Document;

import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
public class DocumentService extends GenericServiceImpl<Document, Integer> implements IDocumentService {

	@Autowired
	private IDocument docDao;
	
	@Override
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	public int markDocumentRead(Integer id){
		
		return docDao.markDocumentRead(id);
	}
	
}
