package com.qlda.tests;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.qlda.core.BaseTest;
import com.qlda.pages.ChatPage;
import com.qlda.pages.LoginPage;
import com.qlda.pages.ProjectPage;
import com.qlda.utils.MockUtils;
import com.qlda.utils.WebDriverConfig;

public class ChatTest extends BaseTest {
    private LoginPage loginPage;
    private ProjectPage projectPage;
    private ChatPage chatPage;

    @BeforeMethod
    @Override
    public void setUp() throws IOException {
        // Tạo 3 user
        MockUtils.mockUser("owner@gmail.com", "Password12345", "Owner User", false);
        MockUtils.mockUser("member1@gmail.com", "Password12345", "Member One", false);
        MockUtils.mockUser("member2@gmail.com", "Password12345", "Member Two", false);

        super.setUp();
        driver.get(WebDriverConfig.getBaseUrl() + "/login");
        loginPage = new LoginPage(driver, wait);
        projectPage = new ProjectPage(driver, wait);
        chatPage = new ChatPage(driver, wait);

        // Đăng nhập owner
        loginPage.loginUser("owner@gmail.com", "Password12345");
        projectPage.waitForPageLoad();

        // Tạo project
        String projectName = "Test Project";
        String projectDesc = "Project for chat test";
        projectPage.clickCreateButton();
        projectPage.fillProjectForm(projectName, projectDesc);
        projectPage.clickSubmitButton();
        projectPage.waitForProjectName(projectName);
        projectPage.clickViewButton();

        // Mời member1 và member2
        projectPage.clickInviteButton();
        projectPage.enterInviteEmail("member1@gmail.com");
        projectPage.clickInviteSubmitButton();
        projectPage.closeInvitePopup();

        projectPage.clickInviteButton();
        projectPage.enterInviteEmail("member2@gmail.com");
        projectPage.clickInviteSubmitButton();
        projectPage.closeInvitePopup();
    }

    /**
     * Kiểm tra nhóm chat chung được tạo tự động khi tạo dự án
     */
    @Test
    public void testAutoCreateGroupChatWhenCreateProject() {
        chatPage.openChatTab();
        // Kiểm tra có group chat với tên project
        Assert.assertTrue(chatPage.isGroupChatDisplayed("Test Project"),
                "Nhóm chat chung phải được tạo khi tạo dự án");
    }

    /**
     * Kiểm tra phòng chat riêng được tạo tự động giữa các thành viên và chủ dự án
     * @throws Exception
     */
    @Test
    public void testAutoCreateDirectChatBetweenMemberAndOwner() throws Exception {
        chatPage.openChatTab();
        chatPage.openGroupChat("Test Project");
        Assert.assertTrue(chatPage.isMemberChatDisplayed("Member One"), "Member One phải là thành viên nhóm chung");
        Assert.assertTrue(chatPage.isMemberChatDisplayed("Member Two"), "Member Two phải là thành viên nhóm chung");
    }
}