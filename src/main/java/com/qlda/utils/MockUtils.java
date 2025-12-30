package com.qlda.utils;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Date;

import com.mongodb.client.MongoCollection;

public class MockUtils {
    // 1. Tạo User để đăng nhập
    public static String mockUser(String email, String password, String name) {
        if (DatabaseUtils.database == null) DatabaseUtils.initConnection();
        MongoCollection<Document> users = DatabaseUtils.database.getCollection(DatabaseUtils.USERS_COLLECTION);

        // Kiểm tra nếu đã tồn tại thì lấy ID, không thì tạo mới
        Document existing = users.find(new Document("email", email)).first();
        if (existing != null) return existing.getObjectId("_id").toHexString();

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        Document user = new Document("email", email)
                .append("password", hashedPassword)
                .append("name", name)
                .append("active", true);

        users.insertOne(user);
        return user.getObjectId("_id").toHexString();
    }

    // 2. Tạo Dự án (Tiền đề cho TC_01)
    public static String mockProject(String projectName, String userId) {
        if (DatabaseUtils.database == null) DatabaseUtils.initConnection();
        MongoCollection<Document> projects = DatabaseUtils.database.getCollection(DatabaseUtils.PROJECTS_COLLECTION);

        Document project = new Document("project_name", projectName)
                .append("created_by", new ObjectId(userId))
                .append("active", true);

        projects.insertOne(project);
        return project.getObjectId("_id").toHexString();
    }

    // 3. Tạo Danh sách công việc (Tiền đề cho TC_01, TC_03)
    public static String mockList(String projectId, String title, int position) {
        if (DatabaseUtils.database == null) DatabaseUtils.initConnection();
        MongoCollection<Document> lists = DatabaseUtils.database.getCollection(DatabaseUtils.LISTS_COLLECTION);

        Document list = new Document("project_id", new ObjectId(projectId))
                .append("title", title)
                .append("position", position)
                .append("created_at", new Date());

        lists.insertOne(list);
        return list.getObjectId("_id").toHexString();
    }

    // 4. Tạo Thẻ công việc (Tiền đề cho TC_02)
    public static void mockTask(String listId, String taskTitle) {
        if (DatabaseUtils.database == null) DatabaseUtils.initConnection();
        MongoCollection<Document> tasks = DatabaseUtils.database.getCollection(DatabaseUtils.TASKS_COLLECTION);

        Document task = new Document("list_id", new ObjectId(listId))
                .append("title", taskTitle)
                .append("status", "todo")
                .append("is_completed", false)
                .append("created_at", new Date());

        tasks.insertOne(task);
    }
}
