package com.Beendo.Dto;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.Beendo.Entities.Slot;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SlotCell {

	private String day;
	private Slot slot;
	
	
}
