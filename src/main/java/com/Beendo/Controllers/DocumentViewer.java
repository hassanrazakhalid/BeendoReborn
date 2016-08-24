package com.Beendo.Controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.primefaces.component.media.Media;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Controller;

import com.Beendo.Entities.Document;
import com.Beendo.Services.IDocumentService;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Controller
public class DocumentViewer {

	@Autowired
	private IDocumentService documentService;
	
	private StreamedContent steam;
//	private MediaService service;

	
	private Document document = null;
	
	public void onLoad(){
		
		Map<String, String> params =FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap();
		String id = params.get("id");
		
		document = documentService.get(Integer.valueOf(id));
		try {
			listener();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void listener() throws IOException {
	    // ...
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//	    ec.redirect(ec.getRequestContextPath() + "/newpage.xhtml");
	    ec.redirect(document.getFullPath());
	}
	
	public boolean isDocumetFound(){
		
		if(document != null)
			return true;
		else
			return false;
	}
	
	public String getDocumentPath(){
		
		String docPath = document.getFullPath()+ "?pfdrid_c=true";
		return docPath;
	}
	
	   public StreamedContent getStream() throws IOException {
	        FacesContext context = FacesContext.getCurrentInstance();

	        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
	            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
	            return new DefaultStreamedContent();
	        } else {
	            // So, browser is requesting the media. Return a real StreamedContent with the media bytes.
//	            String id = context.getExternalContext().getRequestParameterMap().get("id");
//	            Media media = service.find(Long.valueOf(id));
	        	InputStream is = 
	                     new ByteArrayInputStream(getFileStream());
	        	
	            return new DefaultStreamedContent(is);
	        }
	    }
	   
	   private byte[] getFileStream(){
		   
			File file = new File("C:\\Users\\Hassan\\Documents\\partracker-doc\\1\\1_Copy_CLIA.pdf");
			Path path = Paths.get("C:\\Users\\Hassan\\Documents\\partracker-doc\\1\\1_Copy_CLIA.pdf");
			try (FileInputStream fis = new FileInputStream(file)) {

				System.out.println("Total file size to read (in bytes) : "+ fis.available());

				int content;
		/*		while ((content = fis.read()) != -1) {
					// convert to char and display it
					System.out.print((char) content);
				}*/
				
				byte[] data = Files.readAllBytes(path);
				return data;

			} catch (IOException e) {
				e.printStackTrace();
			}
		   return null;
	   }
}
