package org.com.nosql.transporter;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.List;

import org.com.nosql.converter.CouchMongoConverter;
import org.com.nosql.converter.DateConvertInfo;
import org.com.nosql.manager.CouchDBManager;
import org.com.nosql.manager.MongoDBManager;

import com.fourspaces.couchdb.Document;
import com.mongodb.DBObject;

public class Couch2MongoTransporter implements Transporter {

	public void transport(TransporterOptions options) throws UnknownHostException, IOException, ParseException{
		CouchDBManager couch = new CouchDBManager(options.couchPort, options.couchDatabase);
		MongoDBManager mongo = new MongoDBManager(options.mongoPort, options.mongoDatabase, options.mongoCollection);
		
		List<Document> doc = couch.getAllDocuments();
		
		DateConvertInfo dcInfo = new DateConvertInfo(options.patternSource, options.patternDest, 
				options.locale, options.dateTimeKeys, options.skipBad);
		
		for(Document d : doc){
			DBObject object = CouchMongoConverter.convert(d, dcInfo);
			mongo.addDocument(object);
		}

	}

}
