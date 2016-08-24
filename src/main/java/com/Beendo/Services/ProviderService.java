package com.Beendo.Services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Beendo.Controllers.ProviderCallback;
import com.Beendo.Dao.IDocument;
import com.Beendo.Dao.IProvider;
import com.Beendo.Dao.IUserDao;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Document;
import com.Beendo.Entities.Email;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.ProviderTransaction;
import com.Beendo.Entities.User;
import com.Beendo.Utils.Screen;
import com.Beendo.Utils.SharedData;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.Beendo.Configuration.HibernateAwareObjectMapper;

@Service
public class ProviderService extends GenericServiceImpl<Provider, Integer> implements IProviderService {

	@Autowired
	private IProvider service;
	@Autowired
	private IUserService userService;
	@Autowired
	private IPayerService payerService;
	@Autowired
	private IEntityService entityService;
	@Autowired
	private ITransactionService transactionService;
	@Autowired
	private IDocument documentDao;
	@Autowired
	private IPractise practiceDao;
	@Autowired
	private IUserDao userDao;
	
	@Override
	@Transactional(readOnly=true, propagation=Propagation.REQUIRED)
	public List<Provider> fetchAllByRole(){
		
		String userRole = SharedData.getSharedInstace().getCurrentUser().getRoleName();
		List<Provider> dataList = new ArrayList<>();
		
		if(SharedData.getSharedInstace().shouldReturnFullList())
		{
			dataList.addAll(getAll());
		}
		else
		{
			CEntitiy entity = SharedData.getSharedInstace().getCurrentUser().getEntity();
			
			List<Provider> tmpList = new ArrayList<Provider>();
			List<Practice> practiseList = new ArrayList<Practice>();
			
			tmpList.addAll(findProvidersByEntity(SharedData.getSharedInstace().getCurrentUser().getEntity().getId()));
			dataList = tmpList;
		}
//		Provider p = dataList.get(0);
//		ArrayList<Email> o = (ArrayList<Email>) p.getEmails();
//		
//		Email tmpMail = new Email();
//		tmpMail.setEmail("pk2333@hotmail.com");
//		
////		p.setEmails(tmpMail);
////		p.getEmails().setEmail("pk@hotmail.com");
//		o.get(0).setEmail("pk112233@hotmail.com");
//		service.saveOrUpdate(p);
//		ObjectMapper mapper = new ObjectMapper();
		
//		mapper.registerModule(new Hibernate4Module());
		
		
//		mapper.registerModule(new HibernateAwareObjectMapper());
//		String jsonString = o.toString();//"[{\"email\": \"abc\"}, {\"email\": \"abc2\"}]";
//		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
//		try {
//			List<Email> myObjects = mapper.readValue(jsonString, new TypeReference<List<Email>>(){});
//			int i = 0;
////			Email[] pojos = objectMapper.readValue(o.toString(), Email[].class);
//		} catch (JsonParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		Map<String, String> oo= (Map<String, String>) o.get(0);
//		for (Email email : o) {
//			System.out.println(email.getEmail());
//		}
		
		return dataList;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void getAllProviderInfo(Integer id, EditProviderCallBack callBack){
		
		callBack.fetchProviderInfo(service.getProviderDetails(id));
	}
	
	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	public void refreshAllData(ProviderCallback callBack)
	{
		
		// User user = SharedData.getSharedInstace().getCurrentUser();
		User tmpUser = userService.findById(SharedData.getSharedInstace().getCurrentUser().getId(), false);

		List<Provider>providerList = this.fetchAllByRole(); // providerService.fetchAllByUser();
		List<CEntitiy> entityList = entityService.fetchAllByRole(Screen.Screen_Provider);
//		List<Payer> payerList = payerService.getAll();
		// new
		// ArrayList(SharedData.getSharedInstace().getCurrentUser().getEntity().getPracticeList());

		List<ProviderTransaction> transactions = transactionService.fetchAllByRole();
		
		callBack.getProviderData(tmpUser,providerList);
	}
	
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	public void addDocumentToProvider(Provider provider, Document document)
	{
		if(document.getId() == null)
		{
			document.setProvider(provider);
			documentDao.saveOrUpdate(document);
			provider.getDocuments().add(document);
		}
		
		service.saveOrUpdate(provider);
	}
	
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	public void removeDocumentFromProvider(Provider provider, Document document)
	{
		if(document.getId() != null)
		{
			try {
				
				provider.getDocuments().remove(document);
				service.saveOrUpdate(provider);
				documentDao.remove(document);
				document.removeFileOnDisk();
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	@Override
	public void remove(Provider provider) {
		
		try {
		
			Set<Practice> practiceList = provider.getPracticeList();
			for (Practice practise : practiceList) {
				practise.getProviders().remove(provider);
			}
			practiceDao.updatePractiseList(practiceList);
//			provider.removeAllDocumentOnDisk();
			provider.deleteDocumentFolder();
			super.remove(provider);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	public void addProviderToPractise(Provider provider, Set<Practice>listPracitce){
		
		practiceDao.updatePractiseList(listPracitce);
		this.saveOrUpdate(provider);
		provider.makeFolderIfNotExist();
	}
		
	public String isNPIExist(String npi){
		
		return service.isNPIExist(npi);
	}
	
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	@Override
	public void update(Provider entity) {
		
		super.update(entity);
		for (Document doc : entity.getDocuments()) {
			
			documentDao.update(doc);
		}
	}
	
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	public void updateProviderList(Set<Provider>list){
		
		service.updateProviderList(list);
	}
	
	public List<Provider> findProvidersByEntity(Integer id){
		
		return service.findProvidersByEntity(id);
	}
	
	/**
	 * Document Fetcing code for sending emails
	 */
	@Override
	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	public void getDocumentByEmail(EmailSendingCallback callBack){
		
		List<Document> docList = documentDao.getDocumentByEmail();
		
		List<User> admins = null;
		if(!docList.isEmpty())
			admins = userDao.getAllAdmins();
		
		callBack.getEmailsData(docList, admins);
	}
	
	@Override
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	public void updateDocuments(List<Document>docList){
		
		for (Document document : docList) {
			
			documentDao.update(document);
		}
//		return documentDao.getDocumentByEmail();
	}
}
