package com.Beendo.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Beendo.Controllers.ProviderCallback;
import com.Beendo.Dao.IDocument;
import com.Beendo.Dao.IProvider;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Document;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.ProviderTransaction;
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
		List<Payer> payerList = payerService.getAll();
		// new
		// ArrayList(SharedData.getSharedInstace().getCurrentUser().getEntity().getPracticeList());

		List<ProviderTransaction> transactions = transactionService.fetchAllByRole();
		
		callBack.getProviderData(tmpUser,providerList,entityList,payerList,transactions);
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
			provider.removeAllDocumentOnDisk();
			super.remove(provider);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	public void addProviderToPractise(Provider provider, Set<Practice>listPracitce){
		
		practiceDao.updatePractiseList(listPracitce);
		this.saveOrUpdate(provider);
	}
		
	public String isNameExist(String name, String lname, String npi){
		
		return service.isNameExist(name, lname, npi);
	}
	
	public void updateProviderList(Set<Provider>list){
		
		service.updateProviderList(list);
	}
	
	public List<Provider> findProvidersByEntity(Integer id){
		
		return service.findProvidersByEntity(id);
	}
}
