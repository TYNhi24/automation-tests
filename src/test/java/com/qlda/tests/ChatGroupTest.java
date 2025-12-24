package com.qlda.tests;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.qlda.core.BaseTest;
import com.qlda.pages.ChatPage;
import com.qlda.pages.LoginPage;
import com.qlda.pages.ProjectPage;
import com.qlda.utils.MockUtils;
import com.qlda.utils.WebDriverConfig;

public class ChatGroupTest extends BaseTest {
    private LoginPage loginPage;
    private ProjectPage projectPage;
    private ChatPage chatPage;

    @BeforeMethod
    @Override
    public void setUp() throws IOException {
        MockUtils.mockUser("test@gmail.com", "Password12345", "Valid User", false);
        MockUtils.mockUser("user2@gmail.com", "Password12345", "Member User", false);
        super.setUp();
        driver.get(WebDriverConfig.getBaseUrl() + "/login");
        loginPage = new LoginPage(driver, wait);
        projectPage = new ProjectPage(driver, wait);
        chatPage = new ChatPage(driver, wait);
        loginPage.loginUser("test@gmail.com", "Password12345");
        projectPage.waitForPageLoad();

        String projectName = "Test Project";
        String projectDesc = "This is a test project.";
        projectPage.clickCreateButton();
        projectPage.fillProjectForm(projectName, projectDesc);
        projectPage.clickSubmitButton();
        projectPage.waitForProjectName(projectName);
        projectPage.clickViewButton();
    }

    /**
     * Kiểm tra tab chat hiển thị đúng
     */
    @Test
    public void testChatTabIsDisplayed() {
        chatPage.openChatTab();
        chatPage.openGroupChat("Test Project");
        Assert.assertTrue(chatPage.isChatInputDisplayed(), "Chat input should be displayed");
    }

    /**
     * Kiểm tra nút gửi tin nhắn bị vô hiệu hóa khi input rỗng
     */
    @Test
    public void testSendButtonDisabledWhenInputEmpty() {
        chatPage.openChatTab();
        chatPage.openGroupChat("Test Project");
        chatPage.waitForPageLoad();
        Assert.assertTrue(chatPage.isSendButtonDisabled(), "Send button should be disabled when input is empty");
    }

    /**
     * Kiểm tra nút gửi tin nhắn bị vô hiệu hóa khi input chỉ chứa khoảng trắng
     */
    @Test
    public void testSendButtonDisabledWhenInputWhitespace() {
        chatPage.openChatTab();
        chatPage.openGroupChat("Test Project");
        chatPage.waitForPageLoad();
        chatPage.typeMessage("    ");
        Assert.assertTrue(chatPage.isSendButtonDisabled(),
                "Send button should be disabled when input is only whitespace");
    }

    /**
     * Kiểm tra gửi tin nhắn với tin nhắn hợp lệ
     */
    @Test
    public void testSendMessageWithValidInput() throws InterruptedException {
        chatPage.openChatTab();
        chatPage.openGroupChat("Test Project");
        chatPage.waitForPageLoad();
        chatPage.typeMessage("Hello, this is a test message.");
        chatPage.clickSendButton();
        Thread.sleep(1000);
        Assert.assertTrue(chatPage.isMyMessageDisplayed("Hello, this is a test message."),
                "Sent message should be displayed in chat");
    }

    /**
     * Kiểm tra gửi tin nhắn với tin nhắn có độ dài tối thiểu
     * @throws InterruptedException
     */
    @Test
    public void testSendMessageWithMinLength() throws InterruptedException {
        chatPage.openChatTab();
        chatPage.openGroupChat("Test Project");
        chatPage.waitForPageLoad();
        String minMessage = "a";
        chatPage.typeMessage(minMessage);
        chatPage.clickSendButton();
        Thread.sleep(1000);
        Assert.assertTrue(chatPage.isMyMessageDisplayed(minMessage), "Sent message should be displayed in chat");
    }

