package com.qlda.tests;

import java.io.IOException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.qlda.core.BaseTest;
import com.qlda.pages.LoginPage;
import com.qlda.pages.ProjectPage;
import com.qlda.utils.MockUtils;
import com.qlda.utils.WebDriverConfig;

public class ProjectListTest extends BaseTest {
    private ProjectPage projectPage;
    private LoginPage loginPage;
    private String userId;

    @BeforeMethod
    @Override
    public void setUp() throws IOException {
        userId = MockUtils.mockUser("test@gmail.com", "Password123", "Valid User", false);
        super.setUp();
        driver.get(WebDriverConfig.getBaseUrl() + "/login");
        loginPage = new LoginPage(driver, wait);
        projectPage = new ProjectPage(driver, wait);
        loginPage.loginUser("test@gmail.com", "Password123");
    }

    @Test
    public void testProjectListNotEmpty() {
        MockUtils.mockProject("Test Project", "Description for test project", userId);
        projectPage.waitForPageLoad();
        assert !projectPage.getProjectNames().isEmpty() : "Project list should not be empty after login";
    }

    @Test
    public void testProjectListEmpty() {
        projectPage.waitForPageLoad();
        assert projectPage.getProjectNames().isEmpty() : "Project list should be empty for non-existing project search";
    }

}
