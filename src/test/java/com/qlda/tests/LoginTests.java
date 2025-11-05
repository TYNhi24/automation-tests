package com.qlda.tests;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
// import com.qlda.pages.LoginPage; // <-- TẠM THỜI TẮT ĐI
import com.qlda.utils.WebDriverConfig;

public class LoginTests {
    WebDriver driver;
    // LoginPage loginPage; // <-- TẠM THỜI TẮT ĐI

    @BeforeTest
    public void init() throws IOException{
        WebDriverConfig.initializeDriver(); // Khởi tạo driver từ lớp utils
        driver = WebDriverConfig.driver;
        driver.get(WebDriverConfig.getBaseUrl()); // Lấy URL từ file config
        // loginPage = new LoginPage(driver); // <-- TẠM THỜI TẮT ĐI
    }

    @Test
    public void TC001_Demo_Mo_Trinh_Duyet(){
        // Để trống
        // Chúng ta có thể thêm một lệnh tạm dừng để bạn nhìn thấy
        try {
            System.out.println("Trình duyệt đã mở, giữ trong 5 giây...");
            Thread.sleep(5000); // Tạm dừng 5 giây (5000 mili-giây)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterTest
    public void Teardown(){
        WebDriverConfig.quitDriver(); // Đóng trình duyệt
    }
}