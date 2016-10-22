package com.Beendo.Entities;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Transaction.class)
public class Transaction_ {

	public static volatile SingularAttribute<Transaction, String> transactionDate;
	public static volatile SingularAttribute<Transaction, Provider> provider;
}
