package com.qlda.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;
import com.qlda.core.BasePage;
import java.time.Duration;

public class ProjectDetailPage extends BasePage {
  
    public ProjectDetailPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }
    private By viewProject = By.xpath("(//div[contains(@class,'flex gap-2')]//button[contains(.,'Xem')])[1]");

    private By taskBoard = By.xpath("//div[contains(@class,'flex-grow') and contains(@class,'overflow-y-auto')]");

    public void clickView() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement btn = wait.until( ExpectedConditions.elementToBeClickable(viewProject));
        btn.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(taskBoard));
    }

    public boolean isTaskBoardDisplayed() {
        return driver.findElement(taskBoard).isDisplayed();
    }

    public boolean isTaskInsideList(String listName, String taskName) {
        try {
            String dynamicXPath = "//div[normalize-space()='" + listName + "']" +
                                "/ancestor::div[contains(@class, 'rounded-lg') or contains(@class, 'w-')][1]" + 
                                "//*[normalize-space()='" + taskName + "']";
            
            System.out.println("DEBUG: Searching XPath: " + dynamicXPath);
            
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXPath))).isDisplayed();
        } catch (Exception e) {
            System.err.println("Không tìm thấy Task '" + taskName + "' trong '" + listName + "'");
            return false;
        }
    }

    private By listHeader(String listName) {
        return By.xpath("//*[contains(text(),'" + listName + "')]");
    }

    // Kéo thả danh sách công việc
    public void dragAndDropList(String sourceListName, String targetListName) {
        Actions actions = new Actions(driver);
        WebElement source = wait.until(ExpectedConditions.visibilityOfElementLocated(listHeader(sourceListName)));
        WebElement target = wait.until(ExpectedConditions.visibilityOfElementLocated(listHeader(targetListName)));

        actions.dragAndDrop(source, target).build().perform();
    }
    
    // checkbox
    public boolean isCheckBoxDisplayed() {
        try {
            String cssString = "input[type='checkbox']";
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssString))).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    private By taskRowByName(String name) {
        return By.xpath(
                "//div[contains(@class,'task-row') or contains(@class,'flex items-center')]" +
                "[.//span[normalize-space()='" + name + "']]"
        );
    }

    private By checkboxInsideRow = By.xpath(".//input[@type='checkbox' or @role='checkbox']");

    /**hiển thị checkbox với từng nhiệm vụ */
    public boolean isTaskCheckboxDisplayed(String taskName) {
        WebElement row = wait.until(
                ExpectedConditions.visibilityOfElementLocated(taskRowByName(taskName))
        );
        return row.findElement(checkboxInsideRow).isDisplayed();
    }

    /** click vào checkbox */
    public void toggleTaskCheckbox(String taskName) {
        WebElement row = wait.until(
                ExpectedConditions.elementToBeClickable(taskRowByName(taskName))
        );
        WebElement cb = row.findElement(checkboxInsideRow);
        cb.click();
    }

    /** trạng thái đã hoàn thành hay chưa
     *  - nếu là <input type="checkbox">: dùng isSelected()
     *  - nếu là button role="checkbox": check aria-checked
     */
    public boolean isTaskCompleted(String taskName) {
        WebElement row = wait.until(
                ExpectedConditions.visibilityOfElementLocated(taskRowByName(taskName))
        );
        WebElement cb = row.findElement(checkboxInsideRow);

        if ("input".equalsIgnoreCase(cb.getTagName())) {
            return cb.isSelected();
        }
        String aria = cb.getAttribute("aria-checked");
        return "true".equalsIgnoreCase(aria);
    }
    
}
