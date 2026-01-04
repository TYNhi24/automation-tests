package com.qlda.tests;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.qlda.core.BaseTest;
import com.qlda.pages.LoginPage;
import com.qlda.pages.ProjectDetailPage;
import com.qlda.pages.TaskDetailPage;
import com.qlda.utils.DatabaseUtils;
import com.qlda.utils.MockUtils;

public class TaskCommentsTests extends BaseTest {
    private LoginPage loginPage;
    private ProjectDetailPage projectDetailPage;

    @BeforeMethod
    public void setupAssignmentData() {

        DatabaseUtils.clearAllTables();

        String ownerId = MockUtils.mockUser("nhu@gmail.com", "nhu152", "YNhi");
        String projectId = MockUtils.mockProject("Dự án Giao việc", ownerId);
        String listId = MockUtils.mockList(projectId, "Việc cần làm", 0);
        String taskId = MockUtils.mockTask(listId, "Nhiệm vụ kiểm thử");

        loginPage = new LoginPage(driver, wait);
        loginPage.login("nhu@gmail.com", "nhu152");

        projectDetailPage = new ProjectDetailPage(driver, wait);
        wait.until(ExpectedConditions.urlContains("projects"));
        projectDetailPage.clickView();

        projectDetailPage.openTaskDetails("Nhiệm vụ kiểm thử");
        
    }

    @Test(priority = 1, description = "Kiểm tra sự tồn tại của phần Bình luận")
    public void verifyCommentSectionExists() {
        TaskDetailPage taskDetailPage = new TaskDetailPage(driver, wait);
        boolean isDisplayed = taskDetailPage.isCommentsSectionDisplayed();
        Assert.assertTrue(isDisplayed, "Phần bình luận không hiển thị.");
    }

    @Test(priority = 2, description = "Kiểm tra hiển thị thêm bình luận")
    public void verifyAddComment() {
        String commentText = "Done";
        TaskDetailPage taskDetailPage = new TaskDetailPage(driver, wait);
        taskDetailPage.writeAndSaveComment(commentText);
        Assert.assertTrue(taskDetailPage.isCommentsDisplayed(commentText), "Bình luận không hiển thị.");
    }

    @Test(priority = 3, description = "Kiểm tra hiển thị Ngày&Giờ của bình luận vừa thêm")
    public void verifyCommentTimestamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss d/M/yyyy");
        String expectedTime = formatter.format(new Date());

        TaskDetailPage taskDetailPage = new TaskDetailPage(driver, wait);
        taskDetailPage.writeAndSaveComment("Kiểm tra thời gian");

        String actualTime = taskDetailPage.getLatestCommentTimestamp();
        System.out.println("Thời gian mong đợi (System): " + expectedTime);
        System.out.println("Thời gian thực tế (UI): " + actualTime);

        // cắt bỏ phần giờ/phút/giây
        Assert.assertTrue(actualTime.contains(expectedTime.substring(9)), 
            "Ngày tháng năm không khớp! Mong đợi: " + expectedTime);
        
        // Kiểm tra định dạng có đủ cả giờ:phút:giây và ngày/tháng/năm 
        Assert.assertTrue(actualTime.matches("\\d{2}:\\d{2}:\\d{2} \\d{1,2}/\\d{1,2}/\\d{4}"), 
            "Lỗi: Định dạng thời gian không đúng chuẩn HH:mm:ss d/M/yyyy");
    }

    @Test(priority = 4, description = "Kiểm tra hiển thị nhiều bình luận")
    public void verifyAddComments() {
        TaskDetailPage taskDetailPage = new TaskDetailPage(driver, wait);
        taskDetailPage.writeAndSaveComment("Comment 1");
        taskDetailPage.writeAndSaveComment("Comment 2");
        taskDetailPage.writeAndSaveComment("Comment 3");
        taskDetailPage.writeAndSaveComment("Comment 4");
        taskDetailPage.writeAndSaveComment("Comment 5");
        taskDetailPage.writeAndSaveComment("Comment 6");
        List<String> comments = taskDetailPage.getAllComments();
        System.out.println("Tổng số bình luận hiển thị: " + comments);
        Assert.assertEquals(comments.size(), 6, "Số lượng bình luận không đúng.");
    }

    @Test(priority = 5, description = "Kiểm tra gửi bình luận rỗng/ dấu cách")
    public void verifyEmptyComment() {
        String commentText = "   ";
        TaskDetailPage taskDetailPage = new TaskDetailPage(driver, wait);
        taskDetailPage.writeComment(commentText);
        boolean isButtonVisible = taskDetailPage.isSaveButtonDisplayed();
        Assert.assertFalse(isButtonVisible, "Lỗi: Nút 'Lưu' vẫn hiển thị khi nội dung bình luận chỉ toàn dấu cách!");
    }

    @Test(priority = 6, description = "Kiểm tra bình luận có chứa ký tự đặc biệt và Emoji")
    public void verifyComment() {
        TaskDetailPage taskDetailPage = new TaskDetailPage(driver, wait);
        String specialComment = "@#$%^&*()_+{}|:\"<>?`~-=[]\\;',./ 😋🚀👍😊, 🔥, 🚀";     
        taskDetailPage.writeCommentWithEmoji(specialComment);
        Assert.assertTrue(taskDetailPage.isCommentsDisplayed(specialComment), "Bình luận không hiển thị.");
    }

    @Test(priority = 7, description = "Kiểm tra bảo mật - XSS Injection")
    public void verifyXSSInjection() {
        String commentText = "<script>alert('Hacked')</script>";
        TaskDetailPage taskDetailPage = new TaskDetailPage(driver, wait);
        taskDetailPage.writeAndSaveComment(commentText);
        Assert.assertTrue(taskDetailPage.isCommentsDisplayed(commentText), "Bình luận không hiển thị.");
    }
}
