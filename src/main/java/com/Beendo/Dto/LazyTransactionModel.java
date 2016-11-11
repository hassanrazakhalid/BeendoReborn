package com.Beendo.Dto;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.Beendo.Entities.Transaction;
import com.Beendo.Services.IReportService;
import com.Beendo.Utils.ReportFilter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LazyTransactionModel extends LazyDataModel<Transaction> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -980668438398121709L;

	private IReportService reportingService;

	public List<Transaction> datasource = new ArrayList<>();

	private Integer entityId;

	private ReportFilter filter;
	
	private Integer _first = -1;
	private Integer _pageSize = 0;
	
	public LazyTransactionModel(IReportService transactionService, Integer entityId, ReportFilter filter) {

		// this.datasource = datasource;
		this.entityId = entityId;
		this.reportingService = transactionService;
		this.filter = filter;
	}

	@Override
	public Transaction getRowData(String rowKey) {

		Optional<Transaction> res = datasource.stream().filter((p) -> {

			return true;
		}).findFirst();

		return res.get();
		// return super.getRowData(rowKey);
	}

	@Override
	public Object getRowKey(Transaction object) {
		// TODO Auto-generated method stub
		// return super.getRowKey(object);
		return object.getId();
	}

	@Override
	public List<Transaction> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {

		
		
		if (this._first == first &&
			this._pageSize == pageSize &&
			!filter.getShouldReloadRowCount()) {
			
			return filerData(datasource, filters);
			//Filter the existing records
		}
		else {//fetch new records from DB
			
			datasource = fetchData(first, pageSize);
		}

		_first = first;
		_pageSize = pageSize;
		return datasource;

	}

	private List<Transaction> filerData(List<Transaction> list, Map<String, Object> filters) {
		
//		try {
//			for (PropertyDescriptor pd : Introspector.getBeanInfo(Provider.class).getPropertyDescriptors()) {
//				 System.out.println(pd.getReadMethod());
////			    System.out.println(pd.getReadMethod().invoke(foo));
//				}
//		} catch (IntrospectionException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		List<Transaction> res = new ArrayList<>();
		for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
			try {
				String filterProperty = it.next();
				String valueToSearch = (String)filters.get(filterProperty);
				 
				res = list.stream().filter((p) -> {

					try {
//						if (p.getProvider() != null) {
//							System.out.println(filterProperty);
						String propertyList[] = filterProperty.split("\\.");
						Object tmp =  getFieldValue(null, propertyList, 0, p);//field.get(p);
						if (tmp == null){
							return false;
						}
						String fieldValue = String.valueOf(tmp);
//						String fieldValue = (String)p.getProvider().getClass().getMethod(filterProperty).invoke(p.getProvider());
//							String fieldValue = (String) new PropertyDescriptor("getFullName", Provider.class).getReadMethod().invoke(p.getProvider());
							
						if (StringUtils.containsIgnoreCase(fieldValue, valueToSearch)) {
								return true;
							}
//						}
					} 
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					return false;
				}).collect(Collectors.toList());

			} catch (Exception e) {
				// match = false;
			}

			return res;
		}
		// if (match) {
		// data.add(car);
		// }

		return datasource;

	}
	
	private Object getFieldValue(Field field , String propertyList[], int index, Object obj){
		
		String property = propertyList[index];
		
		
		try {
			
			Object tmp = null;
			Field tmpField = null;
			if (property.contains("()")) {
					
				Method methods = obj.getClass().getMethod(property.replace("()", ""), null);
				tmp = methods.invoke(obj, null);	
			}
			
			else {
				 tmpField = obj.getClass().getDeclaredField(property);
				tmpField.setAccessible(true);
				 tmp = tmpField.get(obj);
				
			}
			if (tmp == null)
				return null;
			
			if (index+1 < propertyList.length)
				return getFieldValue(tmpField, propertyList, ++index, tmp);
			else
				return tmp;//getFieldValue(tmpField, propertyList, ++index, tmp);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private List<Transaction> fetchData(int first, int pageSize) {

		filter.setStart(first);
		filter.setMaxResults(pageSize);
		
		List<Transaction> res = reportingService.getTransactionByProvider(filter);
		setRowCount(filter.getTotalRows());
//		List<Transaction> res = transactionService.fetchAllByRole(first, pageSize, entityId);
		return res;
	}
}
