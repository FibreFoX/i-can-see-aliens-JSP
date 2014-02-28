package de.dynamicfiles.projects.testing.icanseealiens.jsp.functional;

import de.dynamicfiles.projects.testing.icanseealiens.jsp.functional.pageabstraction.EntriesPage;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.functional.pageabstraction.LoginPage;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.functional.pageabstraction.MembersPage;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.functional.pageabstraction.RegisterPage;
import java.io.File;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author Danny Althoff
 */
@RunWith(Arquillian.class)
@RunAsClient
public class ManageComments {

    private static final String TITLE_ENTRY_FIRST = "ManageComments-TITLE_ENTRY_FIRST";
    private static final String TITLE_ENTRY_SECOND = "ManageComments-TITLE_ENTRY_SECOND";
    private static final String CONTENT_COMMENT_FIRST = "ManageComments-CONTENT_COMMENT_FIRST";
    private static final String CONTENT_COMMENT_SECOND = "ManageComments-CONTENT_COMMENT_SECOND";
    private static final String CONTENT_ENTRY_FIRST = "ManageComments-CONTENT_ENTRY_FIRST";
    private static final String CONTENT_ENTRY_SECOND = "ManageComments-CONTENT_ENTRY_SECOND";

    private static final String USERNAME_FIRST = "ManageComments-USERNAME_FIRST";
    private static final String USERNAME_SECOND = "ManageComments-USERNAME_SECOND";
    private static final String PASSWORD_FIRST = "ManageComments-PASSWORD_FIRST";
    private static final String PASSWORD_SECOND = "ManageComments-PASSWORD_SECOND";

    public static final String SCREENSHOT_DIR = "target/screenshots/ManageComments";

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL deploymentURL;

