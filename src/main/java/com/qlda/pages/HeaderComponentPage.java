package com.qlda.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
public class HeaderComponentPage {
    private WebDriver driver;
    private WebDriverWait wait;
    public HeaderComponentPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    // đúng cái button chứa avatar
    private By avatarButton = By.xpath(
            "//button[.//img[@alt='User Avatar']]"
    );
    // li đầu tiên trong menu dropdown
    private By profileItem = By.xpath(
    "//ul[contains(@class,'py-1')]/li[1]//a"
);
    public void clickAvatarButton() {
        WebElement avatar = wait.until(
            ExpectedConditions.elementToBeClickable(avatarButton));
            // ((JavascriptExecutor) driver).executeScript(
            //     "arguments[0].scrollIntoView({block:'center',inline:'center'});",avatar);
        avatar.click();
    }
    public UserProfilePage navigateToProfilePage() {
        // mở dropdown
        clickAvatarButton();
        // chờ menu xổ ra rồi click "Hồ sơ" (li đầu)
        WebElement profile = wait.until(
            ExpectedConditions.elementToBeClickable(profileItem));
        profile.click();
        return new UserProfilePage(driver);
    }
}
