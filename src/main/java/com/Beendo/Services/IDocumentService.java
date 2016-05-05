package com.Beendo.Services;

import com.Beendo.Entities.Document;

public interface IDocumentService extends GenericService<Document, Integer> {

	public int markDocumentRead(Integer id);
}
