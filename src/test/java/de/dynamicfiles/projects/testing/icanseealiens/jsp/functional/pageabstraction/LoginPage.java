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
public class LoginPage {

    public static final String SCREENSHOT_DIR = "target/screenshots";

    private final WebDriver browser;
    private final URL deploymentURL;
    private final String currentTestingClassName;
    private final String currentTestingClassMethodName;
    private TakesScreenshot screenshotter = null;

    public LoginPage(WebDriver browser, URL deploymentURL, String currentTestingClassName, String currentTestingClassMethodName) {
        this.browser = browser;
        this.deploymentURL = deploymentURL;
        this.currentTestingClassName = currentTestingClassName;
        this.currentTestingClassMethodName = currentTestingClassMethodName;
    }

    public void setScreenshotter(TakesScreenshot screenshotter) {
        this.screenshotter = screenshotter;
    }

    public void login(String username, String password) {
        browser.navigate().to(deploymentURL.toExternalForm() + "login.jsp");
        tryToMakeScreenshot("login-navigate");
        Assert.assertEquals("title should be as accepted", Helper.TITLE_LOGIN, browser.getTitle());
        WebElement inputUsername = browser.findElement(Helper.LOGIN_INPUT_USERNAME);
        inputUsername.sendKeys(username);

        WebElement inputPassword = browser.findElement(Helper.LOGIN_INPUT_PASSWORD);
        inputPassword.sendKeys(password);
        
        WebElement submit = browser.findElement(Helper.LOGIN_SUBMIT);
        tryToMakeScreenshot("login-beforeSubmit");
        submit.click();
        tryToMakeScreenshot("login-afterSubmit");
        Assert.assertEquals("title should be as accepted", Helper.TITLE_THANKYOU, browser.getTitle());
    }

    public void loginFailed(String username, String password) {
        browser.navigate().to(deploymentURL.toExternalForm() + "login.jsp");
        tryToMakeScreenshot("loginFailed-navigate");
        Assert.assertEquals("title should be as accepted", Helper.TITLE_LOGIN, browser.getTitle());
        WebElement inputUsername = browser.findElement(Helper.LOGIN_INPUT_USERNAME);
        inputUsername.sendKeys(username);

        WebElement inputPassword = browser.findElement(Helper.LOGIN_INPUT_PASSWORD);
        inputPassword.sendKeys(password);
        
        WebElement submit = browser.findElement(Helper.LOGIN_SUBMIT);
        tryToMakeScreenshot("loginFailed-beforeSubmit");
        submit.click();
        tryToMakeScreenshot("loginFailed-afterSubmit");
        Assert.assertEquals("title should be as accepted", Helper.TITLE_LOGIN, browser.getTitle());
        Assert.assertTrue(browser.getPageSource().contains("There was no user with the given username/password-combination."));
    }
    
    public void logout(){
        browser.navigate().to(deploymentURL.toExternalForm() + "logout.jsp");
        tryToMakeScreenshot("logout-navigate");
        Assert.assertEquals("title should be as accepted", Helper.TITLE_STARTPAGE, browser.getTitle());
    }

    private void tryToMakeScreenshot(String filename) {
        if( screenshotter == null ){
            return;
        }
        Helper.captureScreenshot(screenshotter, currentTestingClassName, currentTestingClassMethodName, filename);
    }
}
