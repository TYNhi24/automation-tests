package com.qlda.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebDriverConfig {

    public static Properties prop;
    public static WebDriver driver;

    // Hàm đọc file config.properties
    public static void loadProperties() throws IOException {
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
        prop.load(fis);
    }

    // Hàm khởi tạo trình duyệt
    public static void initializeDriver() throws IOException {
        loadProperties(); // Đọc config
        String browserName = prop.getProperty("BROWSER");

        if (browserName.equalsIgnoreCase("CHROME")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else if (browserName.equalsIgnoreCase("FIREFOX")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        } else if (browserName.equalsIgnoreCase("EDGE")) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
        }

        driver.manage().window().maximize();
    }

    // Hàm tạo WebDriver mới
    public static WebDriver createDriver() {
        try {
            if (prop == null) {
                loadProperties();
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot load config properties", e);
        }
        String browserName = prop.getProperty("BROWSER", "CHROME");
        WebDriver newDriver;
        if (browserName.equalsIgnoreCase("CHROME")) {
            WebDriverManager.chromedriver().setup();
            newDriver = new ChromeDriver();
        } else if (browserName.equalsIgnoreCase("FIREFOX")) {
            WebDriverManager.firefoxdriver().setup();
            newDriver = new FirefoxDriver();
        } else if (browserName.equalsIgnoreCase("EDGE")) {
            WebDriverManager.edgedriver().setup();
            newDriver = new EdgeDriver();
        } else {
            throw new RuntimeException("Unsupported browser: " + browserName);
        }
        newDriver.manage().window().maximize();
        return newDriver;
    }

    // Hàm lấy URL từ file config
    public static String getBaseUrl() {
        return prop.getProperty("BASE_URL");
    }

    // Hàm đóng trình duyệt
    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}