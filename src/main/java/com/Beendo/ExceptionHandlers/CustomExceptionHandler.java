package com.Beendo.ExceptionHandlers;

import java.util.Iterator;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Beendo.Utils.SharedData;

public class CustomExceptionHandler extends ExceptionHandlerWrapper {
	
	private final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);
    private ExceptionHandler wrapped;
 
    CustomExceptionHandler(ExceptionHandler exception) {
        this.wrapped = exception;
    }
 
    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }
 
    @Override
    public void handle() throws FacesException {
 
        final Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();
        while (i.hasNext()) {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context =
                    (ExceptionQueuedEventContext) event.getSource();
 
            // get the exception from context
            Throwable t = context.getException();
 
            logger.info(t.toString());
            final FacesContext fc = FacesContext.getCurrentInstance();
            final Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
            final NavigationHandler nav = fc.getApplication().getNavigationHandler();
 
            //here you do what ever you want with exception
            try {
 
                //log error ?
//                log.log(Level.SEVERE, "Critical Exception!", t);
 
                //redirect error page
                requestMap.put("exceptionMessage", t.getMessage());
                nav.handleNavigation(fc, null, "/Views/Unsecured/Login/index");
                fc.renderResponse();
 
                // remove the comment below if you want to report the error in a jsf error message
                //JsfUtil.addErrorMessage(t.getMessage());
 
            } finally {
                //remove it from queue
                i.remove();
            }
        }
        //parent hanle
        getWrapped().handle();
    }
}
