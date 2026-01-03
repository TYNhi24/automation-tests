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
        MockUtils.mockTask(listId1, "Task Â");
        loginPage = new LoginPage(driver, wait);
        loginPage.login("user@gmail.com", "User12");
        
        projectDetailPage = new ProjectDetailPage(driver, wait);
        wait.until(ExpectedConditions.urlContains("projects"));
        projectDetailPage.clickView();         
    }

    @Test(priority = 1, description = "Kiểm tra hiển thị checkbox")
    public void shouldDisplayCheckbox() {
        Assert.assertTrue(projectDetailPage.isCheckBoxDisplayed(),
                "Checkbox should be displayed for each task");
    }

    @Test(priority = 2, description = "Kiểm tra Đánh dấu hoàn thành nhiệm vụ (từ chưa xong -> xong)")
    public void shouldMarkTaskAsCompletedWhenCheckingCheckbox() {
        if (projectDetailPage.isTaskCompleted("Task Â")) {
            projectDetailPage.toggleTaskCheckbox("Task Â");
            Assert.assertFalse(projectDetailPage.isTaskCompleted("Task Â"));
        }
        projectDetailPage.toggleTaskCheckbox("Task Â");
        Assert.assertTrue(projectDetailPage.isTaskCompleted("Task Â"),
                "Task should be completed after checking checkbox");
    }

    @Test(priority = 3, description = "Kiểm tra Bỏ đánh dấu nhiệm vụ (từ xong -> chưa xong)")
    public void shouldUnmarkTaskAsCompletedWhenUncheckingCheckbox() {
        if (!projectDetailPage.isTaskCompleted("Task A")) {
            projectDetailPage.toggleTaskCheckbox("Task A"); // check để thành xong
            Assert.assertTrue(projectDetailPage.isTaskCompleted("Task A"));
        }
        projectDetailPage.toggleTaskCheckbox("Task A");
        Assert.assertFalse(projectDetailPage.isTaskCompleted("Task A"),
                "Task should not be completed after unchecking checkbox"
        );
    }
}
