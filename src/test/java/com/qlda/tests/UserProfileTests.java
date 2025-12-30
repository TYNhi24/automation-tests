package com.qlda.tests;
import java.io.IOException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.qlda.pages.HeaderComponentPage;
import com.qlda.pages.UserProfilePage;
import com.qlda.utils.DatabaseUtils;
import com.qlda.utils.MockUtils;
import com.qlda.pages.LoginPage;
import com.qlda.core.BaseTest;

public class UserProfileTests extends BaseTest {
    private LoginPage loginPage; 
    private HeaderComponentPage headerComponent;
    private UserProfilePage userProfilePage;
    private String userId;
    @BeforeMethod
    public void init() throws IOException {  
        DatabaseUtils.clearAllTables();
        userId = MockUtils.mockUser("user@gmail.com", "User12", "Nhi");
        
        loginPage = new LoginPage(driver, wait); 
        loginPage.login("user@gmail.com", "User12");     
        headerComponent = new HeaderComponentPage(driver, wait);
        userProfilePage = headerComponent.navigateToProfilePage();
    }

    @Test
    public void verifyUserIsLoggedIn() {
        String expectedName = "Nhi";        
        Assert.assertTrue(userProfilePage.isUserNameDisplayed(expectedName), 
                        "Tên người dùng " + expectedName + " không hiển thị!"
        );
    }

    @Test
    public void verifyEmailIsLoggedIn() {
        String expectedEmail = "user@gmail.com";        
        Assert.assertTrue(userProfilePage.isEmailDisplayed(expectedEmail), 
                        "Email " + expectedEmail + " không hiển thị!"
        );
    }

    @Test
    public void verifyCreatedProjectNo() {
        String expected = "Bạn chưa tạo dự án nào.";        
        Assert.assertTrue(userProfilePage.isNotCreatedProjectNDisplayed(expected), 
                        "Tồn tại dự án đã tạo!"
        );
    }

    @Test
    public void verifyCreatedProjectIs() {
        MockUtils.mockProject("Dự án A", userId);
        String expected = "Dự án A";     
        driver.navigate().refresh();   
        Assert.assertTrue(userProfilePage.isCreatedProjectDisplayed(expected), 
                        "Không tồn tại dự án: " + expected
        );
    }
}

