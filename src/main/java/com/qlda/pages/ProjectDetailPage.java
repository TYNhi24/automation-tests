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
    private By shareBtn = By.xpath("//button[normalize-space()='Chia sẻ']");  
    
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
    // tìm hàng chứa Task
    private By taskRowByName(String name) {
        return By.xpath("//div[p[normalize-space()='" + name + "']]");
    }

    // checkbox nằm bên trong hàng 
    private By checkboxInsideRow = By.xpath(".//input[@type='checkbox']");

    //click vào checkbox của một task cụ thể
    public void toggleTaskCheckbox(String taskName) {
        WebElement row = wait.until( ExpectedConditions.visibilityOfElementLocated(taskRowByName(taskName)));
        WebElement cb = row.findElement(checkboxInsideRow);
        cb.click();
    }

    // Kiểm tra trạng thái hoàn thành: khi hoàn thành thẻ <p> sẽ có class 'line-through'
    public boolean isTaskCompleted(String taskName) {
        WebElement row = wait.until(
                ExpectedConditions.visibilityOfElementLocated(taskRowByName(taskName))
        );
        // Tìm thẻ p chứa tên task
        WebElement taskText = row.findElement(By.xpath(".//p"));
        String classValue = taskText.getAttribute("class");
        return classValue != null && classValue.contains("line-through");
    }

    /** Click vào thẻ Task để xem chi tiết */
    public void openTaskDetails(String taskName) {
        String taskCardXPath = "//div[contains(@class, 'cursor-pointer') and .//*[normalize-space()='" + taskName + "']]";
        WebElement card = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(taskCardXPath)));
        card.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[contains(text(),'Mô tả')]")));
    }
    
}
