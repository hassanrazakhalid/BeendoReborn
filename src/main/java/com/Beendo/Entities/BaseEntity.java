package com.Beendo.Entities;

import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.Beendo.Configuration.JSONUserType;

//@TypeDefs({
//@TypeDef(name = "json", typeClass = JsonStringType.class),
//@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
//})

@TypeDefs( {@TypeDef( name= "JsonStringType", typeClass = JSONUserType.class)})

@MappedSuperclass
public class BaseEntity {

}
