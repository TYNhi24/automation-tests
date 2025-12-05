package com.qlda.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

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
        if (driver != null) {
            driver.quit();
            driver = null;
        }
        String browserName = prop.getProperty("BROWSER");

        if (browserName.equalsIgnoreCase("CHROME")) {
            WebDriverManager.chromedriver().setup(); // Tự động tải driver
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

    // Hàm lấy URL từ file config
    public static String getBaseUrl() {
        return prop.getProperty("BASE_URL");
    }
    public static WebDriver getDriver() { 
        return driver; 
    }
    // Hàm đóng trình duyệt
    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}