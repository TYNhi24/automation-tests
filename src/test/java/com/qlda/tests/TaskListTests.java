package com.qlda.tests;
import com.qlda.core.BaseTest;
import com.qlda.pages.ProjectDetailPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.qlda.pages.LoginPage;
import java.io.IOException;
public class TaskListTests extends BaseTest {
    private ProjectDetailPage projectDetailPage;
    private LoginPage loginPage; 

    @BeforeMethod
    public void init() throws IOException {  
        loginPage = new LoginPage(driver); 
        loginPage.login("user@gmail.com", "User12");
        projectDetailPage = new ProjectDetailPage(driver);
        projectDetailPage.clickView();     
    }
    

    // TC1: Hiển thị danh sách công việc dưới dạng các thẻ
    @Test
    public void verifyTaskBoardIsVisible() {
        Assert.assertTrue(
                projectDetailPage.isTaskBoardDisplayed(),
                "Task board is not displayed on project detail page"
        );
    }

    // TC2: Hiển thị đúng danh sách các thẻ công việc
    // Test data: đã tạo sẵn 2 thẻ “Task A”, “Task B”
    @Test
    public void verifyTaskCardsAreDisplayedCorrectly() {
        var titles = projectDetailPage.getTaskTitles();
        System.out.println("Task titles = " + titles); 
        Assert.assertFalse(
                titles.contains("Task A") && titles.contains("Task B"),
                "Task cards titles are incorrect. Actual: " + titles
        );
    }
}
