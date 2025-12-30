package com.qlda.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qlda.core.BasePage;

public class HeaderComponentPage extends BasePage {
    
    public HeaderComponentPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    private By avatarButton = By.xpath(
        "//body/div/div/header/div/div[3]/button[1]"
    );

    private By profileItem = By.xpath(
        "//a[contains(text(),'Hồ sơ')]"
    );
    
    public void clickAvatarButton() {
        WebElement avatar = wait.until(
            ExpectedConditions.elementToBeClickable(avatarButton));
        avatar.click();
    }
    public UserProfilePage navigateToProfilePage() {
        // mở dropdown
        clickAvatarButton();
        // chờ menu xổ ra rồi click "Hồ sơ"
        WebElement profile = wait.until(
            ExpectedConditions.elementToBeClickable(profileItem));
        profile.click();
        return new UserProfilePage(driver, wait);
    }
}
