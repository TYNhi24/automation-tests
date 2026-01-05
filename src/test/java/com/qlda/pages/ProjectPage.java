package com.qlda.pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qlda.core.BasePage;
import com.qlda.utils.WebDriverConfig;

public class ProjectPage extends BasePage {

    private final By projectHeader = By.cssSelector("h1.text-3xl.font-bold.text-gray-900");
    private final By projectNameElements = By.cssSelector("h3.text-lg.font-semibold.text-gray-900");
    private final By projectNameField = By.name("project_name");
    private final By projectDescField = By.name("description");
    private final By searchField = By.xpath("//input[@placeholder='Tìm kiếm dự án...']");
    private final By createButton = By.xpath("//button[contains(.,'Tạo dự án mới')]");
    private final By editButton = By.xpath("//button[contains(text(),'Sửa')]");
    private final By deleteButton = By.xpath("//button[normalize-space()='Xóa']");
    private final By submitButton = By.cssSelector("form button[type='submit']");
    private final By confirmDeleteButton = By.xpath("//button[contains(text(),'Xác nhận xóa')]");
    private final By cancelButton = By.xpath("//button[contains(.,'Hủy')]");
    private final By viewButton = By.xpath("//button[normalize-space()='Xem']");

    private final By inviteButton = By.xpath("//span[contains(text(),'Chia sẻ')]");
    private final By inviteEmailField = By.xpath("//input[@placeholder='Nhập email hoặc tên thành viên...']");
    private final By inviteButtonSubmit = By.xpath("//button[normalize-space()='+ Thêm']");
    private final By closeInvitePopupButton = By.xpath("//button[@aria-label='Đóng']//*[name()='svg']");

    private final By profileMenuButton = By.xpath("//img[@alt='avatar']");
    private final By logoutButton = By.xpath("//button[contains(text(),'Đăng xuất')]");

    public ProjectPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public ProjectPage waitForPageLoad() {
        wait.until(ExpectedConditions.presenceOfElementLocated(projectHeader));
        return this;
    }

    public void goToProject(String projectId) {
        driver.get(WebDriverConfig.getBaseUrl() + "/projects/" + projectId);
    }

    public void waitForProjectName(String projectName) {
        wait.until(localDriver -> getProjectNames().contains(projectName));
    }

    public void clickViewButton() {
        click(viewButton);
    }

    public List<String> getProjectNames() {
        int attempts = 0;
        while (attempts < 3) {
            try {
                List<WebElement> elements = driver.findElements(projectNameElements);
                return elements.stream()
                        .map(WebElement::getText)
                        .collect(Collectors.toList());
            } catch (StaleElementReferenceException e) {
                attempts++;
            }
        }
        // Fallback: return empty list or throw
        return driver.findElements(projectNameElements).stream()
                .map(e -> {
                    try {
                        return e.getText();
                    } catch (Exception ex) {
                        return "";
                    }
                })
                .collect(Collectors.toList());
    }

    public ProjectPage enterProjectName(String name) {
        type(projectNameField, name);
        return this;
    }

    public ProjectPage enterProjectDescription(String description) {
        type(projectDescField, description);
        return this;
    }

    public ProjectPage fillProjectForm(String name, String description) {
        enterProjectName(name);
        enterProjectDescription(description);
        return this;
    }

    public void searchProject(String keyword) {
        WebElement searchInput = wait.until(ExpectedConditions.presenceOfElementLocated(searchField));
        searchInput.clear();
        searchInput.sendKeys(keyword);
        searchInput.sendKeys(Keys.ENTER);
        waitForPageLoad();
    }

    public void clickCreateButton() {
        click(createButton);
    }

    public void clickEditButton() {
        click(editButton);
    }

    public void clickDeleteButton() {
        click(deleteButton);
    }

    public void clickSubmitButton() {
        click(submitButton);
    }

    public void clickConfirmDeleteButton() {
        click(confirmDeleteButton);
    }

    public void clickCancelButton() {
        click(cancelButton);
    }

    public void clickInviteButton() {
        click(inviteButton);
    }

    public ProjectPage enterInviteEmail(String email) {
        type(inviteEmailField, email);
        return this;
    }

    public void clickInviteSubmitButton() {
        click(inviteButtonSubmit);
    }

    public void closeInvitePopup() {
        click(closeInvitePopupButton);
    }

    public boolean isInviteSuccessMessageDisplayed(String email) {
        By successMessage = By.xpath("//p[normalize-space()='" + email + "']");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void logout() {
        click(profileMenuButton);
        click(logoutButton);
    }

}
