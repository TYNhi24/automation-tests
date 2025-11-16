package com.qlda.utils;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;

import com.mongodb.client.MongoCollection;

public class MockUtils {
    public static String mockUser(String email, String password, String name, boolean isAdmin) {
        try {
            if (DatabaseUtils.database == null)
                DatabaseUtils.initConnection();
            MongoCollection<Document> users = DatabaseUtils.database.getCollection(DatabaseUtils.USERS_COLLECTION);

            // Kiểm tra đã tồn tại chưa
            Document existing = users.find(new Document("email", email)).first();
            if (existing != null) {
                return existing.getObjectId("_id").toHexString();
            }

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));

            Document user = new Document("email", email)
                    .append("password", hashedPassword)
                    .append("name", name)
                    .append("role", isAdmin ? "admin" : "user")
                    .append("active", true);

            users.insertOne(user);
            return user.getObjectId("_id").toHexString();
        } catch (Exception e) {
            System.err.println("Error creating test user: " + e.getMessage());
            return null;
        }
    }

    public static String mockProject(String projectName, String description, String createdByUserId) {
        try {
            if (DatabaseUtils.database == null)
                DatabaseUtils.initConnection();
            MongoCollection<Document> projects = DatabaseUtils.database
                    .getCollection(DatabaseUtils.PROJECTS_COLLECTION);

            Document project = new Document("project_name", projectName)
                    .append("description", description)
                    .append("created_by", new ObjectId(createdByUserId))
                    .append("active", true);

            projects.insertOne(project);
            return project.getObjectId("_id").toHexString();
        } catch (Exception e) {
            System.err.println("Error creating test project: " + e.getMessage());
            return null;
        }
    }

}
