package com.qlda.tests;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.qlda.core.BaseTest;
import com.qlda.pages.LoginPage;
import com.qlda.utils.MockUtils;
import com.qlda.utils.WebDriverConfig;

public class LoginTest extends BaseTest {
    private LoginPage loginPage;

    @BeforeMethod
    @Override
    public void setUp() throws IOException {
        super.setUp();
        MockUtils.mockUser(
                config.getProperty("TEST_EMAIL"),
                config.getProperty("TEST_PASSWORD"),
                config.getProperty("TEST_USERNAME"),
                false);
        driver.get(WebDriverConfig.getBaseUrl() + "/login");
        loginPage = new LoginPage(driver, wait);
    }

    /**
     * Test Case: Đăng nhập thành công với tài khoản hợp lệ
     * Expected: Chuyển hướng sang trang chính (dashboard)
     */
    @Test
    public void testSuccessfulLogin() {
        loginPage.loginUser(config.getProperty("TEST_EMAIL"), config.getProperty("TEST_PASSWORD"));
        Assert.assertTrue(loginPage.isSuccessful(),
                "Should redirect to dashboard after successful login");
    }

    /**
     * Test Case: Đăng nhập với email không tồn tại
     * Expected: Hiển thị thông báo lỗi tài khoản không tồn tại
     */
    @Test
    public void testLoginWithNonExistingEmail() {
        loginPage.loginUser("notfound@gmail.com", config.getProperty("TEST_PASSWORD"));
        Assert.assertTrue(loginPage.hasAnyError(), "Should show error for non-existing email");
    }

    /**
     * Test Case: Đăng nhập với mật khẩu sai
     * Expected: Hiển thị thông báo lỗi mật khẩu không đúng
     */
    @Test
    public void testLoginWithWrongPassword() {
        loginPage.loginUser(config.getProperty("TEST_EMAIL"), "WrongPassword");
        Assert.assertTrue(loginPage.hasAnyError(), "Should show error for wrong password");
    }

    /**
     * Test Case: Đăng nhập với email không hợp lệ
     * Expected: Hiển thị thông báo lỗi định dạng email
     */
    @Test
    public void testLoginWithInvalidEmailFormat() {
        loginPage.loginUser("invalidemail", config.getProperty("TEST_PASSWORD"));
        Assert.assertTrue(loginPage.hasAnyError(), "Should show error for invalid email format");
    }

    /**
     * Test Case: Đăng nhập với trường email bỏ trống
     * Expected: Hiển thị thông báo lỗi yêu cầu nhập thông tin
     */
    @Test
    public void testLoginWithEmptyEmailField() {
        loginPage.loginUser("", config.getProperty("TEST_PASSWORD"));
        Assert.assertTrue(loginPage.hasAnyError(), "Should show error for empty fields");
    }

    /**
     * Test Case: Đăng nhập với trường bỏ trống
     * Expected: Hiển thị thông báo lỗi yêu cầu nhập thông tin
     */
    @Test
    public void testLoginWithEmptyPasswordField() {
        loginPage.loginUser(config.getProperty("TEST_EMAIL"), "");
        Assert.assertTrue(loginPage.hasAnyError(), "Should show error for empty password field");
    }

    /**
     * Test Case: Đăng nhập bằng Google
     * Expected: Chuyển hướng sang trang chính (dashboard) sau khi xác thực Google
     */
    @Test
    public void testGoogleLoginButtonRedirect() {
        loginPage.loginWithGoogle();
        Assert.assertTrue(driver.getCurrentUrl().contains("accounts.google.com"),
                "Should redirect to Google OAuth page");
    }

}