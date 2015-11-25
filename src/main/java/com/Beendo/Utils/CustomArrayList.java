package com.Beendo.Utils;

import java.util.ArrayList;
import java.util.ListIterator;

public class CustomArrayList<T> extends ArrayList<T> {

	
   
	public boolean contains(ICache<T> sender){

		 ListIterator<T> litr = this.listIterator();
	      while(litr.hasNext()) {
	         ICache element = (ICache)litr.next();
	         
	         if(element.getCacheId() == sender.getCacheId())
	        	 return true;
	      }
		
		
		return false;
	}
}
