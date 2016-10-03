package com.Beendo.Services;

import java.util.List;

import com.Beendo.Entities.Document;
import com.Beendo.Entities.DocumentType;

public interface IDocumentService extends GenericService<Document, Integer> {

	public int markDocumentRead(Integer id);
	public List<DocumentType> getSharedList();
}
