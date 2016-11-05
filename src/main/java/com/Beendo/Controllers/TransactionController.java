package com.Beendo.Controllers;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;

import org.hibernate.StaleObjectStateException;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.Beendo.Dto.LazyTransactionModel;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.Transaction;
import com.Beendo.Entities.User;
import com.Beendo.Services.IEntityService;
import com.Beendo.Services.IReportService;
import com.Beendo.Services.ITransactionCallback;
import com.Beendo.Services.ITransactionService;
import com.Beendo.Utils.Constants;
import com.Beendo.Utils.ReportFilter;
import com.Beendo.Utils.ReportType;
import com.Beendo.Utils.Role;
import com.Beendo.Utils.SharedData;
import com.github.javaplugs.jsf.SpringScopeView;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
//@Scope(value="session")
@SpringScopeView
public class TransactionController extends BaseViewController implements DisposableBean, Serializable{
	
/**
	 * 
	 */
	private static final long serialVersionUID = -6178498762662211117L;

	//	extends RootController
	@Autowired
	private ITransactionService transactionService;

	private LazyTransactionModel lazyModel;
	private List<Transaction> transactions = new ArrayList<>();
	private User tmpUser;

	private List<Integer> selectedPayers = new ArrayList<>();
	private List<Transaction> filterTransactions = new ArrayList<>();
	private List<Transaction> realTimefilterList = new ArrayList<>();
	//private HashMap<Integer, Practice> _hash = new HashMap<Integer, Practice>();
	private List<Payer> payerList = new ArrayList<>();
	
	@Autowired
	IReportService reportService;
	private ReportFilter reportFilter = new ReportFilter();
	
	private void refreshAllData(){
		
		Integer entityId = SharedData.getSharedInstace().getCurrentUser().getEntity().getId();

		transactionService.getLatestTransactions(entityId);
		
		reportFilter.setEntityId(entityId);
		reportFilter.setReportType(ReportType.ReportTypeTransaction);
		
		ITransactionCallback callBack = (User user, List<Transaction>transactions, List<Payer>payerList, List<Practice>practiceList, List<Provider>providerList, Map<String,Object>otherInfo)->{
			
			tmpUser = user;
			this.lazyModel = new LazyTransactionModel(reportService,entityId, reportFilter);
			this.lazyModel.setRowCount((Integer)otherInfo.get("count"));
//			this.lazyModel.setRowCount(20);
//			this.transactions = transactions;
			this.payerList = payerList;
			this.filterTransactions.clear();
			this.filterTransactions.addAll(transactions);
		};

		transactionService.refreshAllData(0, 10, entityId, callBack);
	}
	
	public void searchButtonPressed(){
		seachResult();
	}
	
	private void seachResult(){
		reportFilter.setPayerIds(selectedPayers);
	}
	
	public boolean canEdit(){
		
		boolean isOK = true;
		if (tmpUser.getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString()) &&
				!tmpUser.getPermission().isCanPayerTransactionEdit())
			isOK = false;
		return isOK;
	}
	
	public boolean canCreate(){
		
		boolean isOK = true;		
		if (tmpUser.getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString()) &&
				!tmpUser.getPermission().isCanPayerTransactionAdd())
			isOK = false;
		return isOK;
	}
	
	
	public String moveToCreateTransaction(){
		return "CreateTransaction";
	}
	
	@PostConstruct
	public void onLoad(){

		refreshAllData();
	}
	
	public boolean shouldShowDelete(){
		
		boolean show = true;
		
		if(tmpUser.getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString()))
			show = false;
		return show;
	}
	
	public void removeClicked(Transaction transac)
	{
		
		try {
			
			transactionService.remove(transac);
			transactions.remove(transac);
			realTimefilterList.remove(transac);
			filterTransactions.remove(transac);
			refreshAllData();
			
		}
		catch (StaleObjectStateException e){
			
			showMessage(FacesMessage.SEVERITY_ERROR, "Transaction", Constants.ERRR_RECORDS_OUDATED);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
//		if(transac.getPractice() != null)
//		{
//			List<Integer> ids =  new ArrayList<>();
//			ids.add(transac.getPractice().getId());
//			transactionService.deleteTransactionByPractics(ids);
//		}
//		else
//		{
//			List<Integer> ids =  new ArrayList<>();
//			ids.add(transac.getProvider().getId());
//			transactionService.deleteTransactionByProvider(ids);
//		}
		
	}
	
//	public void showMessage(String msg) {
//		
//        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Transaction", msg);     
//        RequestContext.getCurrentInstance().showMessageInDialog(message);
//    }
	

	//Filter Code
	/**
	 * Filter Data logic
	 * @param obj
	 */
/*	public Predicate<Transaction> getFilterPredicate(){
		
//		filterTransactions.clear();
//		if(payerFilter.size() <= 0)
//		{
//			// show all
//			filterTransactions.addAll(transactions);
//		}
//		else
//		{
			
			Predicate<Transaction> predicate = (t)->{
				
				if (payerFilter.isEmpty())
					return true;
				
				Optional<Integer> res = payerFilter.stream()
						.filter((payerId) -> payerId == t.getPayer().getId()).findFirst();
			
				if(res.isPresent())
				{
//					System.out.println("true");
					return true;
				}
				else
				{
//					System.out.println("false");
					return false;
				}
			};
			
			return predicate;

	}*/
	

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("");
	}
}
