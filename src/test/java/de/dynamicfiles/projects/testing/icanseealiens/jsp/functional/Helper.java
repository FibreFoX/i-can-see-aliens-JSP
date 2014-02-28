package de.dynamicfiles.projects.testing.icanseealiens.jsp.functional;

import static de.dynamicfiles.projects.testing.icanseealiens.jsp.functional.CreateAndDeleteBillboardEntry.SCREENSHOT_DIR;
import java.io.File;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

/**
 *
 * @author Danny Althoff
 */
public class Helper {

    public final static By STARTPAGE_REGISTER_BUTTON = By.xpath("//a[contains(text(),'Create account')]");
    public final static By STARTPAGE_LOGIN_BUTTON = By.xpath("//a[contains(text(),'Login')]");
    public final static By REGISTER_INPUT_USERNAME = By.name("name");
    public final static By REGISTER_INPUT_PASSWORD = By.name("pwd");
    public final static By REGISTER_SUBMIT = By.xpath("//button[contains(text(),'Create Account')]");
    public final static By LOGIN_INPUT_USERNAME = By.name("name");
    public final static By LOGIN_INPUT_PASSWORD = By.name("pwd");
    public final static By LOGIN_SUBMIT = By.xpath("//button[contains(text(),'Login')]");
    public final static By MEMBERS_DELETE_USER_LINK = By.xpath("//a[contains(text(),'delete user')]");
    public final static By ENTRIES_CREATE_ENTRY_LINK = By.xpath("//a[contains(text(),'Write a new entry')]");
    public final static By ENTRIES_DELETE_ENTRY_LINK = By.xpath("//a[contains(text(),'remove entry')]"); // can exist multiple times
    public final static By ENTRIES_ADD_COMMENT_LINK = By.xpath("//a[contains(text(),'add comment')]"); // can exist multiple times
    public final static By CREATE_ENTRY_INPUT_TITLE = By.name("title");
    public final static By CREATE_ENTRY_INPUT_MESSAGE = By.name("content");
    public final static By CREATE_ENTRY_SUBMIT = By.xpath("//button[contains(text(), 'Create')]");
    public final static By CREATE_COMMENT_INPUT_MESSAGE = By.name("content");
    public final static By CREATE_COMMENT_SUBMIT = By.xpath("//button[contains(text(),'Add comment')]");

    public final static String TITLE_STARTPAGE = "Startpage - I can see aliens (JSP)";
    public final static String TITLE_LOGIN = "Login - I can see aliens (JSP)";
    public final static String TITLE_REGISTER = "Registration - I can see aliens (JSP)";
    public final static String TITLE_THANKYOU = "Thankyou - I can see aliens (JSP)";
    public final static String TITLE_MEMBERS = "Members - I can see aliens (JSP)";
    public final static String TITLE_ENTRIES = "Billboard - I can see aliens (JSP)";
    public final static String TITLE_CREATE_ENTRY = "Write a new entry - I can see aliens (JSP)";
    public final static String TITLE_CREATE_COMMENT = "Write a new comment - I can see aliens (JSP)";

    public static void captureScreenshot(TakesScreenshot screenshotter, String currentTestingClassName, String currentTestingClassMethodName, String fileName) {
        String myScreenshotDirPath = SCREENSHOT_DIR + "/" + currentTestingClassName + "/" + currentTestingClassMethodName;
        File screenshotDir = new File(myScreenshotDirPath);
        screenshotDir.mkdirs();
        try{
            File tempFile = screenshotter.getScreenshotAs(OutputType.FILE);
            File screenshotFile = new File(myScreenshotDirPath, fileName + ".png");
            FileUtils.copyFile(tempFile, screenshotFile);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
