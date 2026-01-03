package com.qlda.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qlda.core.BasePage;

public class TaskDetailPage extends BasePage{
    public TaskDetailPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    private By addMemberButton = By.xpath("//button[@class='w-8 h-8 rounded-full bg-gray-100 hover:bg-gray-200 flex items-center justify-center text-gray-600 transition-colors border border-gray-200']");
    private By memberPopup = By.xpath("//h4[normalize-space()='Thành viên']");
    private By memberNameElements = By.xpath("//p[@class='text-sm font-medium text-gray-800 truncate']");

    public void clickAddMember() {
        wait.until(ExpectedConditions.elementToBeClickable(addMemberButton)).click();
    }

    public boolean isAddMemberButtonDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(addMemberButton)).isDisplayed();
        } catch (Exception e) {
            System.err.println("Không tìm thấy nút Thêm thành viên (+)");
            return false;
        }
    }

    public boolean isMemberPopupDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(memberPopup)).isDisplayed();
        } catch (Exception e) {
            System.err.println("Không tìm thấy popup thành viên");
            return false;
        }
    }

    public List<String> getAllMemberNamesInPopup() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(memberNameElements));      
        List<WebElement> elements = driver.findElements(memberNameElements);
        List<String> names = new ArrayList<>();      
        for (WebElement element : elements) {
            names.add(element.getText().trim());
        }
        return names;
    }

    // Chọn một thành viên từ popup dựa vào tên
    public void selectMemberByName(String name) {
        List<WebElement> elements = driver.findElements(memberNameElements);
        for (WebElement element : elements) {
            if (element.getText().trim().equals(name)) {
                element.click();
                return; 
            }
        }
        System.err.println("Không tìm thấy thành viên có tên: " + name);
    }

    // Kiểm tra thành viên có hiển thị trong chi tiết task không
    public boolean isMemberAvatarDisplayed(String name) {
        try {
            String avatarXPath = "//div[@title='" + name + "']";
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(avatarXPath))).isDisplayed();
        } catch (Exception e) {
            System.err.println("Không tìm thấy thành viên được gán task: " + name);
            return false;
        }
    }
    
}

