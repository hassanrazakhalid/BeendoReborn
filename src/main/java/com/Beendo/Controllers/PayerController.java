package com.Beendo.Controllers;

import java.util.List;

import javax.faces.application.FacesMessage;

import org.hibernate.StaleObjectStateException;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.ProviderTransaction;
import com.Beendo.Entities.User;
import com.Beendo.Services.PayerService;
import com.Beendo.Services.TransactionService;
import com.Beendo.Services.UserService;
import com.Beendo.Utils.Constants;
import com.Beendo.Utils.OperationType;
import com.Beendo.Utils.Role;
import com.Beendo.Utils.SharedData;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
@Scope(value="session")
public class PayerController extends RootController {

	@Autowired
	private PayerService payerService;
	
	@Autowired
	private TransactionService transactionService;

	private Payer payer = new Payer();
	private List<Payer> payers;
	private OperationType operationType;
	
	@Autowired
	private UserService userService;
	
	private List<ProviderTransaction> transactions;

	private void refreshAllData(){
		
		payers = payerService.findAll();
		transactions = transactionService.fetchAllByRole();

		User tmpUser = userService.findById(SharedData.getSharedInstace().getCurrentUser().getId(), false);
		
		initHashThree(payers);
	}
	
	public void onLoad(){
	
		refreshAllData();
	}
	
	public String view() {
		
		return "PayerView";
	}

	public void updateClicked(Payer _payer) {
		payer = _payer;
		operationType = OperationType.Edit;
	}

	public void saveInfo() {

		try {
			
			switch (this.operationType) {
			case Create: {

				List<Payer> result = payerService.isNameExist(payers, payer.getName(), payer.getPlanName(), payer.getCity(),
						payer.getState(), payer.getZip(), payer.getStreet());
				if (result.size() <= 0) {
					payers.add(payer);
					payerService.save(payer);
					RequestContext.getCurrentInstance().execute("PF('Dlg1').hide()");
					//showMessage("Payer has been saved");
				} else
					showMessage("Payer info already exists!");
			}
				break;
			case Edit: {
				payerService.update(payer);
				RequestContext.getCurrentInstance().execute("PF('Dlg1').hide()");
				//showMessage("Payer has been updated");
			}
				break;

			default:
				break;
			}
		}
		catch (StaleObjectStateException e){
			
			showMessage(Constants.ERRR_RECORDS_OUDATED);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}

	
	public void removeClicked(Payer _payer) {
		
		try
		{
			payers.remove(_payer);
			
			for (ProviderTransaction providerTransaction : transactions) {
				
				for (Payer pay : providerTransaction.getPayerList()) {
					
					if(pay.getId() == _payer.getId())
					{
						providerTransaction.getPayerList().remove(pay);
						transactionService.update(providerTransaction);
					}
				}			
				
			}
			
			payerService.delete(_payer);
			refreshAllData();
			
		}
		catch (StaleObjectStateException e){
			
			showMessage(Constants.ERRR_RECORDS_OUDATED);
		}
		catch(Exception ex)
		{}
	}
	
	
	public void clearData() {
		payer = new Payer();
		operationType = OperationType.Create;
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
