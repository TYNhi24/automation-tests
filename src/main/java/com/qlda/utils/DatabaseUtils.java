package com.qlda.utils;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DatabaseUtils {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "project_management";

    // Collection names - Updated based on your database
    static final String USERS_COLLECTION = "users";
    static final String PROJECTS_COLLECTION = "projects";
    static final String TASKS_COLLECTION = "tasks";
    static final String LISTS_COLLECTION = "lists";
    static final String PROJECT_MEMBERS_COLLECTION = "project_members";
    
    private static MongoClient mongoClient;
    static MongoDatabase database;

    public static void initConnection() {
        try {
            mongoClient = MongoClients.create(CONNECTION_STRING);
            database = mongoClient.getDatabase(DATABASE_NAME);
            System.out.println("MongoDB connection initialized successfully!");
        } catch (Exception e) {
            System.err.println("Failed to connect to MongoDB: " + e.getMessage());
        }
    }

    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB connection closed.");
        }
    }

    public static boolean testConnection() {
        try {
            initConnection();
            database.listCollectionNames().first();
            System.out.println("MongoDB connection test successful!");
            return true;
        } catch (Exception e) {
            System.err.println("MongoDB connection test failed: " + e.getMessage());
            return false;
        }
    }

    public static long clearTable(String tableName) {
        try {
            if (database == null) initConnection();
            MongoCollection<Document> collection = database.getCollection(tableName);
            long count = collection.countDocuments();
            collection.deleteMany(new Document());
            System.out.println("✅ Cleared " + tableName.toUpperCase() + " table: " + count + " documents deleted");
            return count;
        } catch (Exception e) {
            System.err.println("❌ Error clearing " + tableName + " table: " + e.getMessage());
            return 0;
        }
    }

    public static void clearAllTables() {
        System.out.println("🧹 Starting to clear ALL tables...");
        long totalDeleted = 0;
        
        totalDeleted += clearTable(USERS_COLLECTION);
        totalDeleted += clearTable(PROJECTS_COLLECTION);
        totalDeleted += clearTable(TASKS_COLLECTION);
        totalDeleted += clearTable(LISTS_COLLECTION);
        totalDeleted += clearTable(PROJECT_MEMBERS_COLLECTION);
        
        System.out.println("🎯 TOTAL CLEARED: " + totalDeleted + " documents from all tables");
    }

}
