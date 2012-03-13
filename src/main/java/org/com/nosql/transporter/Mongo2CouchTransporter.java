package org.com.nosql.transporter;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.List;

import org.com.nosql.converter.DateConvertInfo;
import org.com.nosql.converter.MongoCouchConverter;
import org.com.nosql.manager.CouchDBManager;
import org.com.nosql.manager.MongoDBManager;

import com.fourspaces.couchdb.Document;
import com.mongodb.DBObject;

public class Mongo2CouchTransporter implements Transporter{
	
	public void transport(TransporterOptions options) throws UnknownHostException, IOException, ParseException{
		CouchDBManager couch = new CouchDBManager(options.couchPort, options.couchDatabase);
		MongoDBManager mongo = new MongoDBManager(options.mongoPort, options.mongoDatabase, options.mongoCollection);
		
		DateConvertInfo dcInfo = new DateConvertInfo(options.patternSource, options.patternDest, 
				options.locale, options.dateTimeKeys, options.skipBad);
		
		List<DBObject> objects = mongo.getAllDocuments();
		for(DBObject object : objects){
		//	System.out.println("!MONGO" + object.toString());
			Document document = MongoCouchConverter.convert(object, dcInfo);
		//	System.out.println("!COUCH!" + document.toString());
			couch.addDocument(document);
		}
	}
}
