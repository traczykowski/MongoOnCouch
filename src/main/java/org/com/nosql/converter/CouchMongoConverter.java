package org.com.nosql.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

import org.bson.BSONObject;
import org.bson.types.BasicBSONList;

import com.fourspaces.couchdb.Document;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Mongo On Couch
 * @author Tomek Traczykowski
 *
 */

public class CouchMongoConverter {
	
	public static DBObject convert(Document document, DateConvertInfo dcInfo) throws ParseException{
		return convert(document.getJSONObject(), dcInfo);
	}
	
	public static DBObject convert(JSONObject document, DateConvertInfo dcInfo) throws ParseException{
		DBObject mongo = new BasicDBObject();
		for(String key : (Collection<String>)document.keySet()){
			Object object = document.get(key);
			if(object instanceof JSONArray){
				BasicDBList array = new BasicDBList();
				Object[] source = ((JSONArray)object).toArray();
				for(Object o : source){
					if(o instanceof JSONObject)
						array.add(convert((JSONObject)o, dcInfo));
					else
						array.add(o);
				}
				
				mongo.put(key, array);
				
			}else if(object instanceof JSONNull){
				mongo.put(key, null);
			}else if(object instanceof JSONObject){
				mongo.put(key, convert((JSONObject)object, dcInfo));
			}else{	//zwykły string
				if(dcInfo.dtkeys.contains(key)){
					mongo.put(key, dcInfo.convert((String)object));
				}else
					mongo.put(key, object);
			}
		}
		
		return mongo;
	}
}
