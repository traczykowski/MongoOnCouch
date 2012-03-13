package org.com.nosql.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class DateConvertInfo {

	public DateConvertInfo(String sourcePattern, String destPattern, String locale, String[] dtkeys, boolean skipBad) {
		this.dest = new SimpleDateFormat(destPattern, new Locale(locale));
		this.source = new SimpleDateFormat(sourcePattern, new Locale(locale));
		this.dtkeys = new ArrayList<String>(Arrays.asList(dtkeys));
		this.skipBad = skipBad;
	}
	
	public String convert(String sourceString) throws ParseException{
		Date date = null;
		try{
			 date = source.parse(sourceString);
			 return dest.format(date);
			
		}catch(ParseException e){
			if(!skipBad)
				throw e;
		}
		return sourceString;
		
		
		
	}
	
	private SimpleDateFormat source;
	private SimpleDateFormat dest;
	public ArrayList<String> dtkeys;
	private boolean skipBad;
}
