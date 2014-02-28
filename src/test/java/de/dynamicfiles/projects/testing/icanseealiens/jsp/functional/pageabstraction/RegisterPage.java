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
public class RegisterPage {

    public static final String SCREENSHOT_DIR = "target/screenshots";

    private final WebDriver browser;
    private final URL deploymentURL;
    private final String currentTestingClassName;
    private final String currentTestingClassMethodName;
    private TakesScreenshot screenshotter = null;

    public RegisterPage(WebDriver browser, URL deploymentURL, String currentTestingClassName, String currentTestingClassMethodName) {
        this.browser = browser;
        this.deploymentURL = deploymentURL;
        this.currentTestingClassName = currentTestingClassName;
        this.currentTestingClassMethodName = currentTestingClassMethodName;
    }

    public void setScreenshotter(TakesScreenshot screenshotter) {
        this.screenshotter = screenshotter;
    }

    public void checkUserNotRegistered(String username) {
        browser.navigate().to(deploymentURL.toExternalForm() + "members.jsp");
        tryToMakeScreenshot("checkUserNotRegistered");
        Assert.assertEquals("title should be as accepted", Helper.TITLE_MEMBERS, browser.getTitle());
        Assert.assertFalse(browser.getPageSource().contains(username));
    }

    public void register(String username, String password) {
        browser.navigate().to(deploymentURL.toExternalForm() + "register.jsp");
        tryToMakeScreenshot("register-navigate");
        Assert.assertEquals("title should be as accepted", Helper.TITLE_REGISTER, browser.getTitle());

        WebElement inputUsername = browser.findElement(Helper.REGISTER_INPUT_USERNAME);
        inputUsername.sendKeys(username);

        WebElement inputPassword = browser.findElement(Helper.REGISTER_INPUT_PASSWORD);
        inputPassword.sendKeys(password);

        tryToMakeScreenshot("register-dataFilled");

        WebElement submitButton = browser.findElement(Helper.REGISTER_SUBMIT);
        submitButton.click();
        tryToMakeScreenshot("register-afterSubmit");

        Assert.assertEquals("we should be on thankyou now", Helper.TITLE_THANKYOU, browser.getTitle());
    }

    private void tryToMakeScreenshot(String filename) {
        if( screenshotter == null ){
            return;
        }
        Helper.captureScreenshot(screenshotter, currentTestingClassName, currentTestingClassMethodName, filename);
    }

}
