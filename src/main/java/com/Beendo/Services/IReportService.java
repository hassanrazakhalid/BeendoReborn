package com.Beendo.Services;

import com.Beendo.Entities.ProviderTransaction;
import com.Beendo.Utils.ReportType;

public interface IReportService extends GenericService<ProviderTransaction, Integer> {

	public void reloadPracticeReportData(IReportCallback callBack, ReportType type);
}
