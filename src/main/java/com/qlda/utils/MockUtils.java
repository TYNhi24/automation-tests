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

    // 2. Tạo Dự án 
    public static String mockProject(String projectName, String userId) {
        if (DatabaseUtils.database == null) DatabaseUtils.initConnection();
        MongoCollection<Document> projects = DatabaseUtils.database.getCollection(DatabaseUtils.PROJECTS_COLLECTION);

        Document project = new Document("project_name", projectName)
                .append("created_by", new ObjectId(userId))
                .append("active", true);

        projects.insertOne(project);
        return project.getObjectId("_id").toHexString();
    }

    // 3. Tạo Danh sách công việc 
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

    // 4. Tạo Thẻ công việc
    public static String mockTask(String listId, String taskTitle) {
        if (DatabaseUtils.database == null) DatabaseUtils.initConnection();
        MongoCollection<Document> tasks = DatabaseUtils.database.getCollection(DatabaseUtils.TASKS_COLLECTION);

        Document task = new Document("list_id", new ObjectId(listId))
                .append("title", taskTitle)
                .append("status", "todo")
                .append("is_completed", false)
                .append("members", new java.util.ArrayList<ObjectId>()) // Khởi tạo mảng members trống
                .append("created_at", new Date());

        tasks.insertOne(task);
        return task.getObjectId("_id").toHexString();
    }


    // 5. Chia sẻ dự án cho thành viên (Thêm vào project_members)
    public static void mockProjectMember(String projectId, String userId, String role) {
        if (DatabaseUtils.database == null) DatabaseUtils.initConnection();
        MongoCollection<Document> projectMembers = DatabaseUtils.database.getCollection(DatabaseUtils.PROJECT_MEMBERS_COLLECTION);

        Document member = new Document("project_id", new ObjectId(projectId))
                .append("user_id", new ObjectId(userId))
                .append("role", role) // role: "owner" hoặc "member"
                .append("joined_at", new Date());

        projectMembers.insertOne(member);
    }

    // 6. Gán thành viên vào thẻ công việc 
    public static void assignMemberToTask(String taskId, String userId) {
        if (DatabaseUtils.database == null) DatabaseUtils.initConnection();
        MongoCollection<Document> tasks = DatabaseUtils.database.getCollection(DatabaseUtils.TASKS_COLLECTION);

        tasks.updateOne(
            new Document("_id", new ObjectId(taskId)),
            new Document("$push", new Document("members", new ObjectId(userId)))
        );
    }

}
