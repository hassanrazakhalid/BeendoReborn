package com.Beendo.CustomJSON;

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import com.Beendo.Entities.Email;
import com.Beendo.Entities.FaxNumber;
import com.Beendo.Entities.PhoneNumber;
import com.Beendo.Entities.Speciality;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
* Hibernate {@link UserType} implementation to handle JSON objects
*
* @see https
*      ://docs.jboss.org/hibernate/orm/4.1/javadocs/org/hibernate/usertype/
*      UserType.html
*/


public class JSONUserType implements UserType, ParameterizedType, Serializable {

	public static final String JSON_List = "JSONListType";
	public static final String JSON_Normal = "JSONUserType";
	public static final String JSON_Map = "JSONMapType";
	public static final String JSON_CustomDeserializer = "JSONCustomDeserializer";
	
	
  private static final long serialVersionUID = 1L;
  
  public static final ObjectMapper MAPPER = new ObjectMapper();
  
  public static final String CLASS_TYPE = "classType";
  private static final String TYPE = "type";
  
  private static final int[] SQL_TYPES = new int[] { Types.LONGVARCHAR, Types.CLOB, Types.BLOB, Types.JAVA_OBJECT, Types.OTHER };
  
//  private TypeReference classType;
  public Class<?> classType;
//  private int sqlType = Types.LONGVARCHAR; // before any guessing
  
//  private T getClass(Class<T extends Object> val){
//	  
//	  
//	  retutn T;
//  }
 public JSONUserType() {
	
	 MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true);
	 MAPPER.setSerializationInclusion(Include.NON_NULL);
	 
//	 SimpleModule mod = new SimpleModule("MyModule");
//	 mod.addSerializer(new CustomSerializer(SWEngineer.class));
//	 mod.addDeserializer(Speciality.class, new FieldDeserializer(Speciality.class));
//	 mapper.registerModule(mod);
}
  
  @Override
  public void setParameterValues(Properties params) {
    String classTypeName = params.getProperty(CLASS_TYPE);
    String type = params.getProperty(TYPE);
//    this.classType = Email.class;
//    if ( type == null )
    {
    	
        try {
//          
        classType = ReflectHelper.classForName(classTypeName, this.getClass());
        
        } catch (ClassNotFoundException cnfe) {
          throw new HibernateException("classType not found", cnfe);
        }
    }
/*    else
    {
    	  if (classTypeName.contains("Email")) {	
    	    	 this.classType = new TypeReference<List<Email>>(){}.getClass();
    	    }
    	    else if (classTypeName.contains("FaxNumber")) {
    	    	
    	   	 this.classType = new TypeReference<List<FaxNumber>>(){}.getClass();
    	   }
    	    else if (classTypeName.contains("PhoneNumber")) {
    	    	
    	      	 this.classType = new TypeReference<List<PhoneNumber>>(){}.getClass();
    	      }
    	    else if (classTypeName.contains("Speciality")) {
    	    	
   	      	 this.classType = new TypeReference<List<Speciality>>(){}.getClass();
   	      }
    	  
    }*/
//    String type = params.getProperty(TYPE);
    if (type != null) {
//      this.sqlType = Integer.decode(type).intValue();
    }
    
  }
  
  @Override
  public Object assemble(Serializable cached, Object owner) throws HibernateException {
    return this.deepCopy(cached);
  }
  
  @Override
  public Object deepCopy(Object value) throws HibernateException {
    Object copy = null;
    if (value != null) {
      
      try {
    	  
    	  copy = MAPPER.readValue(MAPPER.writeValueAsString(value), classType);
//    	copy = MAPPER.readValue(MAPPER.writeValueAsString(value), MAPPER.getTypeFactory().constructCollectionType(List.class, classType));
//        return MAPPER.readValue(MAPPER.writeValueAsString(value), new TypeReference<List<Email>>(){});
      } catch (IOException e) {
        throw new HibernateException("unable to deep copy object", e);
      }
    }
    return copy;
  }
  
  @Override
  public Serializable disassemble(Object value) throws HibernateException {
    try {
      return MAPPER.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new HibernateException("unable to disassemble object", e);
    }
  }
  
  @Override
  public boolean equals(Object x, Object y) throws HibernateException {
	  
    return Objects.equals(x, y);
  }
  
  @Override
  public int hashCode(Object x) throws HibernateException {
    return Objects.hashCode(x);
  }
  
  @Override
  public boolean isMutable() {
    return true;
  }
  
  @Override
  public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException,
  SQLException {
	  
    Object obj = null;
//    if (!rs.wasNull()) {
//      if (this.sqlType == Types.CLOB || this.sqlType == Types.BLOB) {
//        byte[] bytes = rs.getBytes(names[0]);
//        if (bytes != null) {
//          try {
//            obj = MAPPER.readValue(bytes, new TypeReference<List<Email>>(){});
//          } catch (IOException e) {
//            throw new HibernateException("unable to read object from result set", e);
//          }
//        }
//      } 
//      else
      {
        try {
          String content = rs.getString(names[0]);
          if (content != null) {
        	  obj = MAPPER.readValue(content, classType);
          }
        } catch (IOException e) {
          throw new HibernateException("unable to read object from result set", e);
        }
      }
//    }
    return obj;
  }
  
  @Override
  public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException,
  SQLException {
    if (value == null) {
    	
    	st.setNull(index, Types.JAVA_OBJECT);
    } else {
      
//      if (this.sqlType == Types.CLOB || this.sqlType == Types.BLOB) {
//        try {
//          st.setBytes(index, MAPPER.writeValueAsBytes(value));
//        } catch (JsonProcessingException e) {
//          throw new HibernateException("unable to set object to result set", e);
//        }
//      } 
//      else
      {
        try {
          st.setString(index, MAPPER.writeValueAsString(value));
        } catch (JsonProcessingException e) {
          throw new HibernateException("unable to set object to result set", e);
        }
      }
    }
  }
  
  @Override
  public Object replace(Object original, Object target, Object owner) throws HibernateException {
    return this.deepCopy(original);
  }
  
  @Override
  public Class<?> returnedClass() {
	  
//	 return this.classType.getClass();
    return this.classType;
  }
  
  @Override
  public int[] sqlTypes() {
	  
	  return new int[] { Types.JAVA_OBJECT };
//    return SQL_TYPES;
  }
}