    /**
     * Kiểm tra gửi tin nhắn với tin nhắn có độ dài tối đa
     * @throws InterruptedException
     */
    @Test
    public void testSendMessageWithMaxLength() throws InterruptedException {
        chatPage.openChatTab();
        chatPage.openGroupChat("Test Project");
        chatPage.waitForPageLoad();
        String maxMessage = "a".repeat(500);
        chatPage.typeMessage(maxMessage);
        chatPage.clickSendButton();
        Thread.sleep(1000);
        Assert.assertTrue(chatPage.isMyMessageDisplayed(maxMessage), "Sent message should be displayed in chat");
    }

    /**
     * Kiểm tra gửi tin nhắn với ký tự đặc biệt
     * @throws InterruptedException
     */
    @Test
    public void testSendMessageWithSpecialCharacters() throws InterruptedException {
        chatPage.openChatTab();
        chatPage.openGroupChat("Test Project");
        chatPage.waitForPageLoad();
        String specialMessage = "!@#$%^&*()_+-=[]{}|;,.<>/?";
        chatPage.typeMessage(specialMessage);
        chatPage.clickSendButton();
        Thread.sleep(1000);
        Assert.assertTrue(chatPage.isMyMessageDisplayed(specialMessage),
                "Special character message should be displayed in chat");
    }

    /**
     * Kiểm tra gửi tin nhắn với ký tự tiếng Việt có dấu
     * @throws InterruptedException
     */
    @Test
    public void testSendMessageWithVietnameseCharacters() throws InterruptedException {
        chatPage.openChatTab();
        chatPage.openGroupChat("Test Project");
        chatPage.waitForPageLoad();
        String vietnameseMessage = "Xin chào, đây là tin nhắn có dấu!";
        chatPage.typeMessage(vietnameseMessage);
        chatPage.clickSendButton();
        Thread.sleep(1000);
        Assert.assertTrue(chatPage.isMyMessageDisplayed(vietnameseMessage),
                "Vietnamese message should be displayed in chat");
    }

    /**
     * Kiểm tra gửi tin nhắn với ký tự Unicode
     */
    @Test
    public void testSendMessageWithUnicodeCharacters() throws InterruptedException {
        chatPage.openChatTab();
        chatPage.openGroupChat("Test Project");
        chatPage.waitForPageLoad();
        String message = "你好 こんにちは 안녕하세요";
        chatPage.typeMessage(message);
        chatPage.clickSendButton();
        Thread.sleep(1000);
        Assert.assertTrue(chatPage.isMyMessageDisplayed(message), "Unicode message should be displayed in chat");
    }

    /**
     * Kiểm tra gửi tin nhắn với emoji
     */
    @Test
    public void testSendMessageWithEmoji() throws InterruptedException {
        chatPage.openChatTab();
        chatPage.openGroupChat("Test Project");
        chatPage.waitForPageLoad();
        String emojiMessage = "Hello ☺★";
        chatPage.typeMessage(emojiMessage);
        chatPage.clickSendButton();
        Thread.sleep(1000);
        Assert.assertTrue(chatPage.isMyMessageDisplayed(emojiMessage), "Emoji message should be displayed in chat");
    }

    /**
     * Kiểm tra gửi tin nhắn với thẻ HTML
     * @throws InterruptedException
     */
    @Test
    public void testSendMessageWithHtmlTags() throws InterruptedException {
        chatPage.openChatTab();
        chatPage.openGroupChat("Test Project");
        chatPage.waitForPageLoad();
        String htmlMessage = "<b>Bold</b> <script>alert('xss')</script>";
        chatPage.typeMessage(htmlMessage);
        chatPage.clickSendButton();
        Thread.sleep(1000);
        Assert.assertTrue(chatPage.isMyMessageDisplayed(htmlMessage), "HTML tag message should be displayed in chat");
    }

