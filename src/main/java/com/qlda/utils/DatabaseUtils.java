package com.qlda.utils;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DatabaseUtils {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "quanlyduan";

    public static final String USERS_COLLECTION = "users";
    public static final String PROJECTS_COLLECTION = "projects";
    public static final String TASKS_COLLECTION = "tasks";
    public static final String LISTS_COLLECTION = "lists";

    private static MongoClient mongoClient;
    public static MongoDatabase database;

    public static void initConnection() {
        if (mongoClient == null) {
            try {
                mongoClient = MongoClients.create(CONNECTION_STRING);
                database = mongoClient.getDatabase(DATABASE_NAME);
                System.out.println("Kết nối MongoDB thành công!");
            } catch (Exception e) {
                System.err.println("Lỗi kết nối MongoDB: " + e.getMessage());
            }
        }
    }

    // Hàm xóa dữ liệu trong một bảng
    public static long clearTable(String tableName) {
        try {
            if (database == null) initConnection();
            MongoCollection<Document> collection = database.getCollection(tableName);
            
            long count = collection.countDocuments();
            collection.deleteMany(new Document()); // Xóa toàn bộ dữ liệu trong collection
            
            System.out.println("   + Đã dọn dẹp bảng: " + tableName.toUpperCase() + " (" + count + " bản ghi)");
            return count;
        } catch (Exception e) {
            System.err.println("   + ❌ Lỗi khi dọn dẹp bảng " + tableName + ": " + e.getMessage());
            return 0;
        }
    }

    // 3. Hàm xóa TẤT CẢ các bảng
    public static void clearAllTables() {
        System.out.println("🧹 Bắt đầu dọn dẹp toàn bộ cơ sở dữ liệu...");
        long totalDeleted = 0;
        
        totalDeleted += clearTable(USERS_COLLECTION);
        totalDeleted += clearTable(PROJECTS_COLLECTION);
        totalDeleted += clearTable(TASKS_COLLECTION);
        totalDeleted += clearTable(LISTS_COLLECTION);
        
        System.out.println("🎯 TỔNG CỘNG: Đã dọn sạch " + totalDeleted + " bản ghi.");
    }

    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Đã đóng kết nối MongoDB.");
        }
    }
}
