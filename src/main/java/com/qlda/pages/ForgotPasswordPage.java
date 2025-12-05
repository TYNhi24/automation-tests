package com.qlda.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ForgotPasswordPage {
    private WebDriver driver;

    private By title        = By.xpath("//h1[contains(text(),'Quên mật khẩu?')]");
    private By emailInput   = By.xpath("//input[@id='email']");
    private By submitButton = By.xpath("//button[contains(text(),'Gửi link reset')]");

    public ForgotPasswordPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isFormDisplayed() {
        return driver.findElement(emailInput).isDisplayed()
                && driver.findElement(submitButton).isDisplayed();
    }

    public String getTitleText() {
        return driver.findElement(title).getText().trim();
    }

    public void enterEmail(String email) {
        driver.findElement(emailInput).sendKeys(email);
    }
    
    /**
     * Phương thức này kiểm tra lỗi validation của HTML5.
     * dùng JS để kiểm tra trạng thái 'validity.valid' của input.
     */
    public boolean isEmailInputInvalid() {
        // 1. Click nút Gửi để kích hoạt validation
        driver.findElement(submitButton).click(); 
        
        // 2. Lấy đối tượng WebElement của trường Email
        WebElement emailElement = driver.findElement(emailInput);
        
        // 3. Sử dụng JavaScript Executor để kiểm tra lỗi validation
        JavascriptExecutor js = (JavascriptExecutor) driver;
        // Kiểm tra thuộc tính validity.valid (trả về false nếu có lỗi)
        return !(Boolean) js.executeScript("return arguments[0].validity.valid;", emailElement);
    }
}
