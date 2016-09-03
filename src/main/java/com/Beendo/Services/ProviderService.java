package com.Beendo.Services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Beendo.Controllers.ProviderCallback;
import com.Beendo.Dao.IDocument;
import com.Beendo.Dao.IProvider;
import com.Beendo.Dao.IUserDao;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.DegreeInfo;
import com.Beendo.Entities.Document;
import com.Beendo.Entities.Email;
import com.Beendo.Entities.Language;
import com.Beendo.Entities.PhoneNumber;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.ProviderTransaction;
import com.Beendo.Entities.Slot;
import com.Beendo.Entities.User;
import com.Beendo.Utils.Screen;
import com.Beendo.Utils.SharedData;

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
/*		Provider p = dataList.get(0);
		ArrayList<Email> o = (ArrayList<Email>) p.getEmails();
		
		Email tmpMail = new Email();
		tmpMail.setEmail("pk2333@hotmail.com");*/
//		
////		p.setEmails(tmpMail);
////		p.getEmails().setEmail("pk@hotmail.com");
//		o.get(0).setEmail("pk112233@hotmail.com");
//		service.saveOrUpdate(p);
		
//		String json = "{\"Mon\" : { \"startTime\" : \"23:50:15\", \"endTime\" : \"23:70:15\" } }";
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.registerModule(new Hibernate4Module());
//		
////		mapper.registerModule(new HibernateAwareObjectMapper());
////		String jsonString = o.toString();//"[{\"email\": \"abc\"}, {\"email\": \"abc2\"}]";
//		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
//		try {
//			
////			TypeReference<HashMap<String,Slot>> typeRef 
////            = new TypeReference<HashMap<String,Slot>>() {};
//            MapLikeType mapType =    mapper.getTypeFactory().constructMapLikeType(Map.class, String.class, Slot.class);
//            Map<String, Object> exchangeRates = mapper.readValue(json, mapType);
//            
//            String s = mapper.writeValueAsString(exchangeRates);
////		Object li =	mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(Map.class, Slot.class));
////			Object myObjects = mapper.readValue(json, typeRef);
//			System.out.println("");
////			Email[] pojos = objectMapper.readValue(o.toString(), Email[].class);
//		}
////		catch (JsonMappingException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		catch (Exception e){
//			e.printStackTrace();
//		}
		
//		Map<String, String> oo= (Map<String, String>) o.get(0);
//		for (Email email : o) {
//			System.out.println(email.getEmail());
//		}
		
		return dataList;
	}
	
	@Transactional(readOnly=true)
	public void getProviderDetailsNoFiles(Integer id, Consumer<Provider> sender) {
		
		Provider provider = service.getProviderDetailsNoFiles(id);
		sender.accept(provider);
	}
	
	@Transactional(readOnly=false, propagation=Propagation.REQUIRES_NEW)
	public void enterDummy(){
	
		Provider provider = new Provider();
		provider.setCAQHId("1233444");
		provider.setCAQHPassword("password");
		
		provider.setFirstName("HRK");
		provider.setLastName("Khalid");
		provider.setNpiNum("1233344");
		
		Email email = new Email();
		email.setEmail("abc@hotmail.com");
		Email email2 = new Email();
		email2.setEmail("abc2@hotmail.com");
		provider.getEmails().add(email);
		provider.getEmails().add(email2);
		
		PhoneNumber n1 = new PhoneNumber();
		n1.setNumber("1234");
		PhoneNumber n2 = new PhoneNumber();
		n2.setNumber("1234567");
		provider.getPhoneNumbers().add(n1);
		provider.getPhoneNumbers().add(n2);
		
		provider.getOtherInfo().setOldestPatientAgeLimit(12345);

		Language l1 = new Language();
		l1.setLanguageName("urdu");
		Language l2 = new Language();
		l2.setLanguageName("english");
		provider.getOtherInfo().getLanguagesList().add(l1);
		provider.getOtherInfo().getLanguagesList().add(l2);

		DegreeInfo d1 = new DegreeInfo();
		d1.setCollegeName("Abc");
		d1.setDegreeName("BSCS");
		d1.setFinishedDate(new Date());
//		provider.getQualitication().setGraduationInfo(d1);
		
		Slot s1 = new Slot();
		s1.setStartTime(new Date());
		s1.setEndTime(new Date());
		provider.getOtherInfo().getSlots().put("Mon", s1);
		
		service.saveOrUpdate(provider);
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
	
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	public void createNewProvider(Provider provider) {
		
		provider.getOtherInfo().setProvider(provider);
		provider.getQualitication().setProvider(provider);
		service.saveOrUpdate(provider);
	}
}
