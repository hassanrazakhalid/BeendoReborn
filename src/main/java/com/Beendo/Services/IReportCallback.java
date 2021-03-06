package com.Beendo.Services;

import java.util.List;

import com.Beendo.Entities.Payer;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.Transaction;
import com.Beendo.Utils.ReportType;

@FunctionalInterface
public interface IReportCallback {

	public void reloadPracticeReportData(List<Practice>practiseList, List<Provider>providerList, List<Payer>payerist, List<Transaction>listTransaction, ReportType type);
}
