package com.qlda.tests;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.qlda.core.BaseTest;
import com.qlda.pages.LoginPage;
import com.qlda.pages.ProjectPage;
import com.qlda.pages.TaskPage;
import com.qlda.utils.MockUtils;
import com.qlda.utils.WebDriverConfig;

public class MissionTest extends BaseTest {
    private TaskPage taskPage;
    private LoginPage loginPage;
    private ProjectPage projectPage;
    private String projectId;
    private String listId;

    @BeforeMethod
    @Override
    public void setUp() throws IOException {
        super.setUp();
        String userId = MockUtils.mockUser("test@gmail.com", "Password123", "Valid User", false);
        projectId = MockUtils.mockProject("Test Project", "desc", userId);
        listId = MockUtils.mockList(projectId, "List 1");
        MockUtils.mockTask(listId, "Task 1");
        driver.get(WebDriverConfig.getBaseUrl() + "/login");
        loginPage = new LoginPage(driver, wait);
        projectPage = new ProjectPage(driver, wait);
        taskPage = new TaskPage(driver, wait);
        loginPage.loginUser("test@gmail.com", "Password123");
        projectPage.waitForPageLoad();
        projectPage.goToProject(projectId);
        taskPage.openTaskModal();
    }

    /**
     * Kiểm tra hiển thị nút thêm nhiệm vụ
     */
    @Test
    public void testAddMisionVisible() {
        Assert.assertTrue(taskPage.isMissionButtonVisible(), "Mission button should be visible");
        taskPage.openMissionInput();
        Assert.assertTrue(taskPage.isMissionInputVisible(), "Mission input should be visible");
    }

    /**
     * Kiểm tra thêm nhiệm vụ với mô tả hợp lệ
     */
    @Test
    public void testAddMissionValid() {
        String missionText = "Valid Mission";
        taskPage.openMissionInput();
        taskPage.enterMission(missionText);
        taskPage.addMission();
        Assert.assertTrue(taskPage.isMissionAdded(missionText), "Mission item should be added");
    }

    /**
     * Kiểm tra thêm nhiệm vụ với mô tả không hợp lệ
     */
    @Test
    public void testAddMissionEmptyDescription() {
        taskPage.openMissionInput();
        taskPage.enterMission("");
        taskPage.addMission();
        Assert.assertFalse(taskPage.isMissionAdded(""), "Mission with empty description should not be added");
    }

    /**
     * Kiểm tra thêm nhiệm vụ với mô tả ngắn, dài, ký tự đặc biệt
     */
    @Test
    public void testAddMissionShortDescription() {
        String missionText = "a";
        taskPage.openMissionInput();
        taskPage.enterMission(missionText);
        taskPage.addMission();
        Assert.assertTrue(taskPage.isMissionAdded(missionText), "Mission with short description should be added");
    }

    /**
     * Kiểm tra thêm nhiệm vụ với mô tả dài hơn 255 ký tự
     */
    @Test
    public void testAddMissionLongDescription() {
        String longDesc = "a".repeat(256);
        taskPage.openMissionInput();
        taskPage.enterMission(longDesc);
        taskPage.addMission();
        Assert.assertTrue(taskPage.isMissionAdded(longDesc), "Mission with long description should be added");
    }

    /**
     * Kiểm tra thêm nhiệm vụ với mô tả chứa ký tự đặc biệt
     */
    @Test
    public void testAddMissionSpecialChars() {
        String specialDesc = "!@#$%^&*()_+";
        taskPage.openMissionInput();
        taskPage.enterMission(specialDesc);
        taskPage.addMission();
        Assert.assertTrue(taskPage.isMissionAdded(specialDesc), "Mission with special characters should be added");
    }

    /**
     * Kiểm tra hủy thêm nhiệm vụ
     */
    @Test
    public void testCancelAddMission() {
        String missionText = "Valid Mission";
        taskPage.openMissionInput();
        taskPage.enterMission(missionText);
        taskPage.closeMissionInput();
        Assert.assertFalse(taskPage.isMissionAdded(missionText), "Mission should NOT be added after cancel");
    }

    /**
     * Kiểm tra tiến độ nhiệm vụ sau khi hoàn thành một nhiệm vụ
     */
    @Test
    public void testProgressAfterCompleteOneMission() {
        taskPage.openMissionInput();
        taskPage.enterMission("A");
        taskPage.addMission();
        taskPage.openMissionInput();
        taskPage.enterMission("B");
        taskPage.addMission();

        taskPage.toggleMissionCheckbox("A");

        String progress = taskPage.getMissionProgress();
        Assert.assertTrue(progress.contains("50"), "Progress should be 50% after completing one of two missions");
    }

    /**
     * Kiểm tra tiến độ nhiệm vụ sau khi hoàn thành nhiều nhiệm vụ
     */
    @Test
    public void testProgressAfterCompleteMultipleMissions() {
        for (String name : new String[] { "A", "B", "C", "D" }) {
            taskPage.openMissionInput();
            taskPage.enterMission(name);
            taskPage.addMission();
        }

        taskPage.toggleMissionCheckbox("A");
        taskPage.toggleMissionCheckbox("B");
        taskPage.toggleMissionCheckbox("C");

        String progress = taskPage.getMissionProgress();
        Assert.assertTrue(progress.contains("75"), "Progress should be 75% after completing three of four missions");
    }

    /**
     * Kiểm tra tiến độ nhiệm vụ sau khi hủy hoàn thành một nhiệm vụ
     */
    @Test
    public void testProgressAfterUncheckOneMission() {
        taskPage.openMissionInput();
        taskPage.enterMission("A");
        taskPage.addMission();
        taskPage.openMissionInput();
        taskPage.enterMission("B");
        taskPage.addMission();

        // Đánh dấu hoàn thành cả 2 nhiệm vụ
        taskPage.toggleMissionCheckbox("A");
        taskPage.toggleMissionCheckbox("B");

        // Huỷ đánh dấu hoàn thành nhiệm vụ đầu tiên
        taskPage.toggleMissionCheckbox("A");

        String progress = taskPage.getMissionProgress();
        Assert.assertTrue(progress.contains("50"),
                "Progress should be 50% after unchecking one of two completed missions");
    }

    /**
     * Kiểm tra tiến độ nhiệm vụ sau khi hủy hoàn thành nhiều nhiệm vụ
     */
    @Test
    public void testProgressAfterUncheckMultipleMissions() {
        for (String name : new String[] { "A", "B", "C", "D" }) {
            taskPage.openMissionInput();
            taskPage.enterMission(name);
            taskPage.addMission();
        }

        // Đánh dấu hoàn thành cả 4 nhiệm vụ
        for (String name : new String[] { "A", "B", "C", "D" }) {
            taskPage.toggleMissionCheckbox(name);
        }

        // Huỷ đánh dấu hoàn thành 2 nhiệm vụ
        taskPage.toggleMissionCheckbox("A");
        taskPage.toggleMissionCheckbox("B");

        String progress = taskPage.getMissionProgress();
        Assert.assertTrue(progress.contains("50"),
                "Progress should be 50% after unchecking two of four completed missions");
    }

}
