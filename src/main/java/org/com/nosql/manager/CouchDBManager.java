package org.com.nosql.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.Session;
import com.fourspaces.couchdb.ViewResults;

public class CouchDBManager {
	
	private Database db; 
	
	public CouchDBManager(int port, String databaseName){
		Session session = new Session("localhost",port);
		db = session.getDatabase(databaseName);
		if(db == null)
			db = session.createDatabase(databaseName);

	}
	
	public List<Document> getAllDocuments() throws IOException{ 
		
		ViewResults result = db.getAllDocuments();
		List<Document> documents = new ArrayList<Document>();
		for(Document d: result.getResults()){
			documents.add(db.getDocument(d.getId()));
		}
		return documents;
	}
	
	public void addDocument(Document document) throws IOException{
		db.saveDocument(document);
	}
	
	public void removeDocument(Document document) throws IOException{
		db.deleteDocument(document);
	}
	

}
