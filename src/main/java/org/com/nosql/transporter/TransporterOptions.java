package org.com.nosql.transporter;

import java.util.Arrays;

public class TransporterOptions {
	public int mongoPort = 27017;
	public int couchPort = 5984;
	public String mongoDatabase = "default";
	public String couchDatabase = "default";
	public String mongoCollection = "default";
	public String source;
	public String locale = "UTF-8";
	public String patternDest = "yy.MM.dd";
	public String patternSource = "yy.MM.dd";
	public String[] dateTimeKeys; 
	public boolean skipBad;
	
	@Override
	public String toString() {
		return "TransporterOptions [mongoPort=" + mongoPort + ", couchPort="
				+ couchPort + ", mongoDatabase=" + mongoDatabase
				+ ", couchDatabase=" + couchDatabase + ", mongoCollection="
				+ mongoCollection + ", source=" + source + ", locale=" + locale
				+ ", patternDest=" + patternDest + ", patternSource="
				+ patternSource + ", dateTimeKeys="
				+ Arrays.toString(dateTimeKeys) + ", skipBad=" + skipBad + "]";
	}
}
