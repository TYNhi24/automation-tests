package com.qlda.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qlda.core.BasePage;

public class ChatPage extends BasePage {
    private final By chat = By.xpath("//span[@class='material-icons text-2xl']");
    private final By chatInput = By.xpath("//textarea[@placeholder='Nhập tin nhắn của bạn...']");
    private final By chatSubmit = By.xpath("//button[@title='Gửi tin nhắn']");
    private final By myMessage = By.xpath(
            "//div[@class='bg-gradient-to-r from-blue-600 to-blue-700 text-white px-4 py-3 rounded-2xl rounded-tr-md shadow-md']");

    public ChatPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void openChatTab() {
        click(chat);
    }

    public ChatPage waitForPageLoad() {
        waitForVisible(chatInput);
        return this;
    }

    public void openGroupChat(String groupName) {
        By groupChat = By.xpath("//h3[normalize-space()='" + groupName + " - Group Chat']");
        click(groupChat);
    }

    public void openMemberChat(String memberName) {
        By memberChat = By.xpath("//h3[normalize-space()='" + memberName + "']");
        click(memberChat);
    }

    public boolean isChatInputDisplayed() {
        return isDisplayed(chatInput);
    }

    public boolean isSendButtonDisabled() {
        return !driver.findElement(chatSubmit).isEnabled();
    }

    public void typeMessage(String message) {
        type(chatInput, message);
    }

    public void clickSendButton() {
        click(chatSubmit);
    }

    public boolean isMyMessageDisplayed(String message) {
        By messageLocator = By.xpath("//p[@class='text-sm leading-relaxed break-words' and text()=\"" + message + "\"]");
        return isDisplayed(messageLocator);
    }

    public boolean isOtherMessageDisplayed(String message) {
        By messageLocator = By.xpath(
                "//p[@class='text-sm text-gray-800 leading-relaxed wrap-break-word' and text()='" + message + "']");
        return isDisplayed(messageLocator);
    }

    // Kiểm tra có group chat với tên project
    public boolean isGroupChatDisplayed(String groupName) {
        By groupChat = By.xpath("//h3[normalize-space()='" + groupName + " - Group Chat']");
        return isDisplayed(groupChat);
    }

    // Kiểm tra có phòng chat riêng với member
    public boolean isMemberChatDisplayed(String memberName) {
        By memberChat = By.xpath("//h3[normalize-space()='" + memberName + "']");
        return isDisplayed(memberChat);
    }

}
