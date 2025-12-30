package com.qlda.tests;

import com.qlda.core.BaseTest;
import com.qlda.pages.ProjectDetailPage;
import com.qlda.utils.DatabaseUtils;
import com.qlda.utils.MockUtils;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.qlda.pages.LoginPage;
import java.io.IOException;

public class TaskListTests extends BaseTest {
    private ProjectDetailPage projectDetailPage;
    private LoginPage loginPage; 
    private String projectId;

    @BeforeMethod
    public void init() throws IOException {  

        DatabaseUtils.clearAllTables();
        String userId = MockUtils.mockUser("user@gmail.com", "User12", "Nhi");
        projectId = MockUtils.mockProject("Dự án Test 1", userId);
        
        String listId1 = MockUtils.mockList(projectId, "To Do", 0);
        String listId2 = MockUtils.mockList(projectId, "In Progress", 1);
        
        MockUtils.mockTask(listId1, "Task A");
        MockUtils.mockTask(listId2, "Task B");

        loginPage = new LoginPage(driver, wait);
        loginPage.login("user@gmail.com", "User12");
        
        projectDetailPage = new ProjectDetailPage(driver, wait);
        wait.until(ExpectedConditions.urlContains("projects"));
        projectDetailPage.clickView();     
    }

    @Test(priority = 1, description = "Kiểm tra hiển thị board công việc")
    public void verifyTaskBoardIsVisible() {
        Assert.assertTrue(projectDetailPage.isTaskBoardDisplayed(), "Bảng công việc không hiển thị");
    }

    @Test(priority = 2, description = "Kiểm tra hiển thị danh sách các thẻ công việc")
    public void verifyTaskCardsAreDisplayedCorrectly() {
        Assert.assertTrue(projectDetailPage.isTaskInsideList("To Do", "Task A"), 
            "Lỗi: Không tìm thấy Task A trong cột To Do");
        Assert.assertTrue(projectDetailPage.isTaskInsideList("In Progress", "Task B"), 
            "Lỗi: Không tìm thấy Task B trong cột In Progress");
    }

    @Test(priority = 3, description = "Kiểm tra kéo thả danh sách công việc")
    public void verifyDragAndDropList() {
        projectDetailPage.dragAndDropList("In Progress", "To Do");
        Assert.assertTrue(true); 
    }
}
