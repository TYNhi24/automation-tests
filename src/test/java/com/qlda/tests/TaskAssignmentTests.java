package com.qlda.tests;

import java.util.List;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.qlda.core.BaseTest;
import com.qlda.pages.LoginPage;
import com.qlda.pages.ProjectDetailPage;
import com.qlda.pages.TaskDetailPage;
import com.qlda.utils.DatabaseUtils;
import com.qlda.utils.MockUtils;

public class TaskAssignmentTests extends BaseTest {
    private LoginPage loginPage;
    private ProjectDetailPage projectDetailPage;

    @BeforeMethod
    public void setupAssignmentData() {

        DatabaseUtils.clearAllTables();

        String ownerId = MockUtils.mockUser("nhu@gmail.com", "nhu152", "YNhi");
        
        String memberAId = MockUtils.mockUser("memberA@gmail.com", "memberA", "Thành viên A");
        String memberBId = MockUtils.mockUser("memberB@gmail.com", "memberB", "Thành viên B");
 
        String projectId = MockUtils.mockProject("Dự án Giao việc", ownerId);
        MockUtils.mockProjectMember(projectId, ownerId, "owner");
        MockUtils.mockProjectMember(projectId, memberAId, "member"); 
        MockUtils.mockProjectMember(projectId, memberBId, "member"); 
        //Tạo List và Task
        String listId = MockUtils.mockList(projectId, "Việc cần làm", 0);
        String taskId = MockUtils.mockTask(listId, "Nhiệm vụ kiểm thử");

        loginPage = new LoginPage(driver, wait);
        loginPage.login("nhu@gmail.com", "nhu152");

        projectDetailPage = new ProjectDetailPage(driver, wait);
        wait.until(ExpectedConditions.urlContains("projects"));
        projectDetailPage.clickView();

    }

    @Test(priority = 1, description = "Kiểm tra hiển thị button Add Member")
    public void verifyAddMemberButtonVisibility() {
        projectDetailPage.openTaskDetails("Nhiệm vụ kiểm thử");
        TaskDetailPage taskDetailPage = new TaskDetailPage(driver, wait);
        boolean isDisplayed = taskDetailPage.isAddMemberButtonDisplayed();
        Assert.assertTrue(isDisplayed, "Nút 'Thêm thành viên' (+) không hiển thị.");
    }

    @Test(priority = 2, description = "Kiểm tra hiển thị popup members")
    public void verifyPopupMembersVisibility() {
        projectDetailPage.openTaskDetails("Nhiệm vụ kiểm thử");
        TaskDetailPage taskDetailPage = new TaskDetailPage(driver, wait);
        // Thêm bước đợi để chắc chắn Modal đã load xong phần 'THÀNH VIÊN'
        Assert.assertTrue(taskDetailPage.isAddMemberButtonDisplayed(), "Nút (+) chưa xuất hiện trong Modal.");
        taskDetailPage.clickAddMember();
        boolean isDisplayed = taskDetailPage.isMemberPopupDisplayed();
        Assert.assertTrue(isDisplayed, "Popup thành viên không hiển thị.");
    }

    @Test(priority = 3, description = "Kiểm tra danh sách thành viên hiển thị đầy đủ và chính xác")
    public void verifyFullMemberListContent() {
        projectDetailPage.openTaskDetails("Nhiệm vụ kiểm thử");
        TaskDetailPage taskDetailPage = new TaskDetailPage(driver, wait);
        taskDetailPage.clickAddMember();

        List<String> actualNames = taskDetailPage.getAllMemberNamesInPopup();
        System.out.println("Thành viên tìm thấy trên UI: " + actualNames);

        Assert.assertTrue(actualNames.contains("YNhi"), "Thiếu chủ dự án 'YNhi'!");
        Assert.assertTrue(actualNames.contains("Thành viên A"), "Thiếu 'Thành viên A'!");
        Assert.assertTrue(actualNames.contains("Thành viên B"), "Thiếu 'Thành viên B'!");
        
        Assert.assertEquals(actualNames.size(), 3, "Số lượng thành viên hiển thị không đúng!");
    }

    @Test(priority = 4, description = "Kiểm tra gán thành viên thành công")
    public void verifyMemberAssignment() {
        projectDetailPage.openTaskDetails("Nhiệm vụ kiểm thử");
        TaskDetailPage taskDetailPage = new TaskDetailPage(driver, wait);
        taskDetailPage.clickAddMember();
        
        taskDetailPage.selectMemberByName("Thành viên A");
        
        boolean isAvatarVisible = taskDetailPage.isMemberAvatarDisplayed("Thành viên A");

        Assert.assertTrue(isAvatarVisible, "Lỗi:  Không hiển thị thành viên sau khi gán!");
    }

    @Test(priority = 5, description = "Kiểm tra gán được nhiều thành viên cho một task")
    public void verifyMemberAssignmentAndAvatarDisplay() {
        projectDetailPage.openTaskDetails("Nhiệm vụ kiểm thử");
        TaskDetailPage taskDetailPage = new TaskDetailPage(driver, wait);

        taskDetailPage.clickAddMember();
        taskDetailPage.selectMemberByName("Thành viên A");

        taskDetailPage.clickAddMember();
        taskDetailPage.selectMemberByName("Thành viên B");
        
        boolean isAvatarVisible1 = taskDetailPage.isMemberAvatarDisplayed("Thành viên A");
        boolean isAvatarVisible2 = taskDetailPage.isMemberAvatarDisplayed("Thành viên B");

        Assert.assertTrue(isAvatarVisible1, "Lỗi: Không hiển thị thành viên sau khi gán!");
        Assert.assertTrue(isAvatarVisible2, "Lỗi: Không hiển thị thành viên sau khi gán!");
    }

    @Test(priority = 6, description = "Kiểm tra thành viên đã được gán sẽ không hiển thị trong danh sách lựa chọn lại")
    public void verifyAssignedMemberIsExcludedFromSelectionList() {
        projectDetailPage.openTaskDetails("Nhiệm vụ kiểm thử");
        TaskDetailPage taskDetailPage = new TaskDetailPage(driver, wait);

        taskDetailPage.clickAddMember();
        taskDetailPage.selectMemberByName("Thành viên A");

        Assert.assertTrue(taskDetailPage.isMemberAvatarDisplayed("Thành viên A"), 
        "Lỗi: Database đã gán nhưng UI chưa hiển thị Thành viên A!");

        taskDetailPage.clickAddMember();
        Assert.assertTrue(taskDetailPage.isMemberPopupDisplayed(), "Popup thành viên không hiển thị.");

        List<String> availableMembers = taskDetailPage.getAllMemberNamesInPopup();
        System.out.println("Danh sách thành viên còn lại trên UI: " + availableMembers);

        // Kiểm tra Thành viên A không còn trong danh sách lựa chọn
        Assert.assertFalse(availableMembers.contains("Thành viên A"),"Lỗi: 'Thành viên A' đã được gán nhưng vẫn xuất hiện trong danh sách lựa chọn!");      
        Assert.assertTrue(availableMembers.contains("Thành viên B"),"Lỗi: 'Thành viên B' chưa được gán nhưng lại không tìm thấy trong danh sách!");
    }
}