    /**
     * Kiểm tra gửi tin nhắn với khoảng trắng ở đầu và cuối
     * @throws InterruptedException
     */
    @Test
    public void testSendMessageWithLeadingAndTrailingSpaces() throws InterruptedException {
        chatPage.openChatTab();
        chatPage.openGroupChat("Test Project");
        chatPage.waitForPageLoad();
        String spacedMessage = "   Hello with spaces   ";
        chatPage.typeMessage(spacedMessage);
        chatPage.clickSendButton();
        Thread.sleep(1000);
        Assert.assertTrue(chatPage.isMyMessageDisplayed(spacedMessage),
                "Message with leading and trailing spaces should be displayed in chat");
    }

    /**
     * Kiểm tra tin nhắn realtime trong nhóm chat
     */
    @Test
    public void testGroupChatRealtimeDisplay() throws Exception {
        // User1 mời User2 vào project
        projectPage.clickInviteButton();
        projectPage.enterInviteEmail("user2@gmail.com");
        projectPage.clickInviteSubmitButton();
        projectPage.closeInvitePopup();
        String projectUrl = driver.getCurrentUrl();

        projectPage.logout();
        loginPage.loginUser("test@gmail.com", "Password12345");
        projectPage.waitForPageLoad();
        driver.get(projectUrl);

        chatPage.openChatTab();
        chatPage.openGroupChat("Test Project");
        String realtimeMessage = "Tin nhắn realtime";
        chatPage.typeMessage(realtimeMessage);
        chatPage.clickSendButton();

        // Tạo driver/session mới cho User2
        WebDriver driver2 = WebDriverConfig.createDriver();
        WebDriverWait wait2 = new WebDriverWait(driver2, Duration.ofSeconds(10));
        driver2.get(WebDriverConfig.getBaseUrl() + "/login");
        LoginPage loginPage2 = new LoginPage(driver2, wait2);
        loginPage2.loginUser("user2@gmail.com", "Password12345");
        ProjectPage projectPage2 = new ProjectPage(driver2, wait2);
        projectPage2.waitForPageLoad();
        driver2.get(projectUrl);
        ChatPage chatPage2 = new ChatPage(driver2, wait2);
        chatPage2.openChatTab();
        chatPage2.openGroupChat("Test Project");

        // User2 kiểm tra tin nhắn xuất hiện ngay lập tức
        boolean received = false;
        for (int i = 0; i < 10; i++) { // chờ tối đa 5 giây
            if (chatPage2.isOtherMessageDisplayed(realtimeMessage)) {
                received = true;
                break;
            }
            Thread.sleep(500);
        }
        Assert.assertTrue(received, "User2 phải thấy tin nhắn realtime từ User1");

        // Đóng driver
        driver2.quit();
    }

    /**
     * Kiểm tra lịch sử tin nhắn trong nhóm chat sau khi đăng nhập lại
     * @throws Exception
     */
    @Test
    public void testGroupChatHistoryAfterRelogin() throws Exception {
        projectPage.clickInviteButton();
        projectPage.enterInviteEmail("user2@gmail.com");
        projectPage.clickInviteSubmitButton();
        projectPage.closeInvitePopup();
        String projectUrl = driver.getCurrentUrl();
        
        projectPage.logout();
        loginPage.loginUser("test@gmail.com", "Password12345");
        projectPage.waitForPageLoad();
        driver.get(projectUrl);
        
        chatPage.openChatTab();
        chatPage.openGroupChat("Test Project");
        String message = "Tin nhắn kiểm tra lịch sử chat";
        chatPage.typeMessage(message);
        chatPage.clickSendButton();
        Thread.sleep(1000);
        Assert.assertTrue(chatPage.isMyMessageDisplayed(message), "Tin nhắn vừa gửi phải hiển thị");

        // Đăng xuất
        projectPage.logout();
        loginPage.loginUser("test@gmail.com", "Password12345");
        projectPage.waitForPageLoad();
        projectPage.clickViewButton();
        chatPage.openChatTab();
        chatPage.openGroupChat("Test Project");

        // Kiểm tra tin nhắn cũ vẫn còn trong lịch sử
        Assert.assertTrue(chatPage.isMyMessageDisplayed(message),
                "Tin nhắn phải còn trong lịch sử chat sau khi đăng nhập lại");
    }

}
