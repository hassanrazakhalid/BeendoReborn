package com.Beendo.Utils;

import com.Beendo.Entities.User;


public class SharedData {
	private static SharedData instance = null;
	public User currentUser;
	
public static SharedData getSharedInstace(){
		
		if(instance ==  null)
			instance = new SharedData();
		return instance;
	}
	
}
