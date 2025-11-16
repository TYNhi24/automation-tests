package com.qlda.tests;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.qlda.core.BaseTest;
import com.qlda.pages.LoginPage;
import com.qlda.pages.ProjectPage;
import com.qlda.utils.MockUtils;
import com.qlda.utils.WebDriverConfig;

public class ProjectDeleteTest extends BaseTest {
    private ProjectPage projectPage;
    private LoginPage loginPage;
    private String userId;

    @BeforeMethod
    @Override
    public void setUp() throws IOException {
        userId = MockUtils.mockUser("test@gmail.com", "Password123", "Valid User", false);
        MockUtils.mockProject("Dự án A", "Mô tả dự án A", userId);
        super.setUp();
        driver.get(WebDriverConfig.getBaseUrl() + "/login");
        loginPage = new LoginPage(driver, wait);
        projectPage = new ProjectPage(driver, wait);
        loginPage.loginUser("test@gmail.com", "Password123");
        projectPage.waitForPageLoad();
    }

    /**
     * Test Case: Xóa dự án thành công
     * Expected: Dự án không còn xuất hiện trong danh sách
     */
    @Test
    public void testDeleteProjectSuccess() {
        Assert.assertTrue(projectPage.getProjectNames().contains("Dự án A"), "Project should exist before delete");
        projectPage.clickDeleteButton();
        projectPage.clickConfirmDeleteButton();
        projectPage.waitForPageLoad();
        Assert.assertFalse(projectPage.getProjectNames().contains("Dự án A"), "Project should be deleted and not visible in list");
    }

    /**
     * Test Case: Hủy thao tác xóa dự án
     * Expected: Dự án vẫn còn trong danh sách sau khi hủy
     */
    @Test
    public void testCancelDeleteProject() {
        Assert.assertTrue(projectPage.getProjectNames().contains("Dự án A"), "Project should exist before delete");
        projectPage.clickDeleteButton();
        projectPage.clickCancelButton();
        projectPage.waitForPageLoad();
        Assert.assertTrue(projectPage.getProjectNames().contains("Dự án A"), "Project should still exist after canceling delete");
    }
}