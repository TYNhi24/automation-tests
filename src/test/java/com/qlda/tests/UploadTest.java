package com.qlda.tests;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.qlda.core.BaseTest;
import com.qlda.pages.LoginPage;
import com.qlda.pages.ProjectPage;
import com.qlda.pages.TaskPage;
import com.qlda.utils.MockUtils;
import com.qlda.utils.WebDriverConfig;

public class UploadTest extends BaseTest {
    private TaskPage taskPage;
    private LoginPage loginPage;
    private ProjectPage projectPage;
    private String projectId;
    private String listId;

    @BeforeMethod
    @Override
    public void setUp() throws IOException {
        super.setUp();
        String userId = MockUtils.mockUser("test@gmail.com", "Password123", "Valid User", false);
        projectId = MockUtils.mockProject("Test Project", "desc", userId);
        listId = MockUtils.mockList(projectId, "List 1");
        MockUtils.mockTask(listId, "Task 1");
        driver.get(WebDriverConfig.getBaseUrl() + "/login");
        loginPage = new LoginPage(driver, wait);
        projectPage = new ProjectPage(driver, wait);
        taskPage = new TaskPage(driver, wait);
        loginPage.loginUser("test@gmail.com", "Password123");
        projectPage.waitForPageLoad();
    }

    @AfterMethod
    public void cleanUpUploads() throws IOException {
        Path uploadsDir = Paths.get("C:\\Users\\tdvit\\source\\Node.js\\qlda\\uploads");
        if (Files.exists(uploadsDir)) {
            Files.list(uploadsDir)
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                        }
                    });
        }
    }

    /**
     * Test: Kiểm tra hiển thị nút "Đính kèm"
     */
    @Test
    public void testAttachButtonVisible() {
        projectPage.goToProject(projectId);
        taskPage.openTaskModal();
        Assert.assertTrue(taskPage.isDisplayed(By.xpath("//span[contains(text(),'Đính kèm')]")),
                "Attach button should be visible on Task page");
    }

    private String getResourceAbsolutePath(String fileName) {
        String resourcePath = "data/" + fileName;
        java.net.URL resourceUrl = getClass().getClassLoader().getResource(resourcePath);
        if (resourceUrl == null) {
            throw new RuntimeException("File not found in resources/data: " + fileName);
        }
        String filePath = resourceUrl.getPath();
        filePath = URLDecoder.decode(filePath, StandardCharsets.UTF_8);
        if (filePath.startsWith("/") && System.getProperty("os.name").toLowerCase().contains("win")) {
            filePath = filePath.substring(1);
        }
        return filePath;
    }

    /**
     * Test: Kiểm tra upload file JPG hợp lệ
     */
    @Test
    public void testUploadValidJpgFile() {
        projectPage.goToProject(projectId);
        taskPage.openTaskModal();

        String filePath = getResourceAbsolutePath("file.jpg");
        taskPage.uploadFile(filePath);

        Assert.assertTrue(taskPage.isFileUploaded("file.jpg"),
                "Uploaded JPG file should be visible in the task attachments");
    }

    /**
     * Test: Kiểm tra upload file JPEG hợp lệ
     */
    @Test
    public void testUploadValidJpegFile() {
        projectPage.goToProject(projectId);
        taskPage.openTaskModal();
        String filePath = getResourceAbsolutePath("file.jpeg");
        taskPage.uploadFile(filePath);
        Assert.assertTrue(taskPage.isFileUploaded("file.jpeg"),
                "Uploaded JPEG file should be visible in the task attachments");
    }

    /**
     * Test: Kiểm tra upload file PDF hợp lệ
     */
    @Test
    public void testUploadValidPdfFile() {
        projectPage.goToProject(projectId);
        taskPage.openTaskModal();
        String filePath = getResourceAbsolutePath("file.pdf");
        taskPage.uploadFile(filePath);
        Assert.assertTrue(taskPage.isFileUploaded("file.pdf"),
                "Uploaded PDF file should be visible in the task attachments");
    }

    /**
     * Test: Kiểm tra upload file DOC hợp lệ
     */
    @Test
    public void testUploadValidDocFile() {
        projectPage.goToProject(projectId);
        taskPage.openTaskModal();
        String filePath = getResourceAbsolutePath("file.doc");
        taskPage.uploadFile(filePath);
        Assert.assertTrue(taskPage.isFileUploaded("file.doc"),
                "Uploaded DOC file should be visible in the task attachments");
    }

    /**
     * Test: Kiểm tra upload file DOCX hợp lệ
     */
    @Test
    public void testUploadValidDocxFile() {
        projectPage.goToProject(projectId);
        taskPage.openTaskModal();
        String filePath = getResourceAbsolutePath("file.docx");
        taskPage.uploadFile(filePath);
        Assert.assertTrue(taskPage.isFileUploaded("file.docx"),
                "Uploaded DOCX file should be visible in the task attachments");
    }

    /**
     * Test: Kiểm tra upload file trùng tên
     */
    @Test
    public void testUploadDuplicateFile() {
        projectPage.goToProject(projectId);
        taskPage.openTaskModal();
        String filePath = getResourceAbsolutePath("file.jpg");
        taskPage.uploadFile(filePath);

        // Upload the same file again
        projectPage.goToProject(projectId);
        taskPage.openTaskModal();
        taskPage.uploadFile(filePath);

        Assert.assertTrue(taskPage.isFileUploaded("file.jpg"),
                "Original file should be visible in the task attachments");
        Assert.assertTrue(taskPage.isFileUploaded("file[1].jpg"),
                "Duplicate file with [1] suffix should be visible in the task attachments");
    }

    /**
     * Test: Kiểm tra upload file có ký tự đặc biệt trong tên
     */
    @Test
    public void testUploadFileWithSpecialChars() {
        projectPage.goToProject(projectId);
        taskPage.openTaskModal();

        String filePath = getResourceAbsolutePath("!@#$%^&.docx");
        taskPage.uploadFile(filePath);

        Assert.assertTrue(taskPage.isFileUploaded("!@#$%^&.docx"),
                "File with special characters in name should be uploaded and visible");
    }

    /**
     * Test: Kiểm tra upload nhiều file
     */
    @Test
    public void testUploadMultipleFiles() {
        projectPage.goToProject(projectId);
        taskPage.openTaskModal();

        String filePath1 = getResourceAbsolutePath("file.jpg");
        String filePath2 = getResourceAbsolutePath("file.pdf");
        String filePath3 = getResourceAbsolutePath("file.docx");

        taskPage.uploadFile(filePath1);
        taskPage.uploadFile(filePath2);
        taskPage.uploadFile(filePath3);

        Assert.assertTrue(taskPage.isFileUploaded("file.jpg"),
                "First file should be uploaded and visible");
        Assert.assertTrue(taskPage.isFileUploaded("file.pdf"),
                "Second file should be uploaded and visible");
        Assert.assertTrue(taskPage.isFileUploaded("file.docx"),
                "Third file should be uploaded and visible");
    }

    /**
     * Test: Kiểm tra tải xuống file đã upload
     * 
     * @throws InterruptedException
     */
    @Test
    public void testDownloadUploadedFile() throws InterruptedException {
        projectPage.goToProject(projectId);
        taskPage.openTaskModal();

        String filePath = getResourceAbsolutePath("file.pdf");
        taskPage.uploadFile(filePath);

        Assert.assertTrue(taskPage.isFileUploaded("file.pdf"), "file.pdf should be uploaded");

        By downloadBtn = By.xpath("//a[normalize-space()='file.pdf']");
        Assert.assertTrue(taskPage.isDisplayed(downloadBtn), "Download link should be visible");

        String originalTab = driver.getWindowHandle();

        taskPage.click(downloadBtn);
        Thread.sleep(1000);

        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(originalTab)) {
                driver.switchTo().window(handle);
                break;
            }
        }
        String bodyText = driver.findElement(By.tagName("body")).getText();
        Assert.assertTrue(bodyText.contains("Cannot GET") || bodyText.toLowerCase().contains("lỗi"),
                "Body should contain error message when download fails. Actual: " + bodyText);
    }

    @Test
    public void testUploadEmptyFile() {
        projectPage.goToProject(projectId);
        taskPage.openTaskModal();

        String filePath = getResourceAbsolutePath("empty.docx"); // file 0KB
        taskPage.uploadFile(filePath);

        Assert.assertTrue(taskPage.isFileUploaded("empty.docx"),
                "Empty file should be uploaded");
    }

    @Test
    public void testUploadFileWithinLimit() {
        projectPage.goToProject(projectId);
        taskPage.openTaskModal();

        String filePath = getResourceAbsolutePath("file.jpg"); // file < 2MB
        taskPage.uploadFile(filePath);

        Assert.assertTrue(taskPage.isFileUploaded("file.jpg"),
                "File under 2MB should be uploaded successfully");
    }

    @Test
    public void testUploadFileAtLimit() {
        projectPage.goToProject(projectId);
        taskPage.openTaskModal();

        String filePath = getResourceAbsolutePath("file2mb.jpg"); // file = 2MB
        taskPage.uploadFile(filePath);

        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        Assert.assertTrue(alertText.contains("File quá lớn") || alertText.contains("2MB"),
                "Alert message should indicate file is too large");
        alert.accept();
    }

    @Test
    public void testUploadFileOverLimit() {
        projectPage.goToProject(projectId);
        taskPage.openTaskModal();

        String filePath = getResourceAbsolutePath("filemax.jpg"); // file > 2MB
        taskPage.uploadFile(filePath);

        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        Assert.assertTrue(alertText.contains("File quá lớn") || alertText.contains("2MB"),
                "Alert message should indicate file is too large");
        alert.accept();
    }

    @Test
    public void testUploadUnsupportedFileType() {
        projectPage.goToProject(projectId);
        taskPage.openTaskModal();

        String filePath = getResourceAbsolutePath("file.exe");
        taskPage.uploadFile(filePath);

        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        Assert.assertTrue(
                alertText.toLowerCase().contains("chỉ chấp nhận") &&
                        alertText.toLowerCase().contains("png") &&
                        alertText.toLowerCase().contains("jpg") &&
                        alertText.toLowerCase().contains("pdf") &&
                        alertText.toLowerCase().contains("doc"),
                "Alert message should indicate only supported file types are accepted");
        alert.accept();
    }

    @Test
    public void testUploadFileWithVeryLongName() {
        projectPage.goToProject(projectId);
        taskPage.openTaskModal();

        String longFileName = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghij.docx";
        String filePath = getResourceAbsolutePath(longFileName);
        taskPage.uploadFile(filePath);

        Assert.assertTrue(taskPage.isFileUploaded(longFileName),
                "File with very long name should be uploaded and visible");
    }

    @Test
    public void testUploadedFileInfoDisplayed() {
        projectPage.goToProject(projectId);
        taskPage.openTaskModal();

        String fileName = "file.jpg";
        String filePath = getResourceAbsolutePath(fileName);
        taskPage.uploadFile(filePath);

        Assert.assertTrue(taskPage.isFileUploaded(fileName), "File should be uploaded");
    }

}
