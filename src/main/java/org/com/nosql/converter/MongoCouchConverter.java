package org.com.nosql.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

import com.fourspaces.couchdb.Document;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Mongo On Couch
 * @author Tomek Traczykowski
 *
 */
public class MongoCouchConverter {
	
	public static Document convert(DBObject document, DateConvertInfo dcInfo) throws ParseException{
		JSONObject couch = new JSONObject();
		
		for(String key : (Collection<String>)document.keySet()){
			Object object = document.get(key);
			if(object instanceof BasicDBList){
				JSONArray array = new JSONArray();
				Object[] source = ((BasicDBList)object).toArray();
				for(Object o : source){
					if(o instanceof JSONObject)
						array.add(convert((DBObject)o, dcInfo));
					else
						array.add(o);
				}
				
				couch.put(key, array);
				
			}else if(object == null){
				couch.put(key, JSONNull.getInstance());
			}else if(object instanceof DBObject){
				couch.put(key, convert((DBObject)object, dcInfo));
			}else{	//zwykły string
				if(dcInfo.dtkeys.contains(key)){
					couch.put(key, dcInfo.convert((String)object));
				}else
					couch.put(key, object);
			}
		}
		
		return new Document(couch);
	}

}
