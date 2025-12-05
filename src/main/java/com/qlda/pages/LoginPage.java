package com.qlda.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;


public class LoginPage {

    private WebDriver driver;

    // 1. Định nghĩa Locators 
    private By usernameInput = By.xpath("//input[@id='email']");
    private By passwordInput = By.xpath("//input[@id='password']"); 
    private By loginButton = By.xpath("//button[contains(text(),'Đăng nhập')]"); 
    private By forgotPasswLink =By.xpath("//a[contains(text(),'Quên mật khẩu?')]");
    private WebDriverWait wait;
    // 2. Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // 3. Viết các hàm hành động
    public void enterUsername(String username) {
       // driver.findElement(usernameInput).sendKeys(username);
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput))
                .sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordInput).sendKeys(password);
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
        return new ForgotPasswordPage(driver);
    }

}