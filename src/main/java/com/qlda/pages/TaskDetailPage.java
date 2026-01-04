package com.qlda.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
    private By commnentsSection = By.xpath("//div[@class='h-full']");
    private By commentInput = By.xpath("//textarea[@placeholder='Viết bình luận...']");
    private By postButton = By.xpath("//button[contains(text(),'Lưu')]");
    private By CommentTimes = By.xpath("(//span[@class='text-xs text-gray-400'])[1]");
    private By commentList = By.xpath("//div[contains(@class, 'whitespace-pre-wrap')]");
            

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

    // Kiểm tra sự tồn tại của phần Bình luận
    public boolean isCommentsSectionDisplayed() {
        return driver.findElement(commnentsSection).isDisplayed();
    }

    public void writeAndSaveComment(String comment) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(commentInput)).sendKeys(comment);
        wait.until(ExpectedConditions.elementToBeClickable(postButton)).click();
    }

    // public boolean isCommentsDisplayed(String comment) {
    //     try {
    //         String commnentXPath = "//div[contains(text(),'" + comment + "')]";
    //         return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(commnentXPath))).isDisplayed();
    //     } catch (Exception e) {
    //         System.err.println("Không tìm thấy bình luận vừa đăng: " + comment);
    //         return false;
    //     }
    // }

    public boolean isCommentsDisplayed(String comment) {
        try {
            // 1. Đợi cho danh sách bình luận xuất hiện
            wait.until(ExpectedConditions.presenceOfElementLocated(commentList));         
            // 2. Lấy tất cả các thẻ div chứa nội dung bình luận
            List<WebElement> commentElements = driver.findElements(commentList);         
            // 3. Duyệt qua danh sách để so sánh bằng Java (Tránh lỗi XPath với ký tự đặc biệt)
            for (WebElement element : commentElements) {
                if (element.getText().trim().equals(comment)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.err.println("Lỗi khi tìm bình luận: " + e.getMessage());
            return false;
        }
    }

    public void writeComment(String comment) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(commentInput)).sendKeys(comment);
    }

    public boolean isSaveButtonDisplayed() {
        try {
            return driver.findElement(postButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> getAllComments() {
        By commentElements = By.xpath("//div[@class='flex gap-3 group']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(commentElements));      
        List<WebElement> elements = driver.findElements(commentElements);
        List<String> comments = new ArrayList<>();      
        for (WebElement element : elements) {
            comments.add(element.getText().trim());
        }
        return comments;
    }
    
    public String getLatestCommentTimestamp() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(CommentTimes)).getText().trim();
    }

    public void writeCommentWithEmoji(String comment) {
        // 1. Chờ ô nhập liệu xuất hiện
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(commentInput));       
        // 2. Sử dụng Script chuyên sâu cho React để cập nhật giá trị và kích hoạt sự kiện
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script = 
            "var element = arguments[0];" +
            "var value = arguments[1];" +
            "var lastValue = element.value;" +
            "element.value = value;" +
            // Đánh lừa trình theo dõi giá trị của React (React 16+)
            "var tracker = element._valueTracker;" +
            "if (tracker) { tracker.setValue(lastValue); }" +
            // Kích hoạt đồng thời các sự kiện để nút "Lưu" nhận diện được text
            "element.dispatchEvent(new Event('input', { bubbles: true }));" +
            "element.dispatchEvent(new Event('change', { bubbles: true }));";
        
        js.executeScript(script, element, comment);
        
        // 3. Click nút Lưu sau khi nó đã được kích hoạt hiển thị
        wait.until(ExpectedConditions.elementToBeClickable(postButton)).click(); 
    }

}

