package com.qlda.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.qlda.utils.DatabaseUtils;
import com.qlda.utils.WebDriverConfig;

public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Properties config;

    @BeforeClass
    public void classSetup() {
        DatabaseUtils.initConnection();
        DatabaseUtils.testConnection();
    }

    @BeforeMethod
    public void setUp() throws IOException {
        config = new Properties();
        config.load(new FileInputStream("src/test/resources/config.properties"));
        WebDriverConfig.initializeDriver();
        driver = WebDriverConfig.driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterMethod
    public void tearDown() {
        DatabaseUtils.clearAllTables();
        WebDriverConfig.quitDriver();
    }

    @AfterClass
    public void classTeardown() {
        DatabaseUtils.closeConnection();
    }
}
