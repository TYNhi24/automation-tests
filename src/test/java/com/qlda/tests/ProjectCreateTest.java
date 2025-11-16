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

public class ProjectCreateTest extends BaseTest {
    private ProjectPage projectPage;
    private LoginPage loginPage;

    @BeforeMethod
    @Override
    public void setUp() throws IOException {
        MockUtils.mockUser("test@gmail.com", "Password123", "Valid User", false);
        super.setUp();
        driver.get(WebDriverConfig.getBaseUrl() + "/login");
        loginPage = new LoginPage(driver, wait);
        projectPage = new ProjectPage(driver, wait);
        loginPage.loginUser("test@gmail.com", "Password123");
    }

    /**
     * Test Case: Kiểm tra thêm dự án thành công với thông tin hợp lệ
     * Expected: Dự án mới xuất hiện trong danh sách
     */
    @Test
    public void testCreateProjectSuccess() {
        projectPage.clickCreateButton();
        projectPage.fillProjectForm("Dự án A", "Mô tả dự án A");
        projectPage.clickSubmitButton();
        projectPage.waitForProjectName("Dự án A");
        Assert.assertTrue(projectPage.getProjectNames().contains("Dự án A"), "Project should be created and visible in list");
    }

    /**
     * Test Case: Kiểm tra thêm dự án với tên rỗng
     * Expected: Hiển thị thông báo lỗi tên dự án là bắt buộc
     */
    @Test
    public void testCreateProjectWithEmptyName() {
        projectPage.clickCreateButton();
        projectPage.fillProjectForm("", "Mô tả dự án A");
        projectPage.clickSubmitButton();
        Assert.assertTrue(projectPage.hasAnyError(), "Should show error for empty project name");
    }

    /**
     * Test Case: Kiểm tra độ dài tối thiểu của tên dự án
     * Expected: Hiển thị thông báo lỗi tên dự án tối thiểu
     */
    @Test
    public void testCreateProjectWithShortName() {
        projectPage.clickCreateButton();
        projectPage.fillProjectForm("A", "Mô tả dự án A");
        projectPage.clickSubmitButton();
        Assert.assertTrue(projectPage.hasAnyError(), "Should show error for short project name");
    }

    /**
     * Test Case: Kiểm tra độ dài tối đa của tên dự án
     * Expected: Hiển thị thông báo lỗi tên quá dài
     */
    @Test
    public void testCreateProjectWithLongName() {
        String longName = "A".repeat(101);
        projectPage.clickCreateButton();
        projectPage.fillProjectForm(longName, "Mô tả dự án A");
        projectPage.clickSubmitButton();
        Assert.assertTrue(projectPage.hasAnyError(), "Should show error for long project name");
    }

    /**
     * Test Case: Kiểm tra độ dài tối đa của mô tả
     * Expected: Hiển thị thông báo lỗi mô tả quá dài
     */
    @Test
    public void testCreateProjectWithLongDescription() {
        String longDesc = "a".repeat(501);
        projectPage.clickCreateButton();
        projectPage.fillProjectForm("Dự án A", longDesc);
        projectPage.clickSubmitButton();
        Assert.assertTrue(projectPage.hasAnyError(), "Should show error for long description");
    }

}
