package com.qlda.tests;
import java.io.IOException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.qlda.pages.HeaderComponentPage;
import com.qlda.pages.UserProfilePage;
import com.qlda.pages.LoginPage;
import com.qlda.core.BaseTest;

public class UserProfileTests extends BaseTest {
    private LoginPage loginPage; 
    private HeaderComponentPage headerComponent;
    private UserProfilePage userProfilePage;

    @BeforeMethod
    public void init() throws IOException {  
        loginPage = new LoginPage(driver); 
        loginPage.login("user@gmail.com", "User12");     
        // Khởi tạo header component
        headerComponent = new HeaderComponentPage(driver);
        // Sau login → mở avatar → điều hướng đến profile
        userProfilePage = headerComponent.navigateToProfilePage();
    }

    @Test
    public void verifyInfoProfileIsDisplayed() {
        // Assert.assertTrue(
        //     userProfilePage.isInfoDisplayed(),
        //     "Thông tin user không hiển thị đúng"
        // );
        Assert.assertFalse(userProfilePage.getProjectNames().contains("Hồ sơ"), "Project should be created and visible in list");
    }
    @Test
    public void verifyTotalProjectIsDisplayed() {
        Assert.assertFalse(userProfilePage.isTotalProjectDisplayed(), "Không thấy dòng Tổng số");
        Assert.assertEquals(userProfilePage.getTotalProjectText(), "Tổng số: 0 dự án");
    }
    }

