package com.Beendo.Entities;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonSerialize(using = SlotSerializer.class)
public class Slot {

	private Date startTime;
	private Date endTime;
	
	public Slot() {
		// TODO Auto-generated constructor stub
	}
	
	@JsonCreator
	public Slot(Map<String,String> props)
	{
		String  startTime = (String) props.get("startTime");
		String  endTime = (String) props.get("endTime");

		String formatString = "HH:mm:ss";
		DateFormat format = new SimpleDateFormat(formatString, Locale.ENGLISH);
		try {
			this.startTime = format.parse(startTime);
			this.endTime = format.parse(endTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("");
	}
	
//	class SlotSerializer extends JsonSerializer<Slot> {
//
//		public SlotSerializer() {
//			// TODO Auto-generated constructor stub
//		}
//		
//		@Override
//		public void serialize(Slot value, JsonGenerator gen, SerializerProvider serializers)
//				throws IOException, JsonProcessingException {
//			
//			ObjectMapper mapper = new ObjectMapper();
//		 	String s = mapper.writeValueAsString(value);
////		 	gen.writes
//		 	gen.writeObject(value);
//		}
//		
//		
//	}
}
