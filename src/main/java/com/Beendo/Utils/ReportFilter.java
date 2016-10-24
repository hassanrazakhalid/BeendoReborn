package com.Beendo.Utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportFilter {

	
	private Integer start = 0;
	private Integer maxResults = 20;
	private Integer totalRows = 0;
	
	private Integer entityId = Constants.RootEntityId;
	private List<Integer> providerIds = new ArrayList<>();
	private List<Integer> payerIds = new ArrayList<>();
	private List<Integer> practiceIds = new ArrayList<>();
	private List<String> statusList = new ArrayList<>();

	private Boolean shouldReloadRowCount;

	private ReportType reportType;
	
	private String baseQuery;
	
	public Boolean isFilterEmpty() {

		boolean isEmpty = true;
		if (entityId != Constants.RootEntityId)
			isEmpty = false;
		if (!providerIds.isEmpty())
			isEmpty = false;
		if (!payerIds.isEmpty())
			isEmpty = false;
		if (!statusList.isEmpty())
			isEmpty = false;
		if (!practiceIds.isEmpty())
			isEmpty = false;
		return isEmpty;
	}

	public void setEntityId(Integer entityId) {

		shouldReloadRowCount = true;
		this.entityId = entityId;
	}

	public void setProviderIds(List<Integer> providerIds) {

		shouldReloadRowCount = true;
		this.providerIds = providerIds;
	}
	
	public void setPracticeIds(List<Integer> practiceIds) {

		shouldReloadRowCount = true;
		this.practiceIds = practiceIds;
	}

	public void setPayerIds(List<Integer> payerIds) {

		shouldReloadRowCount = true;
		this.payerIds = payerIds;
	}
	
	

	public void setStatusList(List<String> statusList) {
		this.statusList = statusList;
	}

	public String getQueryString() {

		String query = baseQuery;

		if ( !isFilterEmpty() ){
			Boolean shouldCheck = true;
			switch (getReportType()) {
			case ReportTypeProvider:
				query += " HAVING type = " + TransactionType.Provider.getValue();
				
				break;
			case ReportTypePractise:
				query += " HAVING type = " + TransactionType.Practise.getValue();
				break;
			case ReportTypeTransaction:
				query += " WHERE";
				shouldCheck = false;
				break;
			}
			if (shouldCheck)
			if (!providerIds.isEmpty() ||
					!payerIds.isEmpty() ||
					!statusList.isEmpty() ||
					!practiceIds.isEmpty() ||
					entityId != Constants.RootEntityId)
					query += " AND";
		}
		
		if (entityId != Constants.RootEntityId) {
			query += " T.entity_id = " + entityId;

			if (!providerIds.isEmpty() ||
				!payerIds.isEmpty() ||
				!statusList.isEmpty() ||
				!practiceIds.isEmpty())
				query += " AND";
		}

		if (!providerIds.isEmpty()) {

			String ids = StringUtils.collectionToCommaDelimitedString(providerIds);
			query += " T.provider_id IN(" + ids + ")";

			if (!payerIds.isEmpty() || !statusList.isEmpty())
				query += " AND";
		}
		if (!practiceIds.isEmpty()) {

			String ids = StringUtils.collectionToCommaDelimitedString(practiceIds);
			query += " T.practice_id IN(" + ids + ")";

			if (!payerIds.isEmpty() || !statusList.isEmpty())
				query += " AND";
		}
		

		if (!payerIds.isEmpty()) {

			String ids = StringUtils.collectionToCommaDelimitedString(payerIds);
			query += " T.payer_id IN(" + ids + ")";

			if (!statusList.isEmpty())
				query += " AND";
		}
		
		if (!statusList.isEmpty()) {

			String statusString = "";
//			String ids = StringUtils.collectionToCommaDelimitedString(statusList);
			for (int i=0; i < statusList.size(); i++) {
				String string = statusList.get(i);
				statusString += StringUtils.quote(string);
				if (i != statusList.size()-1)
					statusString += ",";
			}
			
			query += " T.parStatus IN(" + statusString + ")";
		}

		
		return query;
	}
}
