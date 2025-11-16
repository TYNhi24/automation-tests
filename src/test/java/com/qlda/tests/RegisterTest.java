package com.qlda.tests;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.qlda.core.BaseTest;
import com.qlda.pages.RegisterPage;
import com.qlda.utils.WebDriverConfig;

public class RegisterTest extends BaseTest {
    private RegisterPage registerPage;

    @BeforeMethod
    @Override
    public void setUp() throws IOException {
        super.setUp();
        driver.get(WebDriverConfig.getBaseUrl() + "/register");
        registerPage = new RegisterPage(driver, wait);
    }

    /**
     * Test Case: Kiểm tra đăng ký thành công với thông tin hợp lệ
     * Expected: Tài khoản được tạo và chuyển sang trang login
     */
    @Test
    public void testSuccessfulRegistration() {
        registerPage.registerUser("Nguyễn Văn A", "test@gmail.com", "Password123");
        Assert.assertTrue(registerPage.isSuccessful(),
                "Should redirect to login page after successful registration");
    }

    /**
     * Test Case: Kiểm tra đăng ký với email đã tồn tại
     * Expected: Hiển thị thông báo lỗi email đã tồn tại
     */
    @Test
    public void testRegistrationWithExistingEmail() {

        // Register first user
        registerPage.registerUser("User First", "test@gmail.com", "Password123");

        // Navigate back to register page
        driver.get(WebDriverConfig.getBaseUrl() + "/register");
        registerPage = new RegisterPage(driver, wait);

        // Try to register with same email
        registerPage.registerUser("User Second", "test@gmail.com", "Password123");

        Assert.assertTrue(registerPage.hasAnyError(), "Should show email already exists error");
    }

    /**
     * Test Case: Kiểm tra định dạng email không hợp lệ
     * Expected: Hiển thị thông báo lỗi email không hợp lệ
     */
    @Test
    public void testRegistrationWithInvalidEmail() {
        registerPage.registerUser("Nguyễn Văn A", "invalidemail", "Password123");
        Assert.assertTrue(registerPage.hasAnyError(), "Should show invalid email error");
    }

    /**
     * Test Case: Kiểm tra mật khẩu và xác nhận không khớp
     * Expected: Hiển thị thông báo lỗi mật khẩu không khớp
     */
    @Test
    public void testRegistrationWithMismatchedPasswords() {
        registerPage.registerUser("Nguyễn Văn A", "test@gmail.com", "Password123", "WrongPassword");
        Assert.assertTrue(registerPage.hasAnyError(), "Should show password mismatch error");
    }

    /**
     * Test Case: Kiểm tra mật khẩu quá ngắn
     * Expected: Hiển thị thông báo lỗi mật khẩu tối thiểu
     */
    @Test
    public void testRegistrationWithShortPassword() {
        registerPage.registerUser("Nguyễn Văn A", "test@gmail.com", "Pass");
        Assert.assertTrue(registerPage.hasAnyError(), "Should show minimum password length error");
    }

    /**
     * Test Case: Kiểm tra định dạng mật khẩu
     * Expected: Hiển thị thông báo lỗi định dạng mật khẩu
     */
    @Test
    public void testRegistrationWithWeakPassword() {
        registerPage.registerUser("Nguyễn Văn A", "test@gmail.com", "password123");
        Assert.assertTrue(registerPage.hasAnyError(), "Should show weak password format error");
    }
}