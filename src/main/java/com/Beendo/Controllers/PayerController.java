package com.Beendo.Controllers;

import java.util.Arrays;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.hibernate.StaleObjectStateException;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Transaction;
import com.Beendo.Entities.User;
import com.Beendo.Services.IPayerService;
import com.Beendo.Services.ITransactionService;
import com.Beendo.Services.IUserService;
import com.Beendo.Services.PayerService;
import com.Beendo.Services.TransactionService;
import com.Beendo.Services.UserService;
import com.Beendo.Utils.Constants;
import com.Beendo.Utils.OperationType;
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
public class PayerController {

	@Autowired
	private IPayerService payerService;
	
	@Autowired
	private ITransactionService transactionService;

	private Payer payer = new Payer();
	private List<Payer> payers;
	
	
	@Autowired
	private IUserService userService;
	
	private List<Transaction> transactions;

	private void refreshAllData(){
		
		payers = payerService.executeListQuery("select P from Payer P left join fetch P.plans");
//		transactions = transactionService.fetchAllByRole();

		User tmpUser = userService.findById(SharedData.getSharedInstace().getCurrentUser().getId(), false);
		
//		initHashThree(payers);
	}
	
	public String createPayerClikced(){
		
		return "CreatePayer";
	}
	
	public void onLoad(){
	
		refreshAllData();
	}
	
	public String view() {
		
		return "PayerView";
	}
	
	public void removeClicked(Payer _payer) {
		
		try
		{	
			payerService.remove(_payer);
			payers.remove(_payer);
			refreshAllData();
			
		}
		catch (StaleObjectStateException e){
			
			showMessage(Constants.ERRR_RECORDS_OUDATED);
		}
		catch(Exception ex)
		{}
	}

	public void showMessage(String msg) {

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Payer", msg);
		RequestContext.getCurrentInstance().showMessageInDialog(message);
	}
	
	public boolean shouldShowPayerLink(){
		
		boolean isOK = true;
	
		if (SharedData.getSharedInstace().getCurrentUser().getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString()) ||
			SharedData.getSharedInstace().getCurrentUser().getRoleName().equalsIgnoreCase(Role.ENTITY_ADMIN.toString())		)
			isOK = false;
		
		return isOK;
	} 

}
