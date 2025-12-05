package com.qlda.tests;
import com.qlda.core.BaseTest;
import com.qlda.pages.LoginPage;
import com.qlda.pages.ProjectDetailPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
public class TaskStatusTests extends BaseTest {

    private LoginPage loginPage;
    private ProjectDetailPage projectDetailPage;

    private final String TODO_TASK_NAME  = "Task A"; // chưa xong
    private final String DONE_TASK_NAME  = "Task B"; // đang ở trạng thái đã xong

    @BeforeMethod
    public void init() throws IOException {  
        loginPage = new LoginPage(driver); 
        loginPage.login("user@gmail.com", "User12");
        projectDetailPage = new ProjectDetailPage(driver);
        projectDetailPage.clickView();     
    }

    // TC1: Hiển thị trạng thái nhiệm vụ bằng checkbox
    @Test
    public void shouldDisplayCheckboxForEachTask() {
        Assert.assertTrue(
                projectDetailPage.isTaskCheckboxDisplayed(TODO_TASK_NAME),
                "Checkbox is not displayed for task: " + TODO_TASK_NAME
        );
        Assert.assertTrue(
                projectDetailPage.isTaskCheckboxDisplayed(DONE_TASK_NAME),
                "Checkbox is not displayed for task: " + DONE_TASK_NAME
        );
    }

    // TC2: Đánh dấu hoàn thành nhiệm vụ (từ chưa xong -> xong)
    @Test
    public void shouldMarkTaskAsCompletedWhenCheckingCheckbox() {
        // đảm bảo ban đầu đang ở trạng thái chưa xong
        if (projectDetailPage.isTaskCompleted(TODO_TASK_NAME)) {
            projectDetailPage.toggleTaskCheckbox(TODO_TASK_NAME); // uncheck để về chưa xong
            Assert.assertFalse(projectDetailPage.isTaskCompleted(TODO_TASK_NAME));
        }

        // hành động: tick vào checkbox
        projectDetailPage.toggleTaskCheckbox(TODO_TASK_NAME);

        // mong đợi: trạng thái chuyển sang xong
        Assert.assertTrue(
                projectDetailPage.isTaskCompleted(TODO_TASK_NAME),
                "Task should be completed after checking checkbox"
        );
    }

    // TC3: Bỏ đánh dấu nhiệm vụ (từ xong -> chưa xong)
    @Test
    public void shouldUnmarkTaskAsCompletedWhenUncheckingCheckbox() {
        // đảm bảo ban đầu đang ở trạng thái đã xong
        if (!projectDetailPage.isTaskCompleted(DONE_TASK_NAME)) {
            projectDetailPage.toggleTaskCheckbox(DONE_TASK_NAME); // check để thành xong
            Assert.assertTrue(projectDetailPage.isTaskCompleted(DONE_TASK_NAME));
        }
        // hành động: bỏ tick
        projectDetailPage.toggleTaskCheckbox(DONE_TASK_NAME);
        // mong đợi: trạng thái từ xong -> chưa xong
        Assert.assertFalse(
                projectDetailPage.isTaskCompleted(DONE_TASK_NAME),
                "Task should not be completed after unchecking checkbox"
        );
    }
}
