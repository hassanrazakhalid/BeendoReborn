package com.Beendo.Dao;

import java.util.List;

import com.Beendo.Entities.Document;

public interface IDocument extends ICRUD<Document, Integer> {

	public List<Document> getDocumentByEmail();
}
