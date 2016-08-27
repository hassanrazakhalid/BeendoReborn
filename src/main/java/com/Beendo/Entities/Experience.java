package com.Beendo.Entities;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Experience {

	private String institutionName;
	private Date startDate;
	private Date endDate;
	private String type;
}
