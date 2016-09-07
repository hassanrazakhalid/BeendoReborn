package com.Beendo.Entities;

import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.Beendo.CustomJSON.JSONCustomDeserializer;
import com.Beendo.CustomJSON.JSONListType;
import com.Beendo.CustomJSON.JSONMapType;
import com.Beendo.CustomJSON.JSONUserType;

//@TypeDefs({
//@TypeDef(name = "json", typeClass = JsonStringType.class),
//@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
//})

@TypeDefs( 
		{
			@TypeDef( name= JSONUserType.JSON_Normal, typeClass = JSONUserType.class),
			@TypeDef( name= JSONUserType.JSON_List, typeClass = JSONListType.class),
			@TypeDef( name= JSONUserType.JSON_Map, typeClass = JSONMapType.class),
			@TypeDef( name= JSONUserType.JSON_CustomDeserializer, typeClass = JSONCustomDeserializer.class),
			
			
			}
		
		)

@MappedSuperclass
public class BaseEntity {

}
