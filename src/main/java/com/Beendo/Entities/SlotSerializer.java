package com.Beendo.Entities;

import java.io.IOException;

import com.Beendo.Utils.DateUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

class SlotSerializer extends JsonSerializer<Slot> {

	public SlotSerializer() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void serialize(Slot value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		
		gen.writeStartObject();
		gen.writeStringField("startTime", DateUtil.toString(value.getStartTime()));
		gen.writeStringField("endTime", DateUtil.toString(value.getEndTime()));
		gen.writeEndObject();
	}
	
	
}
