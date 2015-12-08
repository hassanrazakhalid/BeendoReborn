package com.Beendo.Services;

import com.Beendo.Dao.ICRUD;
import com.Beendo.Entities.Practice;

public interface IPractise extends ICRUD<Practice, Integer> {

	public String checkDuplicateUsername(String name);
}
 