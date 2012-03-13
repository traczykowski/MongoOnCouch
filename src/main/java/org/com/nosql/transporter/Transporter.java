package org.com.nosql.transporter;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;

public interface Transporter {
	
	void transport(TransporterOptions options) throws UnknownHostException, IOException, ParseException;
}
