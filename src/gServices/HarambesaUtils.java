package com.harambesa.gServices; 

import java.io.PrintWriter;
import java.io.StringWriter;

public class HarambesaUtils{

		/*Method  for converting  stack trace from an exception*/
		
		public static String getStackTrace(final Throwable throwable) {
					final StringWriter sw = new StringWriter();
					final PrintWriter pw = new PrintWriter(sw, true);
					throwable.printStackTrace(pw);
					return sw.getBuffer().toString();
			 }

}
 
