package com.qlda.pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qlda.core.BasePage;

public class ProjectPage extends BasePage {
    private final By projectHeader = By.cssSelector("h1.text-3xl.font-bold.text-gray-900");
    private final By projectNameElements = By.cssSelector("h3.text-lg.font-semibold.text-gray-900");
    private final By projectNameField = By.name("project_name");
    private final By projectDescField = By.name("description");
    private final By createButton = By.xpath("//button[contains(.,'Tạo dự án mới')]");
    private final By editButton = By.xpath("//button[contains(text(),'Sửa')]");
    private final By deleteButton = By.xpath("//button[contains(text(),'Xóa')]");
    private final By submitButton = By.cssSelector("form button[type='submit']");
    private final By confirmDeleteButton = By.xpath("//button[contains(text(),'Xác nhận xóa')]");
    private final By cancelButton = By.xpath("//button[contains(.,'Hủy')]");

    public ProjectPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public ProjectPage waitForPageLoad() {
        wait.until(ExpectedConditions.presenceOfElementLocated(projectHeader));
        return this;
    }

    public void waitForProjectName(String projectName) {
        wait.until(driver -> getProjectNames().contains(projectName));
    }

    public List<String> getProjectNames() {
        List<WebElement> elements = driver.findElements(projectNameElements);
        return elements.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public ProjectPage enterProjectName(String name) {
        waitAndSendKeys(projectNameField, name);
        return this;
    }

    public ProjectPage enterProjectDescription(String description) {
        waitAndSendKeys(projectDescField, description);
        return this;
    }

    public ProjectPage fillProjectForm(String name, String description) {
        enterProjectName(name);
        enterProjectDescription(description);
        return this;
    }

    public void clickCreateButton() {
        waitAndClick(createButton);
    }

    public void clickEditButton() {
        waitAndClick(editButton);
    }

    public void clickDeleteButton() {
        waitAndClick(deleteButton);
    }

    public void clickSubmitButton() {
        waitAndClick(submitButton);
    }

    public void clickConfirmDeleteButton() {
        waitAndClick(confirmDeleteButton);
    }

    public void clickCancelButton() {
        waitAndClick(cancelButton);
    }

}
