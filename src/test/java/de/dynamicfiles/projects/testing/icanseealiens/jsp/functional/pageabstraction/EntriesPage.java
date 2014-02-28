package de.dynamicfiles.projects.testing.icanseealiens.jsp.functional.pageabstraction;

import de.dynamicfiles.projects.testing.icanseealiens.jsp.functional.Helper;
import java.net.URL;
import java.util.List;
import org.junit.Assert;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 *
 * @author Danny Althoff
 */
public class EntriesPage {

    public static final String SCREENSHOT_DIR = "target/screenshots";

    private final WebDriver browser;
    private final URL deploymentURL;
    private final String currentTestingClassName;
    private final String currentTestingClassMethodName;
    private TakesScreenshot screenshotter = null;

    public EntriesPage(WebDriver browser, URL deploymentURL, String currentTestingClassName, String currentTestingClassMethodName) {
        this.browser = browser;
        this.deploymentURL = deploymentURL;
        this.currentTestingClassName = currentTestingClassName;
        this.currentTestingClassMethodName = currentTestingClassMethodName;
    }

    public void setScreenshotter(TakesScreenshot screenshotter) {
        this.screenshotter = screenshotter;
    }

    public void checkEntryNotExisting(String entryTitle, String entryContent) {
        browser.navigate().to(deploymentURL.toExternalForm() + "entries.jsp");
        tryToMakeScreenshot("checkEntryNotExisting");
        Assert.assertEquals("title should be as accepted", Helper.TITLE_ENTRIES, browser.getTitle());
        Assert.assertFalse(browser.getPageSource().contains(entryTitle));
        Assert.assertFalse(browser.getPageSource().contains(entryContent));
    }

    public void checkEntryExisting(String entryTitle, String entryContent) {
        browser.navigate().to(deploymentURL.toExternalForm() + "entries.jsp");
        tryToMakeScreenshot("checkEntryNotExisting");
        Assert.assertEquals("title should be as accepted", Helper.TITLE_ENTRIES, browser.getTitle());
        Assert.assertTrue(browser.getPageSource().contains(entryTitle));
        Assert.assertTrue(browser.getPageSource().contains(entryContent));
    }

    public void createEntry(String entryTitle, String entryContent) {
        browser.navigate().to(deploymentURL.toExternalForm() + "entries.jsp");
        tryToMakeScreenshot("createEntry-navigate");
        Assert.assertEquals("title should be as accepted", Helper.TITLE_ENTRIES, browser.getTitle());

        WebElement createLink = browser.findElement(Helper.ENTRIES_CREATE_ENTRY_LINK);
        createLink.click();

        tryToMakeScreenshot("createEntry-createEntries");
        Assert.assertEquals("title should be as accepted", Helper.TITLE_CREATE_ENTRY, browser.getTitle());

        WebElement input_title = browser.findElement(Helper.CREATE_ENTRY_INPUT_TITLE);
        input_title.sendKeys(entryTitle);

        WebElement input_message = browser.findElement(Helper.CREATE_ENTRY_INPUT_MESSAGE);
        input_message.sendKeys(entryContent);

        WebElement submitButton = browser.findElement(Helper.CREATE_ENTRY_SUBMIT);
        submitButton.click();
        tryToMakeScreenshot("createEntry-afterSubmit");
        Assert.assertEquals("title should be as accepted", Helper.TITLE_ENTRIES, browser.getTitle());
    }

    private void tryToMakeScreenshot(String filename) {
        if( screenshotter == null ){
            return;
        }
        Helper.captureScreenshot(screenshotter, currentTestingClassName, currentTestingClassMethodName, filename);
    }

    public void createComment(String commentMessage, int entryToComment) {
        browser.navigate().to(deploymentURL.toExternalForm() + "entries.jsp");
        tryToMakeScreenshot("createComment-navigate");
        Assert.assertEquals("title should be as accepted", Helper.TITLE_ENTRIES, browser.getTitle());

        List<WebElement> addCommentLinks = browser.findElements(Helper.ENTRIES_ADD_COMMENT_LINK);
        Assert.assertFalse(addCommentLinks.isEmpty());

        WebElement addCommentLink = addCommentLinks.get(entryToComment);
        addCommentLink.click();

        Assert.assertEquals("title should be as accepted", Helper.TITLE_CREATE_COMMENT, browser.getTitle());

        WebElement submitButton = browser.findElement(Helper.CREATE_COMMENT_SUBMIT);
        submitButton.click();

        Assert.assertEquals("title should be as accepted", Helper.TITLE_ENTRIES, browser.getTitle());
    }

}
