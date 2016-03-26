package com.Beendo.Services;

import java.util.List;

import com.Beendo.Entities.CEntitiy;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.User;

public interface IPracticeCallBack {

	public void refreshAllData(User user, List<CEntitiy>entityList, List<Practice>practiceList);
}
