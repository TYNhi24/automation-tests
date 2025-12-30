package com.qlda.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.qlda.core.BasePage;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ForgotPasswordPage extends BasePage {

    private By title        = By.xpath("//h1[contains(text(),'Quên mật khẩu?')]");
    private By emailInput   = By.xpath("//input[@id='email']");
    private By submitButton = By.xpath("//button[contains(text(),'Gửi link reset')]");

    public ForgotPasswordPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public boolean isFormDisplayed() {
        return waitForVisible(emailInput).isDisplayed()
                && waitForVisible(submitButton).isDisplayed();
    }

    public String getTitleText() {
        return waitForVisible(title).getText().trim();
    }

    public void enterEmail(String email) {
        waitForVisible(emailInput).sendKeys(email);
    }
    
    /**
     * Phương thức này kiểm tra lỗi validation của HTML5.
     * dùng JS để kiểm tra trạng thái 'validity.valid' của input.
     */
    public boolean isEmailInputInvalid() {
        driver.findElement(submitButton).click(); 
        
        WebElement emailElement = driver.findElement(emailInput);
 
        JavascriptExecutor js = (JavascriptExecutor) driver;
        // Kiểm tra thuộc tính validity.valid (trả về false nếu có lỗi)
        return !(Boolean) js.executeScript("return arguments[0].validity.valid;", emailElement);
    }
}
