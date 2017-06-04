package com.Beendo.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.Beendo.Entities.Practice;
import com.Beendo.Entities.User;
import com.Beendo.Services.IPractiseService;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportFilter {

	private enum QueryState {

		start, join, firstCondition, conditions
	}

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
	private QueryState state = QueryState.start;

	private IPractiseService practiseService;
	
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

	private void addConditions(String key, Object value) {

		// assuming only valid coditions are comming
		if (value instanceof List) {
			String statusString = "";
			if (key.equalsIgnoreCase("transactionState")) {
				// String ids =
				// StringUtils.collectionToCommaDelimitedString(statusList);
				for (int i = 0; i < statusList.size(); i++) {
					String string = statusList.get(i);
					statusString += StringUtils.quote(string);
					if (i != statusList.size() - 1)
						statusString += ",";
				}
			} else {	
				List<Integer> idsList = (List<Integer>) value;
				statusString = StringUtils.collectionToCommaDelimitedString(idsList);
			}
			baseQuery += " ts." + key + " IN(" + statusString + ")";
		} else if (value instanceof Integer) {
			Integer tmpId = (Integer) value;
			baseQuery += " " + key + " = " + tmpId;
		}
	}

	private String getProviderPractiseBaseString(List<String> groupByIds, Boolean forProvider , Boolean countModeOn) {
		
		String joinColumn = forProvider ? "provider_id" : "practice_id" ; 
		
		String commaSepGroupIds = String.join(",", groupByIds);
		String finalbaseString = "SELECt * from transaction  as ts"
				+ " inner join"
				+ " ( SELECT Max(t.transactionDate) as MaxTransactoin, t.payer_id as subPayerId, t." + joinColumn + " as subJoin , t.plan_id as subPlanId" + " FROM transaction  t" + " GROUP BY "
				+ commaSepGroupIds + " )" //order by t.id
				+ " as p on p.MaxTransactoin=ts.transactionDate"
				+ " and p.subPayerId = ts.payer_id"
				+ " and p.subJoin = ts." + joinColumn
				+ " and p.subPlanId = ts.plan_id";

		if (!countModeOn) {
			finalbaseString += geJoins();
		}
		return finalbaseString;
	}

	private String getTransactionBaseString( Boolean countModeOn) {
		String finalbaseString = "SELECt * from transaction  as ts";
		if (!countModeOn) {
			finalbaseString += geJoins();
			finalbaseString += " GROUP BY ts.id";
		}
		return finalbaseString;
	}
	
	private String geJoins(){
		return " LEFT JOIN payer AS P ON P.id = ts.payer_id"
//				+ " LEFT JOIN  transaction_plan AS planIds ON planIds.transaction_id = T.id"
				+ " LEFT JOIN plan AS PL ON PL.id = ts.plan_id";
	}

	private void updatePractiseIdsByUser(){
		
		User user = SharedData.getSharedInstace().getCurrentUser();
		List<Practice> tmpList = practiseService.getPracticeByUser(user.getId());
//		Set<Practice> tmpList = SharedData.getSharedInstace().getCurrentUser().getPractises();
		
//		tmpList.stream().map(Practice::getId);
		Set<Integer> tmpIds = tmpList .stream().map(Practice::getId ).collect(Collectors.toSet());
		practiceIds.addAll(tmpIds);
	}
	
	public String getQueryString(Boolean countMode) {

		User user = SharedData.getSharedInstace().getCurrentUser();
		String userRole = user.getRoleName();
		
		String str = "";
		switch (reportType) {
		case ReportTypeTransaction:
			
			if(userRole.equalsIgnoreCase(Role.ENTITY_USER.toString()) &&
					practiceIds.isEmpty()) {
				updatePractiseIdsByUser();
			}
			str = getTransactionBaseString(countMode);
			break;
		case ReportTypePractise:
			//Getting specific
			
			if(userRole.equalsIgnoreCase(Role.ENTITY_USER.toString()) &&
					practiceIds.isEmpty()) {
				updatePractiseIdsByUser();
			}
			//Here false is for practise
			str = getProviderPractiseBaseString(Arrays.asList("t.practice_id", "t.plan_id", "t.payer_id"),false,countMode);
			break;
		case ReportTypeProvider:
			//Here ttue is for provider
			str = getProviderPractiseBaseString(Arrays.asList("t.provider_id", "t.plan_id", "t.payer_id"),true, countMode);
			break;
		default:
			break;
		}		
		baseQuery = str;

		Map<String, Object> conditionsMap = new HashMap<>();

		if (getReportType() == ReportType.ReportTypeProvider)
			conditionsMap.put("type", TransactionType.Provider.getValue());
		else if (getReportType() == ReportType.ReportTypePractise)
			conditionsMap.put("type", TransactionType.Practise.getValue());

		if (entityId != Constants.RootEntityId) {
			conditionsMap.put("entity_id", entityId);
		}
		if (!providerIds.isEmpty())
			conditionsMap.put("provider_id", providerIds);
		if (!practiceIds.isEmpty())
			conditionsMap.put("practice_id", practiceIds);
		if (!payerIds.isEmpty())
			conditionsMap.put("payer_id", payerIds);
		if (!statusList.isEmpty())
			conditionsMap.put("transactionState", statusList);

		// addConditions(conditionsMap);

		state = QueryState.firstCondition;
		for (Map.Entry<String, Object> entry : conditionsMap.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			switch (state) {
			case firstCondition:
				switch (getReportType()) {
				case ReportTypeProvider:
				case ReportTypePractise:
				case ReportTypeTransaction:
					baseQuery += " HAVING";
					addConditions(key, value);
					state = QueryState.conditions;
					break;
				}
				break;
			case conditions:
				baseQuery += " AND";
				addConditions(key, value);
				break;
			default:
				break;
			}
		}
		return baseQuery;
		//
		//// if ( !isFilterEmpty() ){
		// Boolean shouldCheck = true;
		// switch (getReportType()) {
		// case ReportTypeProvider:
		// query += " HAVING type = " + TransactionType.Provider.getValue();
		//
		// break;
		// case ReportTypePractise:
		// query += " HAVING type = " + TransactionType.Practise.getValue();
		// break;
		// case ReportTypeTransaction:
		// if (entityId != Constants.RootEntityId)
		// query += " WHERE";
		// shouldCheck = false;
		// break;
		// }
		// if (shouldCheck)
		// if (!providerIds.isEmpty() ||
		// !payerIds.isEmpty() ||
		// !statusList.isEmpty() ||
		// !practiceIds.isEmpty() ||
		// entityId != Constants.RootEntityId)
		// query += " AND";
		//// }
		//
		// if (entityId != Constants.RootEntityId) {
		// query += " T.entity_id = " + entityId;
		//
		// if (!providerIds.isEmpty() ||
		// !payerIds.isEmpty() ||
		// !statusList.isEmpty() ||
		// !practiceIds.isEmpty())
		// query += " AND";
		// }
		//
		// if (!providerIds.isEmpty()) {
		//
		// String ids =
		// StringUtils.collectionToCommaDelimitedString(providerIds);
		// query += " T.provider_id IN(" + ids + ")";
		//
		// if (!payerIds.isEmpty() || !statusList.isEmpty())
		// query += " AND";
		// }
		// if (!practiceIds.isEmpty()) {
		//
		// String ids =
		// StringUtils.collectionToCommaDelimitedString(practiceIds);
		// query += " T.practice_id IN(" + ids + ")";
		//
		// if (!payerIds.isEmpty() || !statusList.isEmpty())
		// query += " AND";
		// }
		//
		//
		// if (!payerIds.isEmpty()) {
		//
		// String ids = StringUtils.collectionToCommaDelimitedString(payerIds);
		// query += " T.payer_id IN(" + ids + ")";
		//
		// if (!statusList.isEmpty())
		// query += " AND";
		// }
		//
		// if (!statusList.isEmpty()) {
		//
		// String statusString = "";
		//// String ids =
		// StringUtils.collectionToCommaDelimitedString(statusList);
		// for (int i=0; i < statusList.size(); i++) {
		// String string = statusList.get(i);
		// statusString += StringUtils.quote(string);
		// if (i != statusList.size()-1)
		// statusString += ",";
		// }
		//
		// query += " T.parStatus IN(" + statusString + ")";
		// }
		//
		//
		// return query;
	}

	// public String getQueryString() {
	//
	// String query = baseQuery;
	//
	// if ( !isFilterEmpty() ){
	// Boolean shouldCheck = true;
	// switch (getReportType()) {
	// case ReportTypeProvider:
	// query += " HAVING";
	// query += " T.type = " + TransactionType.Provider.getValue();
	//
	// break;
	// case ReportTypePractise:
	// query += " HAVING";
	// query += " HAVING T.type = " + TransactionType.Practise.getValue();
	// break;
	// case ReportTypeTransaction:
	// query += " WHERE";
	// shouldCheck = false;
	// break;
	// }
	// if (shouldCheck)
	// if (!providerIds.isEmpty() ||
	// !payerIds.isEmpty() ||
	// !statusList.isEmpty() ||
	// !practiceIds.isEmpty() ||
	// entityId != Constants.RootEntityId);
	// query += " AND";
	// }
	//
	// if (entityId != Constants.RootEntityId) {
	// query += " E.id = " + entityId;
	//
	// if (!providerIds.isEmpty() ||
	// !payerIds.isEmpty() ||
	// !statusList.isEmpty() ||
	// !practiceIds.isEmpty())
	// query += " AND";
	// }
	//
	// if (!providerIds.isEmpty()) {
	//
	// String ids = StringUtils.collectionToCommaDelimitedString(providerIds);
	// query += " Pro.id IN(" + ids + ")";
	//
	// if (!payerIds.isEmpty() || !statusList.isEmpty())
	// query += " AND";
	// }
	// if (!practiceIds.isEmpty()) {
	//
	// String ids = StringUtils.collectionToCommaDelimitedString(practiceIds);
	// query += " Pra.id IN(" + ids + ")";
	//
	// if (!payerIds.isEmpty() || !statusList.isEmpty())
	// query += " AND";
	// }
	//
	//
	// if (!payerIds.isEmpty()) {
	//
	// String ids = StringUtils.collectionToCommaDelimitedString(payerIds);
	// query += " Pay.id IN(" + ids + ")";
	//
	// if (!statusList.isEmpty())
	// query += " AND";
	// }
	//
	// if (!statusList.isEmpty()) {
	//
	// String statusString = "";
	//// String ids = StringUtils.collectionToCommaDelimitedString(statusList);
	// for (int i=0; i < statusList.size(); i++) {
	// String string = statusList.get(i);
	// statusString += StringUtils.quote(string);
	// if (i != statusList.size()-1)
	// statusString += ",";
	// }
	//
	// query += " T.parStatus IN(" + statusString + ")";
	// }
	//
	//
	// return query;
	// }
}
