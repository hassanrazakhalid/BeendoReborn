package com.Beendo.Controllers;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;

import org.hibernate.StaleObjectStateException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.Beendo.Dto.DocumentCell;
import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Document;
import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.Transaction;
import com.Beendo.Entities.User;
import com.Beendo.Services.EntityService;
import com.Beendo.Services.IEntityService;
import com.Beendo.Services.IPayerService;
import com.Beendo.Services.IPractiseService;
import com.Beendo.Services.IProviderService;
import com.Beendo.Services.ITransactionService;
import com.Beendo.Services.IUserService;
import com.Beendo.Services.PayerService;
import com.Beendo.Services.PractiseService;
import com.Beendo.Services.ProviderService;
import com.Beendo.Services.TransactionService;
import com.Beendo.Services.UserService;
import com.Beendo.Utils.Constants;
import com.Beendo.Utils.OperationType;
import com.Beendo.Utils.Role;
import com.Beendo.Utils.Screen;
import com.Beendo.Utils.SharedData;
import com.github.javaplugs.jsf.SpringScopeView;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
//@Scope(value = "session")
@SpringScopeView
public class ProviderController extends BaseViewController implements DisposableBean,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7529847705103493695L;

	@Autowired
	private IProviderService providerService;

	private List<Provider> providerList = new ArrayList<>();



//	private List<Payer> payerList = new ArrayList<>();;
//	private List<Integer> selectedPayers = new ArrayList<>();;


	private User tmpUser;
	private List<DocumentCell> documentCells = new ArrayList<>();;


	

	private void refreshAllData() {

		documentCells = new ArrayList<>();
		
		ProviderCallback response = (User user, List<Provider>providerList)->{

			this.tmpUser = user;
//			System.out.println(tmpUser.canEditTransaction());
			this.providerList = providerList;
			
		};
		providerService.refreshAllData(response);
	}

	public void onLoad() {

		refreshAllData();
	}

	public String view() {

		return "ProviderView";
	}



	public boolean shouldShowDelete() {

		boolean show = true;

		if (tmpUser.getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString()))
			show = false;
		return show;
	}



	public boolean canEditProvider() {

		boolean isOK = true;

		if (tmpUser.getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString())
				&& !tmpUser.getPermission().isCanProviderEdit())
			isOK = false;

		// if
		// (SharedData.getSharedInstace().getCurrentUser().getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString())
		// &&
		// !SharedData.getSharedInstace().getCurrentUser().getPermission().isCanProviderEdit())
		// isOK = false;

		return isOK;
	}

	public boolean canCreateProvider() {

		boolean isOK = true;

		if (tmpUser.getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString())
				&& !tmpUser.getPermission().isCanProviderAdd())
			isOK = false;
		// if
		// (SharedData.getSharedInstace().getCurrentUser().getRoleName().equalsIgnoreCase(Role.ENTITY_USER.toString())
		// &&
		// !SharedData.getSharedInstace().getCurrentUser().getPermission().isCanProviderAdd())
		// isOK = false;

		return isOK;
	}



	public void removeClicked(Provider provider) {

		try {

//			int sz = provider.getPracticeList().size();

			// currentEntity.setPracticeList(provider.getPracticeList());
			// provider.setCentity(currentEntity);

			// providerService.update(provider);
			// entityService.update(currentEntity);
			providerService.remove(provider);
			providerList.remove(provider);
			refreshAllData();
			// entityService.update(currentEntity);

		} catch (StaleObjectStateException e) {

			showMessage(FacesMessage.SEVERITY_ERROR, "Provider", Constants.ERRR_RECORDS_OUDATED);
		} catch (Exception ex) {
		}
	}
	
	public String moveToEditView(){
		
		return "EditProviderView";
	}

	public String createProviderClicked() {

		return "CreateProvider";
	}



	public String getExtension(String fileName){
		
		List<String> strList = new ArrayList<>(Arrays.asList(fileName.split("\\."))) ;
		
		return strList.get(strList.size()-1);
	}
	
//	/**
//	 * Upload Code
//	 * 
//	 * @param Called
//	 *            when file is selected
//	 */
//	public void handleFileUpload(FileUploadEvent event) {
//
//		UploadedFile file = event.getFile();
//		DocumentCell obj =  (DocumentCell)event.getComponent().getAttributes().get("name");
//
//		Document doc = (Document)obj.getDocument();//provider.getFilenameByType(docName);
//
//		doc.setOrignalName(event.getFile().getFileName());
//		doc.setNameOnDisk(provider.getId()+"_"+doc.getType()+"."+getExtension(file.getFileName()));
//		doc.removeFileOnDisk();
//		
//		try {
//			file.write(doc.getFullPath());
//			FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
//			RequestContext.getCurrentInstance().showMessageInDialog(message);
//			providerService.addDocumentToProvider(provider, doc);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public void deleteFileClicked(DocumentCell cell) {
//
//		providerService.removeDocumentFromProvider(provider,cell.getDocument());
//		documentCells = provider.getDocumentCellList();
//		
////		for (DocumentCell tmpCell : documentCells) {
////			
////			System.out.println(tmpCell.getDocument().getOrignalName());
////		}
//	}



	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("");
	}
}
