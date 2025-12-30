package com.qlda.core;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.time.Duration;
import com.qlda.pages.LoginPage;
import com.qlda.utils.WebDriverConfig;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    
    @BeforeMethod
    public void setup() throws IOException {
        WebDriverConfig.initializeDriver(); 
        driver = WebDriverConfig.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(WebDriverConfig.getBaseUrl()); 
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            WebDriverConfig.quitDriver();
            driver = null;
        }
    }

    public void baseLogin(String email, String password) {
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.login(email, password);
    }
}
