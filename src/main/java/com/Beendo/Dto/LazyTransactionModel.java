package com.Beendo.Dto;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import com.Beendo.Entities.Provider;
import com.Beendo.Entities.ProviderTransaction;
import com.Beendo.Services.ITransactionService;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LazyTransactionModel extends LazyDataModel<ProviderTransaction> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -980668438398121709L;

	private ITransactionService transactionService;

	private List<ProviderTransaction> datasource;

	private Integer entityId;

	public LazyTransactionModel(ITransactionService transactionService, Integer entityId) {

		// this.datasource = datasource;
		this.entityId = entityId;
		this.transactionService = transactionService;
	}

	@Override
	public ProviderTransaction getRowData(String rowKey) {

		Optional<ProviderTransaction> res = datasource.stream().filter((p) -> {

			return true;
		}).findFirst();

		return res.get();
		// return super.getRowData(rowKey);
	}

	@Override
	public Object getRowKey(ProviderTransaction object) {
		// TODO Auto-generated method stub
		// return super.getRowKey(object);
		return object.getId();
	}

	@Override
	public List<ProviderTransaction> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {

		datasource = fetchData(first, pageSize);

		if (filters != null && !filters.isEmpty()) {
			return filerData(datasource, filters);
		}

		return datasource;
		// return super.load(first, pageSize, sortField, sortOrder, filters);
	}

	private List<ProviderTransaction> filerData(List<ProviderTransaction> list, Map<String, Object> filters) {

		
		try {
			for (PropertyDescriptor pd : Introspector.getBeanInfo(Provider.class).getPropertyDescriptors()) {
				 System.out.println(pd.getReadMethod());
//			    System.out.println(pd.getReadMethod().invoke(foo));
				}
		} catch (IntrospectionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		List<ProviderTransaction> res = new ArrayList<>();
		for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
			try {
				String filterProperty = it.next();
				String valueToSearch = (String)filters.get(filterProperty);

				res = list.stream().filter((p) -> {

					try {
						if (p.getProvider() != null) {
						String fieldValue = (String)p.getProvider().getClass().getMethod(filterProperty).invoke(p.getProvider());
//							String fieldValue = (String) new PropertyDescriptor("getFullName", Provider.class).getReadMethod().invoke(p.getProvider());
							
						if (StringUtils.containsIgnoreCase(fieldValue, valueToSearch)) {
								return true;
							}
						}
					} catch (Exception e) {
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

	private List<ProviderTransaction> fetchData(int first, int pageSize) {

		List<ProviderTransaction> res = transactionService.fetchAllByRole(first, pageSize, entityId);
		return res;
	}
}
