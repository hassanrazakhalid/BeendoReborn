package com.Beendo.Services;

import java.util.Set;

import com.Beendo.Dao.ICRUD;
import com.Beendo.Entities.Practice;

public interface IPractise extends ICRUD<Practice, Integer> {

	public String checkDuplicateUsername(String name);
	
	public void updatePractiseList(Set<Practice>list);
}
 