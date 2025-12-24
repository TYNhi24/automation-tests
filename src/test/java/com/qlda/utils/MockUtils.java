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

    public static void addMemberToProject(String projectId, String userId, String role) {
        try {
            if (DatabaseUtils.database == null)
                DatabaseUtils.initConnection();
            MongoCollection<Document> projectMembers = DatabaseUtils.database.getCollection("project_members");
            // Kiểm tra đã là member chưa
            Document existing = projectMembers.find(
                    new Document("project_id", new ObjectId(projectId))
                            .append("user_id", new ObjectId(userId)))
                    .first();
            if (existing != null)
                return;

            Document member = new Document("project_id", new ObjectId(projectId))
                    .append("user_id", new ObjectId(userId))
                    .append("role", role != null ? role : "member")
                    .append("joined_at", new java.util.Date());
            projectMembers.insertOne(member);
        } catch (Exception e) {
            System.err.println("Error adding member to project: " + e.getMessage());
        }
    }

    /**
     * Tạo 1 task giả lập cho 1 project
     * 
     * @param projectId id của project
     * @param title     tiêu đề task
     * @return ObjectId dạng hex string của task vừa tạo
     */
    public static String mockList(String projectId, String title) {
        try {
            if (DatabaseUtils.database == null)
                DatabaseUtils.initConnection();
            MongoCollection<Document> tasks = DatabaseUtils.database.getCollection("lists");
            Document task = new Document("project_id", new ObjectId(projectId))
                    .append("title", title)
                    .append("position", 0)
                    .append("created_at", new java.util.Date())
                    .append("updated_at", new java.util.Date());
            tasks.insertOne(task);
            return task.getObjectId("_id").toHexString();
        } catch (Exception e) {
            System.err.println("Error creating mock task: " + e.getMessage());
            return null;
        }
    }

    /**
     * Tạo 1 task giả lập trong 1 list
     * 
     * @param listId id của list
     * @param title  tiêu đề task
     * @return ObjectId dạng hex string của task vừa tạo
     */
    public static String mockTask(String listId, String title) {
        try {
            if (DatabaseUtils.database == null)
                DatabaseUtils.initConnection();
            MongoCollection<Document> tasks = DatabaseUtils.database.getCollection("tasks");
            Document task = new Document("list_id", new ObjectId(listId))
                    .append("assigned_to", new java.util.ArrayList<>())
                    .append("title", title)
                    .append("description", null)
                    .append("status", "todo")
                    .append("start_date", null)
                    .append("due_date", null)
                    .append("priority", "medium")
                    .append("position", 0)
                    .append("is_completed", false)
                    .append("created_at", new java.util.Date())
                    .append("updated_at", new java.util.Date());
            tasks.insertOne(task);
            return task.getObjectId("_id").toHexString();
        } catch (Exception e) {
            System.err.println("Error creating mock task: " + e.getMessage());
            return null;
        }
    }

    /**
     * Tạo group chat room cho project
     * 
     * @param projectId   id của project
     * @param projectName tên project
     * @return ObjectId dạng hex string của group chat room vừa tạo
     */
    public static String mockGroupChatRoom(String projectId, String projectName) {
        try {
            if (DatabaseUtils.database == null)
                DatabaseUtils.initConnection();
            MongoCollection<Document> groupRooms = DatabaseUtils.database.getCollection("group_rooms");
            Document room = new Document("project_id", new ObjectId(projectId))
                    .append("name", projectName + " - Group Chat")
                    .append("type", "group")
                    .append("members", new java.util.ArrayList<>())
                    .append("created_at", new java.util.Date());
            groupRooms.insertOne(room);
            return room.getObjectId("_id").toHexString();
        } catch (Exception e) {
            System.err.println("Error creating mock group chat room: " + e.getMessage());
            return null;
        }
    }

    public static class ProjectWithMembers {
        public String projectId;
        public String userId1;
        public String userId2;
        public String groupRoomId;
    }

    // Thêm hàm này vào MockUtils.java
    public static void addMemberToGroupRoom(String roomId, String userId) {
        try {
            if (DatabaseUtils.database == null)
                DatabaseUtils.initConnection();
            MongoCollection<Document> chatRoomMembers = DatabaseUtils.database.getCollection("chat_room_members");
            // Kiểm tra đã là member chưa
            Document existing = chatRoomMembers.find(
                    new Document("room_id", new ObjectId(roomId))
                            .append("user_id", new ObjectId(userId)))
                    .first();
            if (existing != null)
                return;

            Document member = new Document("room_id", new ObjectId(roomId))
                    .append("user_id", new ObjectId(userId))
                    .append("joined_at", new java.util.Date());
            chatRoomMembers.insertOne(member);
        } catch (Exception e) {
            System.err.println("Error adding member to group chat room: " + e.getMessage());
        }
    }

    public static ProjectWithMembers mockProjectWith2MembersAndGroupChatByEmail(
            String email1,
            String email2,
            String projectName, String projectDesc) {
        ProjectWithMembers result = new ProjectWithMembers();

        // Lấy _id user1 và user2 từ DB
        try {
            if (DatabaseUtils.database == null)
                DatabaseUtils.initConnection();
            MongoCollection<Document> users = DatabaseUtils.database.getCollection(DatabaseUtils.USERS_COLLECTION);

            Document user1 = users.find(new Document("email", email1)).first();
            Document user2 = users.find(new Document("email", email2)).first();
            if (user1 == null || user2 == null)
                throw new RuntimeException("User not found");

            String userId1 = user1.getObjectId("_id").toHexString();
            String userId2 = user2.getObjectId("_id").toHexString();
            result.userId1 = userId1;
            result.userId2 = userId2;

            // Tạo project (user1 là chủ)
            String projectId = mockProject(projectName, projectDesc, userId1);
            result.projectId = projectId;

            // Thêm cả 2 user vào project_members
            addMemberToProject(projectId, userId1, "owner");
            addMemberToProject(projectId, userId2, "member");

            // Tạo group chat room
            String groupRoomId = mockGroupChatRoom(projectId, projectName);
            result.groupRoomId = groupRoomId;

            // Thêm cả 2 user vào chat_room_members
            addMemberToGroupRoom(groupRoomId, userId1);
            addMemberToGroupRoom(groupRoomId, userId2);

            return result;
        } catch (Exception e) {
            System.err.println("Error in mockProjectWith2MembersAndGroupChatByEmail: " + e.getMessage());
            return null;
        }
    }

}
