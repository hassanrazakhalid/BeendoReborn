package com.Beendo.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

	static String timeFormat = "HH:mm:ss";
	
	static public String toString(Date date) {
		
		DateFormat df = new SimpleDateFormat(timeFormat);
		String reportDate = df.format(date);
		return reportDate;
	}
	
	static public Date toDate(String dateStr) {
		
		DateFormat format = new SimpleDateFormat(timeFormat, Locale.ENGLISH);
		try {
			Date date = format.parse(dateStr);
			return date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
}
