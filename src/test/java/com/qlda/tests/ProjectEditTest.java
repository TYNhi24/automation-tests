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

public class ProjectEditTest extends BaseTest {
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
    }

    /**
     * Test Case: Kiểm tra chỉnh sửa thông tin dự án thành công
     * Expected: Tên và mô tả mới xuất hiện trong danh sách
     */
    @Test
    public void testEditProjectSuccess() {
        projectPage.clickEditButton();
        projectPage.fillProjectForm("Dự án B", "Mô tả dự án B");
        projectPage.clickSubmitButton();
        projectPage.waitForProjectName("Dự án B");
        Assert.assertTrue(projectPage.getProjectNames().contains("Dự án B"), "Edited project name should be visible in list");
    }

    /**
     * Test Case: Kiểm tra bỏ trống trường bắt buộc khi sửa
     * Expected: Hiển thị thông báo lỗi tên dự án là bắt buộc
     */
    @Test
    public void testEditProjectWithEmptyName() {
        projectPage.clickEditButton();
        projectPage.fillProjectForm("", "Mô tả dự án B");
        projectPage.clickSubmitButton();
        Assert.assertTrue(projectPage.hasAnyError(), "Should show error for empty project name when editing");
    }

    /**
     * Test Case: Kiểm tra khi không có thay đổi nào
     * Expected: Hiển thị thông báo lỗi tên dự án là bắt buộc
     */
    @Test
    public void testEditProjectWithoutChanges() {
        projectPage.clickEditButton();
        projectPage.clickSubmitButton();
        projectPage.waitForProjectName("Dự án A");
        Assert.assertTrue(projectPage.getProjectNames().contains("Dự án A"), "Edited project name should be visible in list");
    }

    /**
     * Test Case: Kiểm tra độ dài tối thiểu của tên dự án
     * Expected: Hiển thị thông báo lỗi tên dự án quá ngắn
     */
    @Test
    public void testEditProjectWithShortName() {
        projectPage.clickEditButton();
        projectPage.fillProjectForm("B", "Mô tả dự án B");
        projectPage.clickSubmitButton();
        Assert.assertTrue(projectPage.hasAnyError(), "Should show error for short project name when editing");
    }

    /**
     * Test Case: Kiểm tra độ dài tối đa của tên dự án
     * Expected: Hiển thị thông báo lỗi tên dự án quá dài
     */
    @Test
    public void testEditProjectWithLongName() {
        String longName = "B".repeat(101);
        projectPage.clickEditButton();
        projectPage.fillProjectForm(longName, "Mô tả dự án B");
        projectPage.clickSubmitButton();
        Assert.assertTrue(projectPage.hasAnyError(), "Should show error for long project name when editing");
    }

    /**
     * Test Case: Chỉnh sửa dự án với mô tả quá dài
     * Expected: Hiển thị thông báo lỗi mô tả quá dài
     */
    @Test
    public void testEditProjectWithLongDescription() {
        String longDesc = "a".repeat(501);
        projectPage.clickEditButton();
        projectPage.fillProjectForm("Dự án B đã sửa", longDesc);
        projectPage.clickSubmitButton();
        Assert.assertTrue(projectPage.hasAnyError(), "Should show error for long description when editing");
    }
}
