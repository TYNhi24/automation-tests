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

    /**
     * Test Case: Kiểm tra hiển thị danh sách dự án có dữ liệu
     * Expected: Hiển thị danh sách đầy đủ các dự án
     */
    @Test
    public void testProjectListNotEmpty() {
        MockUtils.mockProject("Dự án A", "Mô tả dự án A", userId);
        projectPage.waitForPageLoad();
        assert !projectPage.getProjectNames().isEmpty() : "Project list should not be empty after login";
    }

    /**
     * Test Case: Kiểm tra hiển thị danh sách dự án khi không có dự án nào
     * Expected: Hiển thị thông báo “Chưa có dự án nào”
     */
    @Test
    public void testProjectListEmpty() {
        projectPage.waitForPageLoad();
        assert projectPage.getProjectNames().isEmpty() : "Project list should be empty for non-existing project search";
    }

    /**
     * Test Case: Kiểm tra tìm kiếm dự án với từ khóa tồn tại
     * Expected: Hiển thị danh sách dự án đúng với từ khóa
     */
    @Test
    public void testSearchProjectFound() {
        MockUtils.mockProject("Dự án A", "Mô tả dự án A", userId);
        projectPage.waitForPageLoad();
        projectPage.searchProject("Dự án A");
        assert projectPage.getProjectNames().contains("Dự án A") : "Search should return the correct project";
    }

    /**
     * Test Case: Kiểm tra tìm kiếm dự án với từ khóa không tồn tại
     * Expected: Hiển thị thông báo “Chưa có dự án nào”
     */
    @Test
    public void testSearchProjectNotFound() {
        MockUtils.mockProject("Dự án B", "Mô tả dự án B", userId);
        projectPage.waitForPageLoad();
        projectPage.searchProject("Dự án A");
        assert projectPage.getProjectNames().isEmpty() : "Search for nonexistent project should return empty list";
    }

}
