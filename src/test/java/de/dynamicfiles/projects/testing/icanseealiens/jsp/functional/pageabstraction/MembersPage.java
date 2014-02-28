package de.dynamicfiles.projects.testing.icanseealiens.jsp.functional.pageabstraction;

import de.dynamicfiles.projects.testing.icanseealiens.jsp.functional.Helper;
import java.net.URL;
import org.junit.Assert;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 *
 * @author Danny Althoff
 */
public class MembersPage {

    public static final String SCREENSHOT_DIR = "target/screenshots";

    private final WebDriver browser;
    private final URL deploymentURL;
    private final String currentTestingClassName;
    private final String currentTestingClassMethodName;
    private TakesScreenshot screenshotter = null;

    public MembersPage(WebDriver browser, URL deploymentURL, String currentTestingClassName, String currentTestingClassMethodName) {
        this.browser = browser;
        this.deploymentURL = deploymentURL;
        this.currentTestingClassName = currentTestingClassName;
        this.currentTestingClassMethodName = currentTestingClassMethodName;
    }

    public void setScreenshotter(TakesScreenshot screenshotter) {
        this.screenshotter = screenshotter;
    }

    private void tryToMakeScreenshot(String filename) {
        if( screenshotter == null ){
            return;
        }
        Helper.captureScreenshot(screenshotter, currentTestingClassName, currentTestingClassMethodName, filename);
    }

    public void checkHasMember(String username) {
        browser.navigate().to(deploymentURL.toExternalForm() + "members.jsp");
        tryToMakeScreenshot("checkHasMember-navigate");
        Assert.assertTrue(browser.getPageSource().contains(username));
    }

    public void checkMemberNotExisting(String username) {
        browser.navigate().to(deploymentURL.toExternalForm() + "members.jsp");
        tryToMakeScreenshot("checkMemberNotExisting-navigate");
        Assert.assertFalse(browser.getPageSource().contains(username));
    }

    public void deleteMember(String username) {
        browser.navigate().to(deploymentURL.toExternalForm() + "members.jsp");
        tryToMakeScreenshot("deleteMember-navigate");

        // ENHANCEMENT restrict "delete-user-link" to given username
        WebElement deleteLink = browser.findElement(Helper.MEMBERS_DELETE_USER_LINK);
        deleteLink.click();
        tryToMakeScreenshot("deleteMember-afterDeletion");
        Assert.assertEquals("title should be as accepted", Helper.TITLE_STARTPAGE, browser.getTitle());
    }
}
