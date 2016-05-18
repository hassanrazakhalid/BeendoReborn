package com.Beendo.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Beendo.Controllers.ProviderCallback;
import com.Beendo.Dao.GenericDao;
import com.Beendo.Dao.ICRUD;
import com.Beendo.Dao.IProvider;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Document;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.User;
import com.Beendo.Utils.Role;
import com.Beendo.Utils.SharedData;

public interface IProviderService extends GenericService<Provider, Integer> {

	public List<Provider> fetchAllByRole();
	
	public String isNameExist(String name, String lname, String npi);
	
	public void updateProviderList(Set<Provider>list);
	
	public List<Provider> findProvidersByEntity(Integer id);
	public void refreshAllData(ProviderCallback callBack);
	public void addDocumentToProvider(Provider provider, Document document);
	public void removeDocumentFromProvider(Provider provider, Document document);
	public void addProviderToPractise(Provider provider, Set<Practice>listPracitce);
	public void getAllProviderInfo(Integer id, EditProviderCallBack callBack);
	public void getDocumentByEmail(EmailSendingCallback callBack);
	public void updateDocuments(List<Document>docList);

}
