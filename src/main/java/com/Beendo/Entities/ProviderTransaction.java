package com.Beendo.Entities;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "PROVIDER_TRANSACTION")
public class ProviderTransaction {

	@Id
	@GeneratedValue
	private Integer id;
	private String transactionType;
	private String transactionDate;
	private String comments;
	private String parStatus;
	
	@ManyToOne
	private CEntitiy entity;
	
	/*@OneToOne(fetch = FetchType.EAGER)
	private Payer payer = new Payer();*/
	
	@ManyToMany(fetch = FetchType.EAGER, cascade =  {CascadeType.PERSIST,CascadeType.REFRESH, CascadeType.MERGE})
	private Set<Payer> payerList = new HashSet<Payer>();
	
	@ManyToOne(fetch = FetchType.EAGER, cascade =  {CascadeType.PERSIST,CascadeType.REFRESH, CascadeType.MERGE})
	private Practice practice = new Practice();
	
	@ManyToOne(fetch = FetchType.EAGER, cascade =  {CascadeType.PERSIST,CascadeType.REFRESH, CascadeType.MERGE})
	private Provider provider = new Provider();
	
	
	/*public String getPayerWithPlan(){
		
		String finalStr = "";
		for (Payer payer : getPayerList()) {
			
			finalStr += payer.getName() + " " + payer.getPlanName();
			finalStr += "<br/>";
		}
		
		return finalStr;
	}*/

	public boolean filterByPayer(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if(filterText == null||filterText.equals("")) {
            return true;
        }
         
        if(value == null) {
            return false;
        }
        String lowerFilterText = filterText.toLowerCase();
        Set<Payer>payers = (Set)value;
        
       boolean result = payers.stream().anyMatch( (p) -> {
        	
    	   boolean isOK = false;
    	   if(p.getName().toLowerCase().contains(lowerFilterText))
    		   isOK = true;
    	   if(p.getPlanName().toLowerCase().contains(lowerFilterText))
    		   isOK = true;
        	return isOK;
        });
        return result;
//        return ((Comparable) value).compareTo(Integer.valueOf(filterText)) > 0;
    }
}
