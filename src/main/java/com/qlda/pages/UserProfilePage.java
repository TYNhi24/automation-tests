package com.qlda.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qlda.core.BasePage;

public class UserProfilePage extends BasePage {
   
    public UserProfilePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }
   
    public boolean isUserNameDisplayed(String name) {
        try {
            String dynamicXPath = "//h2[normalize-space()='" + name + "']";
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXPath))).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isEmailDisplayed(String email) {
        try {
            String dynamicXPath = "//p[normalize-space()='" + email + "']";
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXPath))).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isNotCreatedProjectNDisplayed(String expectedText) {
        try {
            String dynamicXPath = "//p[contains(text(),'Bạn chưa tạo dự án nào.')]";
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXPath))).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCreatedProjectDisplayed(String nameProject) {
        try {
            String dynamicXPath = "//p[normalize-space()='" + nameProject + "']";
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXPath))).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
