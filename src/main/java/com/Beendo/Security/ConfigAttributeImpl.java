package com.Beendo.Security;

import org.springframework.security.access.ConfigAttribute;

public class ConfigAttributeImpl implements ConfigAttribute {

	private String name;
	
	@Override
	public String getAttribute() {
		// TODO Auto-generated method stub
		return name;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
