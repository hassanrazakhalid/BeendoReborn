package com.Beendo.CustomJSON;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

public class JSONListType extends JSONUserType {

	
	  @Override
	  public Object deepCopy(Object value) throws HibernateException {
	    Object copy = null;
	    if (value != null) {
	      
	      try {
	    	return MAPPER.readValue(MAPPER.writeValueAsString(value), MAPPER.getTypeFactory().constructCollectionType(List.class, classType));
//	        return MAPPER.readValue(MAPPER.writeValueAsString(value), new TypeReference<List<Email>>(){});
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
	        	  obj = MAPPER.readValue(content, MAPPER.getTypeFactory().constructCollectionType(List.class, classType));
	          }
	        } catch (IOException e) {
	          throw new HibernateException("unable to read object from result set", e);
	        }
	      }
//	    }
	    return obj;
	  }
}
