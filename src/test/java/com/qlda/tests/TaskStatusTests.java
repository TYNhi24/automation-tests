package com.qlda.tests;
import com.qlda.core.BaseTest;
import com.qlda.pages.LoginPage;
import com.qlda.pages.ProjectDetailPage;
import com.qlda.utils.DatabaseUtils;
import com.qlda.utils.MockUtils;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
public class TaskStatusTests extends BaseTest {

    private LoginPage loginPage;
    private ProjectDetailPage projectDetailPage;
    private String projectId;

    @BeforeMethod
    public void init() throws IOException {  
        DatabaseUtils.clearAllTables();
        String userId = MockUtils.mockUser("user@gmail.com", "User12", "Nhi");
        projectId = MockUtils.mockProject("Dự án Test 1", userId);
        
        String listId1 = MockUtils.mockList(projectId, "To Do", 0);

        MockUtils.mockTask(listId1, "Task A");

        loginPage = new LoginPage(driver, wait);
        loginPage.login("user@gmail.com", "User12");
        
        projectDetailPage = new ProjectDetailPage(driver, wait);
        wait.until(ExpectedConditions.urlContains("projects"));
        projectDetailPage.clickView();         
    }

    @Test(priority = 1, description = "Kiểm tra hiển thị checkbox")
    public void shouldDisplayCheckboxForEachTask() {
        Assert.assertTrue(projectDetailPage.isCheckBoxDisplayed(),
                "Checkbox should be displayed for each task");
    }

    @Test(priority = 1, description = "Kiểm tra Đánh dấu hoàn thành nhiệm vụ (từ chưa xong -> xong)")
    public void shouldMarkTaskAsCompletedWhenCheckingCheckbox() {
        // đảm bảo ban đầu đang ở trạng thái chưa xong
        if (projectDetailPage.isTaskCompleted("Task A")) {
            projectDetailPage.toggleTaskCheckbox("Task A"); // uncheck để về chưa xong
            Assert.assertFalse(projectDetailPage.isTaskCompleted("Task A"));
        }

        // hành động: tick vào checkbox
        projectDetailPage.toggleTaskCheckbox("Task A");

        // mong đợi: trạng thái chuyển sang xong
        Assert.assertTrue(
                projectDetailPage.isTaskCompleted("Task A"),
                "Task should be completed after checking checkbox"
        );
    }

    @Test(priority = 1, description = "Kiểm tra Bỏ đánh dấu nhiệm vụ (từ xong -> chưa xong)")
    public void shouldUnmarkTaskAsCompletedWhenUncheckingCheckbox() {
        // đảm bảo ban đầu đang ở trạng thái đã xong
        if (!projectDetailPage.isTaskCompleted("Task A")) {
            projectDetailPage.toggleTaskCheckbox("Task A"); // check để thành xong
            Assert.assertTrue(projectDetailPage.isTaskCompleted("Task A"));
        }
        // hành động: bỏ tick
        projectDetailPage.toggleTaskCheckbox("Task A");
        // mong đợi: trạng thái từ xong -> chưa xong
        Assert.assertFalse(
                projectDetailPage.isTaskCompleted("Task A"),
                "Task should not be completed after unchecking checkbox"
        );
    }
}
