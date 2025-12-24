package com.qlda.core;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    // Chờ element hiển thị
    public WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // Chờ element có thể click
    public WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    // Chờ element biến mất
    public boolean waitForInvisible(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    // Di chuyển tới element
    public void scrollToElement(By locator) {
        WebElement element = waitForVisible(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void scrollToElementCenter(By locator) {
        WebElement element = waitForVisible(locator);
        ((JavascriptExecutor) driver).executeScript(
                "var rect = arguments[0].getBoundingClientRect();" +
                        "window.scrollBy({top: rect.top - window.innerHeight/2, behavior: 'smooth'});",
                element);
    }

    // Scroll to top of the page
    public void scrollToTop() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
    }

    // Kiểm tra element có hiển thị không
    public boolean isDisplayed(By locator) {
        try {
            return waitForVisible(locator).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    // Click vào element
    public void click(By locator) {
        waitForClickable(locator).click();
    }

    // Nhập text vào element
    public void type(By locator, String text) {
        WebElement element = waitForVisible(locator);
        element.click();
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        element.sendKeys(text);
    }

    public void select(By locator, String value) {
        Select select = new Select(waitForVisible(locator));
        select.selectByVisibleText(value);
    }

    // Lấy URL hiện tại
    public String getUrlString() {
        return driver.getCurrentUrl();
    }

    // Lấy text của element
    public String getText(By locator) {
        return waitForVisible(locator).getText();
    }

    // Lấy attribute của element
    public String getAttribute(By locator, String attribute) {
        return waitForVisible(locator).getAttribute(attribute);
    }

    // Lấy danh sách element
    public List<WebElement> getElements(By locator) {
        return driver.findElements(locator);
    }

    // Lấy giá trị đã chọn dropdown
    public String getSelectedOption(By locator) {
        Select select = new Select(waitForVisible(locator));
        return select.getFirstSelectedOption().getText();
    }

    // Click bằng Actions
    public void actionClick(By locator) {
        Actions actions = new Actions(driver);
        WebElement element = waitForClickable(locator);
        actions.moveToElement(element).click().perform();
    }

    // Hover chuột vào element
    public void hover(By locator) {
        Actions actions = new Actions(driver);
        WebElement element = waitForVisible(locator);
        actions.moveToElement(element).perform();
    }

    // Cuộn trang xuống bằng Robot class
    public void robotScrollDown() throws AWTException {
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_PAGE_DOWN);
        robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
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
