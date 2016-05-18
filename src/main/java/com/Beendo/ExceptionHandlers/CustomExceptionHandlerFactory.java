package com.Beendo.ExceptionHandlers;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

public class CustomExceptionHandlerFactory extends ExceptionHandlerFactory {

	private ExceptionHandlerFactory parent;
	
	// this injection handles jsf
	   public CustomExceptionHandlerFactory(ExceptionHandlerFactory parent) {
	    this.parent = parent;
	   }
	
	@Override
	public ExceptionHandler getExceptionHandler() {
		// TODO Auto-generated method stub
		ExceptionHandler handler = new CustomExceptionHandler(parent.getExceptionHandler());
		 
        return handler;
	}

}
