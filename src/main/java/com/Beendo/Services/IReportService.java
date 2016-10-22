package com.Beendo.Services;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.Beendo.Entities.Transaction;
import com.Beendo.Utils.ReportFilter;
import com.Beendo.Utils.ReportType;

public interface IReportService extends GenericService<Transaction, Integer> {


	public void reloadPracticeReportData(Consumer<Map<String,Object>> sender, ReportFilter filter , ReportType type); 

	public List<Transaction> getTransactionByProvider(ReportFilter filter);
	public Integer getPageSize(ReportFilter filter);
}
