package com.qlda.pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class UserProfilePage {
    private WebDriver driver;
    
    public UserProfilePage(WebDriver driver) {
        this.driver = driver;
    }
    private By projectNameElements = By.cssSelector("span.font-semibold.text-gray-700.text-3xl");
    private By name = By.xpath("//h2[normalize-space()='user']");
    private By email  = By.cssSelector("//p[@class='text-sm text-gray-500']");
    private By createdProject =
            By.xpath("//h2[contains(normalize-space(), 'Dự án đã tạo')]");
    private By joinedProject =
            By.xpath("//h2[contains(normalize-space(), 'Dự án tham gia')]");
    private By jobStatistics =
            By.xpath("//h2[contains(normalize-space(), 'Thống kê công việc')]");

    private By totalProjectText =By.xpath("//h2[normalize-space()='Dự án đã tạo (0)']");
    // By.xpath( "(//h2[contains(@class,'text-lg') and contains(@class,'font-semibold')])[1]" );

    public List<String> getProjectNames() {
        List<WebElement> elements = driver.findElements(projectNameElements);
        return elements.stream()
                .map(e -> {
                    try {
                        return e.getText();
                    } catch (StaleElementReferenceException ex) {
                        return driver.findElement(projectNameElements).getText();
                    }
                })
                .collect(Collectors.toList());
        }
    public boolean isInfoDisplayed() {
        return driver.findElement(name).isDisplayed()
                && driver.findElement(email).isDisplayed()
                && driver.findElement(createdProject).isDisplayed()
                && driver.findElement(joinedProject).isDisplayed()
                && driver.findElement(jobStatistics).isDisplayed();
    }

    //
    public boolean isTotalProjectDisplayed() {
        return driver.findElement(totalProjectText).isDisplayed();
    }
    public String getTotalProjectText() {
        return driver.findElement(totalProjectText).getText();
    }
}
