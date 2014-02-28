package de.dynamicfiles.projects.testing.icanseealiens.jsp.functional;

import de.dynamicfiles.projects.testing.icanseealiens.jsp.functional.pageabstraction.EntriesPage;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.functional.pageabstraction.LoginPage;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.functional.pageabstraction.RegisterPage;
import java.io.File;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * This test is a little bit heavier than a small test:
 * - it creates a new user
 * - it tries to login with that user
 * - it creates a new billboard-entry
 * - it deletes that entry
 * - it creates a second billboard-entry
 * - it deletes the account and checks, if that entry is deleted too
 *
 * @author Danny Althoff
 */
@RunWith(Arquillian.class)
@RunAsClient
public class CreateAndDeleteBillboardEntry {

    public static final String SCREENSHOT_DIR = "target/screenshots";

    private static final String USERNAME_FOR_NEW_USER = "CreateAndDeleteBillboardEntryUser";
    private static final String PASSWORD_FOR_NEW_USER = "CreateAndDeleteBillboardEntryPassword";

    private static final String TITLE_BILLBOARDENTRY_FIRST = "FirstBillboardEntry-Title";
    private static final String TITLE_BILLBOARDENTRY_SECOND = "SecondBillboardEntry-Title";

    private static final String CONTENT_BILLBOARDENTRY_FIRST = "FirstBillboardEntry-Content";
    private static final String CONTENT_BILLBOARDENTRY_SECOND = "SecondBillboardEntry-Content";

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL deploymentURL;

