package com.qlda.core;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    protected void waitAndClick(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void waitAndSendKeys(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    protected String waitAndGetText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    protected boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasAnyError() {
        try {
            // Try multiple error detection strategies
            String[] errorSelectors = {
                    ".error-message",
                    "[class*='bg-red']",
                    "[class*='text-red']",
                    "[class*='border-red']",
                    "[class*='error']",
                    "[class*='danger']",
                    "[class*='alert']",
                    ".fixed.top-4.right-4"
            };

            String[] errorTexts = {
                    "error", "failed", "invalid", "không hợp lệ", "tồn tại",
                    "không khớp", "tối thiểu", "required", "match", "minimum", "email"
            };

            for (String selector : errorSelectors) {
                List<WebElement> elements = driver.findElements(By.cssSelector(selector));
                for (WebElement element : elements) {
                    try {
                        wait.until(ExpectedConditions.visibilityOf(element));
                    } catch (Exception ignored) {
                    }
                    if (element.isDisplayed() && !element.getText().trim().isEmpty()) {
                        System.out.println("Found error: " + element.getText());
                        return true;
                    }
                }
            }

            List<WebElement> allElements = driver.findElements(By.xpath("//*"));
            for (WebElement element : allElements) {
                if (element.isDisplayed()) {
                    String text = element.getText().toLowerCase();
                    for (String errorText : errorTexts) {
                        if (text.contains(errorText)) {
                            System.out.println("Found error text: " + element.getText());
                            return true;
                        }
                    }
                }
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

}
