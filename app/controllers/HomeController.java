package controllers;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import com.typesafe.config.Config;

import play.Environment;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class HomeController extends Controller {
 
	private final MongoDatabase database;
	private MongoCollection<Document> collection;

	@Inject
    public HomeController(Environment environment, Config config) {
        String uri = config.getString("mongodb.uri");
        MongoClient mongoClient = MongoClients.create(uri);
        this.database = mongoClient.getDatabase("MyDB");
        this.collection = database.getCollection("testcollection");
	}
    public Result index() {
        return ok("Connected to MongoDB!");
    }
    
    public Result getAllDocuments() {
        MongoCursor<Document> cursor = collection.find().iterator();
        ObjectNode result = Json.newObject();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            result.put(document.getObjectId("_id").toString(), Json.toJson(document));
        }
        return ok(result);
    }
    
    public Result createDocument() {
        Document newDocument = new Document("name", "John Doe")
                .append("age", 30)
                .append("email", "john.doe@example.com");
        collection.insertOne(newDocument);
        Document newDocument1 = new Document("name", "Karthik")
                .append("age", 35)
                .append("email", "karthik@example.com");
        collection.insertOne(newDocument1);
        return ok("Document created: " + newDocument.getObjectId("_id"));
        
    }
    
    public Result updateDocument(String id) {
        Document query = new Document("_id", new org.bson.types.ObjectId(id));
        Document update = new Document("$set", new Document("age", 32));
        collection.updateOne(query, update);
        return ok("Document updated: " + id);
    }
    
    public Result deleteDocument(String id) {
        Document query = new Document("_id", new org.bson.types.ObjectId(id));
        collection.deleteOne(query);
        return ok("Document deleted: " + id);
    }

    }
  
