package com.Beendo.Entities;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
//@JsonDeserialize(using=EmailsListDeserializer.class)
public class Email {

	private String email;
	
	public Email() {
		// TODO Auto-generated constructor stub
	}
//	@JsonCreator
//	public Email(Map<String,Object> props)
//	{
//		email = (String) props.get("email");
//
//	}
}



/*public class LabelsUserType extends JacksonListUserType {
    @Override
    public Class returnedClass() {
        return Label.class;
    }
}*/
