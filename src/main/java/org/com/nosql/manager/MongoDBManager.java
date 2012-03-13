package org.com.nosql.manager;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class MongoDBManager {
	
	private  DBCollection coll;
	
	public MongoDBManager(int port, String databaseName, String collection)  throws UnknownHostException{
		Mongo m = new Mongo( "localhost" , port);
		DB db = m.getDB(databaseName);	//jeżeli baza nie istnieje to zostanie utworzona
		coll = db.getCollection(collection);	//jeżeli kolekcja nie istnieje to zostanie utworzona
	}
	
	public List<DBObject> getAllDocuments() throws UnknownHostException{
		DBCursor cur = coll.find();
		List<DBObject> result = new ArrayList<DBObject>();
		
        while(cur.hasNext())
            result.add(cur.next());
        
        return result;

	}
	
	public void addDocument(DBObject document) throws UnknownHostException{
		coll.insert(document);
		
	}
	
	public void removeDocument(DBObject document) throws UnknownHostException{
		coll.remove(document);
	}

}
