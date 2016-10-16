package com.Beendo.Services;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.Beendo.Entities.Transaction;
import com.Beendo.Utils.ReportType;

public interface IReportService extends GenericService<Transaction, Integer> {


	public void reloadPracticeReportData(Consumer<Map<String,Object>> sender,int start, int end, int entityId, ReportType type); 
}
