package com.qlda.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qlda.core.BasePage;

public class LoginPage extends BasePage {
    private final By emailField = By.id("email");
    private final By passwordField = By.id("password");
    private final By loginButton = By.cssSelector("button[type='submit']");
    private final By googleLoginButton = By.xpath("//button[contains(.,'Google')]");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public LoginPage waitForPageLoad() {
        wait.until(ExpectedConditions.presenceOfElementLocated(emailField));
        return this;
    }

    public LoginPage enterEmail(String email) {
        waitAndSendKeys(emailField, email);
        return this;
    }

    public LoginPage enterPassword(String password) {
        waitAndSendKeys(passwordField, password);
        return this;
    }

    public LoginPage fillLoginForm(String email, String password) {
        waitForPageLoad();
        enterEmail(email);
        enterPassword(password);
        return this;
    }

    public void clickLogin() {
        waitAndClick(loginButton);
    }

    public void loginUser(String email, String password) {
        fillLoginForm(email, password);
        clickLogin();
    }

    public void loginWithGoogle() {
        waitAndClick(googleLoginButton);
    }

    public boolean isSuccessful() {
        try {
            wait.until(ExpectedConditions.urlContains("/projects"));
            return driver.getCurrentUrl().contains("/projects");
        } catch (Exception e) {
            return false;
        }
    }

}