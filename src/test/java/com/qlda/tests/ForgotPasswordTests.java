package com.qlda.tests;

import java.io.IOException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.qlda.core.BaseTest;
import com.qlda.pages.ForgotPasswordPage;
import com.qlda.pages.LoginPage;
import org.testng.Assert;

public class ForgotPasswordTests extends BaseTest {
    private LoginPage loginPage;
    private ForgotPasswordPage forgotPage;
    @BeforeMethod
    public void init() throws IOException{  
        loginPage = new LoginPage(driver, wait); 
    }
     /**
     * Test Case: Kiểm tra hiển thị form Quên mật khẩu
     * Expected: Hiển thị đúng form
     */
    @Test
     public void verifyForgotPasswordFormIsDisplayed(){     
        forgotPage = loginPage.clickForgotPasswordLink();
        Assert.assertTrue(
                forgotPage.isFormDisplayed(),
                "Form quên mật khẩu không hiển thị đúng"
        );
        Assert.assertEquals(
                forgotPage.getTitleText(),
                "Quên mật khẩu?",
                "Tiêu đề form quên mật khẩu không đúng"
        );
    }
     /**
     * Test Case: Kiểm tra Email sai định dạng
     * Expected: Hiển thị lỗi: “Email không hợp lệ”.
     */
    @Test
    public void verifyInvalidEmailFormat() {
        String invalidEmail = "user"; 
        forgotPage = loginPage.clickForgotPasswordLink();
        // 1. Nhập email sai định dạng
        forgotPage.enterEmail(invalidEmail);
        // 2. Xác minh lỗi validation (kiểm tra bằng JavaScript Executor)
        Assert.assertTrue(
            forgotPage.isEmailInputInvalid(),
            "Trường email không hiển thị lỗi validation của trình duyệt sau khi nhập email sai định dạng."
        );
        
    }
}
