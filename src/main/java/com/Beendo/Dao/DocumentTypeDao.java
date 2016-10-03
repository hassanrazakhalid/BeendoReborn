package com.Beendo.Dao;

import org.springframework.stereotype.Repository;

import com.Beendo.Entities.DocumentType;

@Repository
public class DocumentTypeDao extends GenericDao<DocumentType, Integer> implements IDocumentType {

}
