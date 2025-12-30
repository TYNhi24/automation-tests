package com.qlda.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.qlda.core.BasePage;

public class LoginPage extends BasePage {
    // 1. Khai báo locators
    private By usernameInput = By.xpath("//input[@id='email']");
    private By passwordInput = By.xpath("//input[@id='password']"); 
    private By loginButton = By.xpath("//button[contains(text(),'Đăng nhập')]"); 
    private By forgotPasswLink =By.xpath("//a[contains(text(),'Quên mật khẩu?')]");
    
    // 2. Constructor
    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    // 3. Viết các hàm hành động
    public void enterUsername(String username) {
        waitForVisible(usernameInput).sendKeys(username);
    }

    public void enterPassword(String password) {
        waitForVisible(passwordInput).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    // Hành động gộp
    public void login(String username, String password) {
        
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }
    //Điều hướng đến form quên mật khẩu
    public ForgotPasswordPage clickForgotPasswordLink(){
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(forgotPasswLink));
        link.click(); 
        return new ForgotPasswordPage(driver, wait);
    }

}