    @Deployment(testable = false)
    public static WebArchive getDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "ManageComments-test.war");

        // classes 
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.model.BillboardEntry.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.model.Comment.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.UserManager.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.BillboardEntryManager.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.CommentManager.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.producer.slsb.UserManagerSLSB.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.producer.slsb.BillboardEntryManagerSLSB.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.producer.slsb.CommentManagerSLSB.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.producer.UserSessionProducerDisposer.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.Current.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.SLSB.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.WasCreated.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.WillBeDeleted.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.exceptions.NoUserFound.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.annotation.Transactional.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.interceptor.TransactionInterceptor.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.session.UserSession.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.observers.UserObserver.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.observers.BillboardEntryObserver.class);

        // CDI
        Deployments.addCDI(archive);

        // persistence
        Deployments.addPersistence(archive);

        // web-file(s)
        Deployments.addBaseWebFiles(archive);

        archive.addAsWebResource(new File("src/main/webapp/create_comment.jsp"));
        archive.addAsWebResource(new File("src/main/webapp/create_entry.jsp"));
        archive.addAsWebResource(new File("src/main/webapp/delete_entry.jsp"));
        archive.addAsWebResource(new File("src/main/webapp/delete_user.jsp"));
        archive.addAsWebResource(new File("src/main/webapp/entries.jsp"));
        archive.addAsWebResource(new File("src/main/webapp/index.jsp"));
        archive.addAsWebResource(new File("src/main/webapp/login.jsp"));
        archive.addAsWebResource(new File("src/main/webapp/logout.jsp"));
        archive.addAsWebResource(new File("src/main/webapp/members.jsp"));
        archive.addAsWebResource(new File("src/main/webapp/register.jsp"));
        archive.addAsWebResource(new File("src/main/webapp/thankyou.jsp"));

        return archive;
    }

    @ArquillianResource
    private TakesScreenshot screenshotter;

    @Test
    @InSequence(1)
    public void createUser() {
        RegisterPage registerPage = new RegisterPage(browser, deploymentURL, "ManageComments", "createUser");
        registerPage.setScreenshotter(screenshotter);
        registerPage.checkUserNotRegistered(USERNAME_FIRST);
        registerPage.register(USERNAME_FIRST, PASSWORD_FIRST);
    }

    @Test
    @InSequence(2)
    public void createSecondUser() {
        RegisterPage registerPage = new RegisterPage(browser, deploymentURL, "ManageComments", "createSecondUser");
        registerPage.setScreenshotter(screenshotter);
        registerPage.checkUserNotRegistered(USERNAME_SECOND);
        registerPage.register(USERNAME_SECOND, PASSWORD_SECOND);
    }

    @Test
    @InSequence(3)
    public void createEntryWithFirstUser() {
        LoginPage loginPage = new LoginPage(browser, deploymentURL, "ManageComments", "createEntryWithFirstUser");
        loginPage.setScreenshotter(screenshotter);
        // logout
        loginPage.logout();
        // login
        loginPage.login(USERNAME_FIRST, PASSWORD_FIRST);
        
        EntriesPage entriesPage = new EntriesPage(browser, deploymentURL, "ManageComments", "createEntryWithFirstUser");
        entriesPage.setScreenshotter(screenshotter);
        entriesPage.checkEntryNotExisting(TITLE_ENTRY_FIRST, CONTENT_ENTRY_FIRST);
        entriesPage.createEntry(TITLE_ENTRY_FIRST, CONTENT_ENTRY_FIRST);
    }

    @Test
    @InSequence(4)
    public void createEntryWithSecondUser() {
        LoginPage loginPage = new LoginPage(browser, deploymentURL, "ManageComments", "createEntryWithSecondUser");
        loginPage.setScreenshotter(screenshotter);
        // logout
        loginPage.logout();
        // login
        loginPage.login(USERNAME_SECOND, PASSWORD_SECOND);
        
        EntriesPage entriesPage = new EntriesPage(browser, deploymentURL, "ManageComments", "createEntryWithSecondUser");
        entriesPage.setScreenshotter(screenshotter);
        entriesPage.checkEntryNotExisting(TITLE_ENTRY_SECOND, CONTENT_ENTRY_SECOND);
        entriesPage.createEntry(TITLE_ENTRY_SECOND, CONTENT_ENTRY_SECOND);
    }

    @Test
    @InSequence(5)
    public void createCommentWithFirstUserOnSecondEntry() {
        LoginPage loginPage = new LoginPage(browser, deploymentURL, "ManageComments", "createCommentWithFirstUserOnSecondEntry");
        loginPage.setScreenshotter(screenshotter);
        // logout
        loginPage.logout();
        // login
        loginPage.login(USERNAME_FIRST, PASSWORD_FIRST);
        
        EntriesPage entriesPage = new EntriesPage(browser, deploymentURL, "ManageComments", "createCommentWithFirstUserOnSecondEntry");
        entriesPage.setScreenshotter(screenshotter);
        entriesPage.createComment(CONTENT_COMMENT_FIRST, 1);
    }

    @Test
    @InSequence(6)
    public void createCommentWithSecondUserOnFirstEntry() {
        LoginPage loginPage = new LoginPage(browser, deploymentURL, "ManageComments", "createCommentWithSecondUserOnFirstEntry");
        loginPage.setScreenshotter(screenshotter);
        // logout
        loginPage.logout();
        // login
        loginPage.login(USERNAME_SECOND, PASSWORD_SECOND);
        
        EntriesPage entriesPage = new EntriesPage(browser, deploymentURL, "ManageComments", "createCommentWithSecondUserOnFirstEntry");
        entriesPage.setScreenshotter(screenshotter);
        entriesPage.createComment(CONTENT_COMMENT_SECOND, 0);

    }

    @Test
    @InSequence(7)
    public void deleteFirstUser() {
        LoginPage loginPage = new LoginPage(browser, deploymentURL, "ManageComments", "deleteFirstUser");
        loginPage.setScreenshotter(screenshotter);
        // logout
        loginPage.logout();
        // login
        loginPage.login(USERNAME_FIRST, PASSWORD_FIRST);
        
        MembersPage membersPage = new MembersPage(browser, deploymentURL, "ManageComments", "deleteFirstUser");
        membersPage.setScreenshotter(screenshotter);
        membersPage.deleteMember(USERNAME_FIRST);
    }

    @Test
    @InSequence(8)
    public void deleteSecondUser() {
        LoginPage loginPage = new LoginPage(browser, deploymentURL, "ManageComments", "deleteSecondUser");
        // logout
        loginPage.logout();
        // login
        loginPage.login(USERNAME_SECOND, PASSWORD_SECOND);
        
        MembersPage membersPage = new MembersPage(browser, deploymentURL, "ManageComments", "deleteSecondUser");
        membersPage.deleteMember(USERNAME_SECOND);
    }

    @Test
    @InSequence(9)
    public void checkNothingExistingAnymore() {
        EntriesPage entriesPage = new EntriesPage(browser, deploymentURL, "ManageComments", "checkNothingExistingAnymore");
        entriesPage.setScreenshotter(screenshotter);
        entriesPage.checkEntryNotExisting(TITLE_ENTRY_FIRST, CONTENT_ENTRY_FIRST);
        entriesPage.checkEntryNotExisting(TITLE_ENTRY_SECOND, CONTENT_ENTRY_SECOND);
        
        assertFalse(browser.getPageSource().contains(USERNAME_FIRST));
        assertFalse(browser.getPageSource().contains(USERNAME_SECOND));
    }
}
