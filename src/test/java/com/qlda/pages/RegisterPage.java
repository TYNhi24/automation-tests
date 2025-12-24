package com.qlda.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qlda.core.BasePage;

public class RegisterPage extends BasePage {
    // Locators
    private final By displayNameField = By.id("displayName");
    private final By emailField = By.id("email");
    private final By passwordField = By.id("password");
    private final By confirmPasswordField = By.id("confirm");
    private final By registerButton = By.cssSelector("button[type='submit']");
    
    public RegisterPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    /**
     * Wait for page to load
     */
    public RegisterPage waitForPageLoad() {
        wait.until(ExpectedConditions.presenceOfElementLocated(displayNameField));
        return this;
    }

    /**
     * Enter display name
     */

    public RegisterPage enterDisplayName(String name) {
        type(displayNameField, name);
        return this;
    }

    /**
     * Enter email
     */

    public RegisterPage enterEmail(String email) {
        type(emailField, email);
        return this;
    }

    /**
     * Enter password
     */

    public RegisterPage enterPassword(String password) {
        type(passwordField, password);
        return this;
    }

    /**
     * Enter confirm password (try both locators)
     */

    public RegisterPage enterConfirmPassword(String confirmPassword) {
        type(confirmPasswordField, confirmPassword);
        return this;
    }

    /**
     * Click register button
     */

    public void clickRegister() {
        click(registerButton);
    }

    /**
     * Complete registration form
     */
    public RegisterPage fillRegistrationForm(String displayName, String email, String password, String confirmPassword) {
        waitForPageLoad();
        enterDisplayName(displayName);
        enterEmail(email);
        enterPassword(password);
        enterConfirmPassword(confirmPassword);
        return this;
    }

    /**
     * Register user - complete process
     */
    public void registerUser(String displayName, String email, String password, String confirmPassword) {
        fillRegistrationForm(displayName, email, password, confirmPassword);
        clickRegister();
    }

    /**
     * Register user with same password confirmation
     */
    public void registerUser(String displayName, String email, String password) {
        registerUser(displayName, email, password, password);
    }

    /**
     * Check if registration was successful (redirected to login)
     */
    public boolean isSuccessful() {
        try {
            wait.until(ExpectedConditions.urlContains("login"));
            return driver.getCurrentUrl().contains("login");
        } catch (Exception e) {
            return false;
        }
    }
}