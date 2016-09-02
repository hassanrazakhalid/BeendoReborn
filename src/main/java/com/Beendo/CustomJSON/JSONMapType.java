package com.Beendo.CustomJSON;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

import com.Beendo.Entities.Slot;
import com.fasterxml.jackson.databind.type.MapLikeType;

import com.Beendo.Entities.Slot;

public class JSONMapType extends JSONUserType {

	  @Override
	  public Object deepCopy(Object value) throws HibernateException {
	    Object copy = null;
	    if (value != null &&
	    	value.toString().length() > 2) {
	      
	      try {
	    	  
	    	String str =  MAPPER.writeValueAsString(value);
	        MapLikeType mapType = MAPPER.getTypeFactory().constructMapLikeType(Map.class, String.class, classType);
	        copy = MAPPER.readValue(str, mapType);
//	    	return MAPPER.readValue(MAPPER.writeValueAsString(value), MAPPER.getTypeFactory().constructCollectionType(List.class, classType));
	      } catch (IOException e) {
	        throw new HibernateException("unable to deep copy object", e);
	      }
	    }
	    return copy;
	  }
	  
	  @Override
	  public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException,
	  SQLException {
		  
	    Object obj = null;
	      {
	        try {
	          String content = rs.getString(names[0]);
	          if (content != null) {
	        	  
	        	  MapLikeType mapType = MAPPER.getTypeFactory().constructMapLikeType(Map.class, String.class, classType);
	        	  obj = MAPPER.readValue(content, mapType);
//	        	  obj = MAPPER.readValue(content, MAPPER.getTypeFactory().constructCollectionType(List.class, classType));
	          }
	        } catch (IOException e) {
	          throw new HibernateException("unable to read object from result set", e);
	        }
	      }
//	    }
	    return obj;
	  }
}
