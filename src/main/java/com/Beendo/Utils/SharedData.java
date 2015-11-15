package com.Beendo.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;

import com.Beendo.Entities.Practice;
import com.Beendo.Entities.User;


@Service
public class SharedData {

//    private static AtomicReference<SharedData> INSTANCE = new AtomicReference<SharedData>();

	private static SharedData instance = null;
	
    private List<Practice> listPractise;
    
    public User currentUser;
    
    private void init(){
    	
    	listPractise = new ArrayList<Practice>();
    	
    }
    
    public SharedData() {
//        final SharedData previous = INSTANCE.getAndSet(this);
//        if(previous != null)
//            throw new IllegalStateException("Second singleton " + this + " created after " + previous);
        
        this.init();
    }

    public static SharedData getSharedInstace() {

    	if(instance == null)
    		instance = new SharedData();
        return instance;
    }

    // Practise List Code
    
    public void addPactiseList(List<Practice> sender){
    	
    	listPractise.addAll(sender);
    }
    
    public void addPactise(Practice sender){
    	
    	listPractise.add(sender);
    }
    
    public void removePactise(Practice sender){
    	
    	listPractise.remove(sender);
    }

	public List<Practice> getListPractise() {
		// TODO Auto-generated method stub
		return listPractise;
	} 
}