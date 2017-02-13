package com.Beendo.Entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.FetchProfiles;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "transaction")

@FetchProfiles({
	@FetchProfile(name = "transactionPlanList",fetchOverrides = {
							@FetchProfile.FetchOverride(
							entity = Transaction.class,
							association = "plan",
							mode = FetchMode.JOIN
							)
			}
			
			)
//	,
//	@FetchProfile(name="transactionPayerPlan",fetchOverrides={@FetchProfile.FetchOverride(entity=Payer.class,association="")})
	
})
public class Transaction {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String transactionState;
	private Date transactionDate = new Date();
	private String comments;
	private String parStatus;
	private Integer type;
	
	@ManyToOne(fetch=FetchType.EAGER,cascade={CascadeType.PERSIST,CascadeType.MERGE})
	private CEntitiy entity;
	
	/*@OneToOne(fetch = FetchType.EAGER)
	private Payer payer = new Payer();*/
	

//	@ManyToMany(fetch = FetchType.EAGER, cascade =  {CascadeType.PERSIST,CascadeType.REFRESH, CascadeType.MERGE})
//	@JoinTable(name="provider_transaction_payer",
//	joinColumns=@JoinColumn(name="PROVIDER_TRANSACTION_id"),
//	inverseJoinColumns=@JoinColumn(name="payerList_id")
//	)
//	private Set<Payer> payerList = new HashSet<Payer>();
//	fetch=FetchType.EAGER
	@ManyToOne()
	private Payer payer = new Payer();
	
	@ManyToOne(fetch = FetchType.EAGER, cascade =  {CascadeType.PERSIST,CascadeType.REFRESH, CascadeType.MERGE})
	private Practice practice = new Practice();
	
	@ManyToOne(fetch = FetchType.EAGER, cascade =  {CascadeType.PERSIST,CascadeType.REFRESH, CascadeType.MERGE})
	private Provider provider = new Provider();

	@ManyToOne(fetch = FetchType.EAGER, cascade =  {CascadeType.PERSIST,CascadeType.REFRESH, CascadeType.MERGE})
	private Plan plan =  new Plan();
	
	public Transaction() {
		// TODO Auto-generated constructor stub
	}
	/*public String getPayerWithPlan(){
		
		String finalStr = "";
		for (Payer payer : getPayerList()) {
			
			finalStr += payer.getName() + " " + payer.getPlanName();
			finalStr += "<br/>";
		}
		
		return finalStr;
	}*/

/*	public boolean filterByPayer(Object value, Object filter, Locale locale) {
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
    }*/
}
