package com.Beendo.Entities;

import java.io.IOException;

import com.Beendo.Utils.DateUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class SpecialitySerializer extends JsonSerializer<Speciality>{

	@Override
	public void serialize(Speciality value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		
		gen.writeStartObject();
		
		gen.writeFieldName("boardInfo");
		
		gen.writeStartObject();
		gen.writeNumberField("id", value.getBoardInfo().getId());
		gen.writeStringField("name", value.getBoardInfo().getName());
		
		gen.writeEndObject();
		
		gen.writeFieldName("specialityInfo");
		
		gen.writeStartObject();
		gen.writeNumberField("id", value.getSpecialityInfo().getId());
		gen.writeStringField("name", value.getSpecialityInfo().getName());
		
		gen.writeEndObject();
		
		gen.writeEndObject();
	}

}
