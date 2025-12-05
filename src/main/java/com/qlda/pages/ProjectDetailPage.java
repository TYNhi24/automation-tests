package com.qlda.pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ProjectDetailPage {
    private WebDriver driver;
    private  WebDriverWait wait;
    public ProjectDetailPage(WebDriver driver) {
        this.driver = driver;
    }
   // nút "Xem" dự án
    private By viewProject = By.xpath(
    "(//div[contains(@class,'flex gap-2')]//button[contains(.,'Xem')])[1]");

    // khu vực danh sách thẻ công việc (board)
    private By taskBoard = By.xpath("//div[contains(@class,'flex-grow') and contains(@class,'overflow-y-auto')]");

    // mỗi thẻ công việc
    private By taskCards = By.xpath("//div[contains(@class,'bg-gray-100') and contains(@class,'rounded-lg')]");

    // tiêu đề trên từng thẻ (relative!)
    private By taskTitleInCard = By.xpath(".//h2");

    public void clickView() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(viewProject)
        );
        btn.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(taskBoard));
    }

    public boolean isTaskBoardDisplayed() {
        return driver.findElement(taskBoard).isDisplayed();
    }

    public int getTaskCount() {
        return driver.findElements(taskCards).size();
    }

    public List<String> getTaskTitles() {
        return driver.findElements(taskCards)
                .stream()
                .map(e -> e.findElement(taskTitleInCard).getText().trim())
                .collect(Collectors.toList());
    }
    
    // checkbox
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
