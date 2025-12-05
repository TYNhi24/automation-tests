package com.qlda.tests;

import java.io.IOException;

import org.testng.annotations.BeforeMethod;

import org.testng.annotations.Test;

import com.qlda.core.BaseTest;
import com.qlda.pages.LoginPage;
public class LoginTests extends BaseTest{
    private LoginPage loginPage; 

    @BeforeMethod
    public void init() throws IOException{  
        loginPage = new LoginPage(driver); 
    }

    @Test
    public void TC001_Demo_Mo_Trinh_Duyet(){
        // Để trống
        // Chúng ta có thể thêm một lệnh tạm dừng để bạn nhìn thấy
        try {
            System.out.println("Trình duyệt đã mở, giữ trong 5 giây...");
            Thread.sleep(5000); // Tạm dừng 5 giây (5000 mili-giây)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void baseLogin(){
        loginPage.login("user@gmail.com", "User12");       
    }
}