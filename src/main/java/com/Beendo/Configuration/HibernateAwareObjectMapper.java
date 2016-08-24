package com.Beendo.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

public class HibernateAwareObjectMapper extends ObjectMapper {

	private static final long serialVersionUID = 1628942139655835956L;

	public HibernateAwareObjectMapper() {
        registerModule(new Hibernate4Module());
    }
}
