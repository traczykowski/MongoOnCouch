package org.com.nosql;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.com.nosql.converter.CouchMongoConverter;
import org.com.nosql.converter.MongoCouchConverter;
import org.com.nosql.manager.CouchDBManager;
import org.com.nosql.manager.MongoDBManager;
import org.com.nosql.transporter.Couch2MongoTransporter;
import org.com.nosql.transporter.Mongo2CouchTransporter;
import org.com.nosql.transporter.Transporter;
import org.com.nosql.transporter.TransporterOptions;

import com.fourspaces.couchdb.Document;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Mongo On Couch
 * @author Tomek Traczykowski
 *
 */
public class App 
{
	
	
	private static void testMongoCouch(){
		CouchDBManager couch = new CouchDBManager(5984, "somedatabase");
		try{
			MongoDBManager mongo = new MongoDBManager(27017, "test", "test");
			
			DBObject a = new BasicDBObject();
			a.put("string", "Tomek");
			a.put("Boolean", new Boolean(false));
			a.put("Number", new Integer(128));
			
			DBObject b = new BasicDBObject();
			b.put("NestedDocument", "Abrakadabra");
			
			a.put("Nest", b);
			
			a.put("JSONNull", null);
			
			BasicDBList array = new BasicDBList();
			array.add("ArrayElement1");
			array.add(new Boolean(true));
			DBObject c = new BasicDBObject();
			c.put("Simple", "Very");
			array.add(c);
			
			a.put("Array", array);

			
			System.out.println("MONGO");
			System.out.println(a);
			
			Document o = MongoCouchConverter.convert(a, null);
			
			System.out.println("COUCH");
			System.out.println(o);
			
			
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	private static void testCouchMongo(){
		CouchDBManager couch = new CouchDBManager(5984, "somedatabase");
		try{
			MongoDBManager mongo = new MongoDBManager(27017, "test", "test");
			
			Document a = new Document();
			a.put("string", "Tomek");
			a.put("Boolean", new Boolean(false));
			a.put("Number", new Integer(128));
			
			Document b = new Document();
			b.put("NestedDocument", "Abrakadabra");
			
			a.put("Nest", b);
			
			a.put("JSONNull", JSONNull.getInstance());
			
			JSONArray array = new JSONArray();
			array.add("ArrayElement1");
			array.add(new Boolean(true));
			Document c = new Document();
			c.put("Simple", "Very");
			array.add(c);
			
			a.put("Array", array);
			
			System.out.println("COUCH");
			System.out.println(a);
			
			DBObject o = CouchMongoConverter.convert(a, null);
			
			System.out.println("MONGO");
			System.out.println(o);
			
			
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
	}
	
	private static void testCouch(){
		try{
        	CouchDBManager couch = new CouchDBManager(5984, "somedatabase");
        List<Document> doc = couch.getAllDocuments();
        Document a = new Document();
        a.put("tomek", "traczykowski");
        Document b = new Document();
        b.put("Abra", "kadabra");
        a.put("hehe", b);
        couch.addDocument(a);
        for(Document d : doc){
        	//d.get("tomek").toString();
        	
        //	for(Document dd : (Collection<Document>)d.values())
        //		dd.toString();
        	for(String s : (Collection<String>)d.keySet()){
        		System.out.println("key "+ s);
        		if(d.get(s) instanceof JSONObject)
        			System.out.println("Achtung JSON object");
        		
        		System.out.println("value"+ d.get(s));
        	}
        	System.out.println(d.toString());
        	System.out.println(d.values());
        }
        }catch(IOException e){
        	e.printStackTrace();
        }
	}
	
	private static void testMongo(){
		BasicDBObject document = new BasicDBObject();
		document.put("id", 1001);
		document.put("msg", "hello world mongoDB in Java");
		try{
		MongoDBManager mongo = new MongoDBManager(27017, "test", "test");
		mongo.addDocument(document);
		List<DBObject> res = mongo.getAllDocuments();
		for(DBObject object : res){
			System.out.println(object.toString());
		}
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
    public static void main( String[] args )
    {
    	
    	
    	
    	Options options = prepareOptions();
    	try{

	    	TransporterOptions opts = resolveOptions(options, args);
	    	System.out.println(opts.toString());
	    	Transporter transporter;
	    	if(opts.source.compareTo("mongo") == 0)
	    		transporter = new Mongo2CouchTransporter();
	    	else
	    		transporter = new Couch2MongoTransporter();
	    	System.out.println(opts.toString());
	    	transporter.transport(opts);
	    	
	  //      System.out.println( "Couch -> Mongo" );
	  //      testCouchMongo();
	  //      System.out.println( "Mongo -> Couch" );
	  //      testMongoCouch();
        
       /* */
	        
    	}catch(org.apache.commons.cli.ParseException e){
    		usage(options);
    	}catch(Exception e){
    		e.printStackTrace();
    	}

    }
    
    private static Options prepareOptions(){
    	Options options = new Options();
    	Option portMongo = new Option("pm","port-mongo", true, "CouchDB port");
    	Option portCouch = new Option("pc", "port-couch", true, "MongoDB port");
    	Option databaseMongo = new Option("dm","database-mongo", true, "MongoDB database");
    	Option databaseCouch = new Option("dc", "database-couch", true, "CouchDB database");
    	Option collectionMongo = new Option("cm", "collection-mongo", true, "MongoDB collection");
    	Option source = new Option("s", "source", true, "Source database ( mongo | couch )");
    	source.setRequired(true);
    	Option patternSrc = new Option("sp", "pattern-source", true, "Source date pattern");
    	Option patternDest = new Option("dp", "pattern-dest", true, "Dest date pattern");
    	Option locale = new Option("l", "locale", true, "Locale");
    	Option dateTimeKeys = new Option("dtk", "dt-keys", true, "Date / time keys");
    	Option skipBroken = new Option("sb", "skip-bad", false, "Skip unparseable date strings");
    	options.addOption(portMongo);
    	options.addOption(portCouch);
    	options.addOption(databaseCouch);
    	options.addOption(databaseMongo);
    	options.addOption(collectionMongo);
    	options.addOption(source);
    	options.addOption(patternSrc);
    	options.addOption(patternDest);
    	options.addOption(locale);
    	options.addOption(dateTimeKeys);
    	options.addOption(skipBroken);
    	return options;
    	
    }
    
    private static void usage(Options options){
    	HelpFormatter formatter = new HelpFormatter();
    	formatter.printHelp( "mongooncouch", options );
    }
    
    private static TransporterOptions resolveOptions(Options options, String[] args) throws org.apache.commons.cli.ParseException{
	    	CommandLineParser cmdParser = new PosixParser();
	    	CommandLine cmd = cmdParser.parse( options, args);
	    	TransporterOptions opts = new TransporterOptions();
	    	if(!cmd.hasOption("s") | cmd.getOptionValue("s").compareTo("mongo") != 0 && cmd.getOptionValue("s").compareTo("couch") != 0)
	    		throw new org.apache.commons.cli.ParseException("Unknown source");
	    	else
	    		opts.source = cmd.getOptionValue("s");
	    	
	    	if(cmd.hasOption("pm")) opts.mongoPort = Integer.parseInt(cmd.getOptionValue("pm"));
	    	if(cmd.hasOption("pc")) opts.couchPort = Integer.parseInt(cmd.getOptionValue("pc"));
	    	if(cmd.hasOption("dm")) opts.mongoDatabase = cmd.getOptionValue("dm");
	    	if(cmd.hasOption("dc")) opts.couchDatabase = cmd.getOptionValue("dc");
	    	if(cmd.hasOption("cm")) opts.mongoCollection = cmd.getOptionValue("cm");
	    	if(cmd.hasOption("sp")) opts.patternSource = cmd.getOptionValue("sp");
	    	if(cmd.hasOption("dp")) opts.patternDest = cmd.getOptionValue("dp");
	    	if(cmd.hasOption("l")) opts.locale = cmd.getOptionValue("l");
	    	if(cmd.hasOption("sb")) opts.skipBad = true;
	    	if(cmd.hasOption("dtk")) opts.dateTimeKeys = cmd.getOptionValue("dtk").split(",");
	    	
	    	return opts;

    }
}
