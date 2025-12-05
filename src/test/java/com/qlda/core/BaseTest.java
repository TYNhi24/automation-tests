package com.qlda.core;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.qlda.pages.LoginPage;
import com.qlda.utils.WebDriverConfig;

public class BaseTest {
// Khai báo chung để các lớp con có thể truy cập
    protected WebDriver driver;
    @BeforeMethod
    public void setup() throws IOException {
        WebDriverConfig.initializeDriver(); // Khởi tạo config
        driver = WebDriverConfig.getDriver(); // Lấy driver đã được khởi tạo
        driver.get(WebDriverConfig.getBaseUrl()); // Lấy URL từ file config
        
    }

    // Phương thức dọn dẹp
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            WebDriverConfig.quitDriver(); // Đóng trình duyệt (từ WebDriverConfig)
            driver = null;
        }
    }
    public void baseLogin(String email, String password) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(email, password);
    }
}
