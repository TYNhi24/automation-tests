package com.qlda.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qlda.core.BasePage;

public class TaskPage extends BasePage {
    private final By task = By.xpath("//div[@class='flex items-start gap-2 flex-1']");
    private final By attachButton = By.xpath("//span[contains(text(),'Đính kèm')]");
    private final By fileInput = By.xpath("//input[@type='file']");
    private final By missionButton = By.xpath("//button[contains(text(),'Thêm một mục')]");
    private final By missionInput = By.xpath("//textarea[@placeholder='Thêm một mục...']");
    private final By addMissionButton = By.xpath("//span[normalize-space()='Thêm']");
    private final By closeMissionButton = By
            .xpath("//span[@class='material-icons text-lg'][normalize-space()='close']");
    private final By missionProgress = By.xpath("//span[@class='text-sm font-medium text-gray-600']");

    public TaskPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public TaskPage waitForPageLoad() {
        waitForVisible(task);
        return this;
    }

    public void openTaskModal() {
        click(task);
    }

    public void uploadFile(String filePath) {
        click(attachButton);
        WebElement input = driver.findElement(fileInput);
        if (!input.isDisplayed()) {
            ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("arguments[0].style.display = 'block';", input);
        }
        waitForVisible(fileInput).sendKeys(filePath);
    }

    public boolean isFileUploaded(String fileName) {
        By uploadedFile = By.xpath("//a[normalize-space()='" + fileName + "']");
        return isDisplayed(uploadedFile);
    }

    public boolean isMissionButtonVisible() {
        return isDisplayed(missionButton);
    }

    public void openMissionInput() {
        click(missionButton);
    }

    public boolean isMissionInputVisible() {
        return isDisplayed(missionInput);
    }

    public void enterMission(String missionText) {
        WebElement input = waitForVisible(missionInput);
        input.clear();
        input.sendKeys(missionText);
    }

    public void addMission() {
        click(addMissionButton);
    }

    public boolean isAddMissionDisabled() {
        WebElement addButton = waitForVisible(addMissionButton);
        return !addButton.isEnabled();
    }

    public void closeMissionInput() {
        click(closeMissionButton);
    }

    public boolean isMissionAdded(String missionText) {
        By missionItem = By.xpath("//span[normalize-space()='" + missionText + "']");
        return isDisplayed(missionItem);
    }

    public String getMissionProgress() {
        return waitForVisible(missionProgress).getText();
    }

    public void toggleMissionCheckbox(String missionText) {
        By checkbox = By
                .xpath("//span[normalize-space()='" + missionText + "']/preceding-sibling::input[@type='checkbox']");
        click(checkbox);
    }
}
