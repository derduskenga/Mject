package com.harambesa.admin;

import java.util.Date;
import java.util.Calendar;

public class demo{

	public static void main(String[] args){ 
		Calendar cal = Calendar.getInstance(); 
		cal.add(cal.MONTH,-12);
		System.out.println(cal.getTime());
		System.out.println(cal.getTimeInMillis());
		System.out.println(new Date(cal.getTimeInMillis()));
		System.out.println("today:"+new Date());
		System.out.println("today is "+new java.sql.Timestamp(new Date().getTime()));
		System.out.println(new java.sql.Timestamp(cal.getTimeInMillis()));
		// System.out.println(new Date((now.getTime())-(6*30*24*60*60*1000)));
	}
}