    @Deployment(testable = false)
    public static WebArchive getDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "CreateAndDeleteBillboardEntry-test.war");

        // classes 
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.model.BillboardEntry.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.model.Comment.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.UserManager.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.CommentManager.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.BillboardEntryManager.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.producer.slsb.UserManagerSLSB.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.producer.slsb.BillboardEntryManagerSLSB.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.producer.slsb.CommentManagerSLSB.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.producer.UserSessionProducerDisposer.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.Current.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.SLSB.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.exceptions.NoUserFound.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.annotation.Transactional.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.interceptor.TransactionInterceptor.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.session.UserSession.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.observers.UserObserver.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.observers.BillboardEntryObserver.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.WasCreated.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.WillBeDeleted.class);

        // CDI
        Deployments.addCDI(archive);

        // persistence
        Deployments.addPersistence(archive);

        // web-file(s)
        Deployments.addBaseWebFiles(archive);
        archive.addAsWebResource(new File("src/main/webapp/create_entry.jsp"));
        archive.addAsWebResource(new File("src/main/webapp/delete_entry.jsp"));
        archive.addAsWebResource(new File("src/main/webapp/delete_user.jsp"));
        archive.addAsWebResource(new File("src/main/webapp/entries.jsp"));
        archive.addAsWebResource(new File("src/main/webapp/members.jsp"));
        archive.addAsWebResource(new File("src/main/webapp/login.jsp"));
        archive.addAsWebResource(new File("src/main/webapp/logout.jsp"));
        archive.addAsWebResource(new File("src/main/webapp/register.jsp"));
        archive.addAsWebResource(new File("src/main/webapp/thankyou.jsp"));
        archive.addAsWebResource(new File("src/main/webapp/index.jsp"));

        return archive;
    }

    @Test
    @InSequence(1)
    public void prepareDataForOtherTests() {
        RegisterPage registerPage = new RegisterPage(browser, deploymentURL, "CreateAndDeleteBillboardEntry", "prepareDataForOtherTests");
        registerPage.setScreenshotter(screenshotter);
        registerPage.checkUserNotRegistered(USERNAME_FOR_NEW_USER);
        registerPage.register(USERNAME_FOR_NEW_USER, PASSWORD_FOR_NEW_USER);
    }

    @Test
    @InSequence(2)
    public void loginWithNewCreatedUser() {
        LoginPage loginPage = new LoginPage(browser, deploymentURL, "CreateAndDeleteBillboardEntry", "loginWithNewCreatedUser");
        loginPage.setScreenshotter(screenshotter);
        
        loginPage.login(USERNAME_FOR_NEW_USER, PASSWORD_FOR_NEW_USER);
    }

    @Test
    @InSequence(3)
    public void createFirstBillboardEntry() {
        EntriesPage entriesPage = new EntriesPage(browser, deploymentURL, "CreateAndDeleteBillboardEntry", "createFirstBillboardEntry");
        entriesPage.setScreenshotter(screenshotter);
        entriesPage.checkEntryNotExisting(TITLE_BILLBOARDENTRY_FIRST, CONTENT_BILLBOARDENTRY_FIRST);
        entriesPage.createEntry(TITLE_BILLBOARDENTRY_FIRST, CONTENT_BILLBOARDENTRY_FIRST);
    }

    @Test
    @InSequence(4)
    public void deleteFirstBillboardEntry() {
        browser.navigate().to(deploymentURL.toExternalForm() + "entries.jsp");
        captureScreenshot("deleteFirstBillboardEntry-navigate");
        Assert.assertEquals("title should be as accepted", Helper.TITLE_ENTRIES, browser.getTitle());
        WebElement deleteLink = browser.findElement(Helper.ENTRIES_DELETE_ENTRY_LINK);
        deleteLink.click();
        captureScreenshot("deleteFirstBillboardEntry-afterDeletion");
        Assert.assertEquals("should be on 'entries'-page again", Helper.TITLE_ENTRIES, browser.getTitle());
        Assert.assertFalse(browser.getPageSource().contains(TITLE_BILLBOARDENTRY_FIRST));
        Assert.assertFalse(browser.getPageSource().contains(CONTENT_BILLBOARDENTRY_FIRST));
    }

    @Test
    @InSequence(5)
    public void createSecondBillboardEntry() {
        EntriesPage entriesPage = new EntriesPage(browser, deploymentURL, "CreateAndDeleteBillboardEntry", "createSecondBillboardEntry");
        entriesPage.setScreenshotter(screenshotter);
        entriesPage.checkEntryNotExisting(TITLE_BILLBOARDENTRY_SECOND, CONTENT_BILLBOARDENTRY_SECOND);
        entriesPage.createEntry(TITLE_BILLBOARDENTRY_SECOND, CONTENT_BILLBOARDENTRY_SECOND);
    }

    @Test
    @InSequence(98)
    public void loginForDeletionOfFirstEntry() {
        LoginPage loginPage = new LoginPage(browser, deploymentURL, "CreateAndDeleteBillboardEntry", "loginWithNewCreatedUser");
        loginPage.setScreenshotter(screenshotter);
        loginPage.logout();
        loginPage.login(USERNAME_FOR_NEW_USER, PASSWORD_FOR_NEW_USER);
    }

    @Test
    @InSequence(99)
    public void removeUserAndSecondEntryWithIt() {
        browser.navigate().to(deploymentURL.toExternalForm() + "entries.jsp");
        captureScreenshot("removeUserAndSecondEntryWithIt-navigate-first");
        Assert.assertEquals("title should be as accepted", Helper.TITLE_ENTRIES, browser.getTitle());
        Assert.assertTrue(browser.getPageSource().contains(TITLE_BILLBOARDENTRY_SECOND));
        Assert.assertTrue(browser.getPageSource().contains(CONTENT_BILLBOARDENTRY_SECOND));

        browser.navigate().to(deploymentURL.toExternalForm() + "members.jsp");
        captureScreenshot("removeUserAndSecondEntryWithIt-navigate-second");
        Assert.assertEquals("title should be as accepted", Helper.TITLE_MEMBERS, browser.getTitle());

        WebElement deleteLink = browser.findElement(Helper.MEMBERS_DELETE_USER_LINK);
        deleteLink.click();

        // now all entries should be removed too, because everything this user created, has to be removed
        captureScreenshot("removeUserAndSecondEntryWithIt-afterRemoveClick");
        Assert.assertEquals("after deletion, we should be on startpage", Helper.TITLE_STARTPAGE, browser.getTitle());
    }

    @Test
    @InSequence(100)
    public void checkEntriesAreNotExisting() {
        EntriesPage entriesPage = new EntriesPage(browser, deploymentURL, "CreateAndDeleteBillboardEntry", "createSecondBillboardEntry");
        entriesPage.setScreenshotter(screenshotter);
        entriesPage.checkEntryNotExisting(TITLE_BILLBOARDENTRY_FIRST, CONTENT_BILLBOARDENTRY_FIRST);
        entriesPage.checkEntryNotExisting(TITLE_BILLBOARDENTRY_SECOND, CONTENT_BILLBOARDENTRY_SECOND);
    }

    @ArquillianResource
    private TakesScreenshot screenshotter;

    private void captureScreenshot(String fileName) {
        File screenshotDir = new File(SCREENSHOT_DIR);
        screenshotDir.mkdirs();
        try{
            File tempFile = screenshotter.getScreenshotAs(OutputType.FILE);
            File screenshotFile = new File(SCREENSHOT_DIR, "screenshot-" + fileName + ".png");
            FileUtils.copyFile(tempFile, screenshotFile);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
