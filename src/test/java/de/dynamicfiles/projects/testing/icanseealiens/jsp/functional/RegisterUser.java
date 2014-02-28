package de.dynamicfiles.projects.testing.icanseealiens.jsp.functional;

import de.dynamicfiles.projects.testing.icanseealiens.jsp.functional.pageabstraction.LoginPage;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.functional.pageabstraction.RegisterPage;
import java.io.File;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
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
 * :( no TestNG
 * https://issues.jboss.org/browse/ARQ-919
 * https://issues.jboss.org/browse/ARQ-1586
 * https://issues.jboss.org/browse/ARQ-1576
 *
 * @author Danny Althoff
 */
@RunWith(Arquillian.class)
@RunAsClient
public class RegisterUser {

    private static final String USERNAME_FOR_NEW_USER = "RegisterUser-USERNAME_FOR_NEW_USER";
    private static final String PASSWORD_FOR_NEW_USER = "RegisterUserPassword-PASSWORD_FOR_NEW_USER";

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private TakesScreenshot screenshotter;

    @Deployment(testable = false)
    public static WebArchive getDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "RegisterUser-test.war");

        // classes 
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User.class);
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

        // CDI
        Deployments.addCDI(archive);

        // persistence
        Deployments.addPersistence(archive);

        // web-file(s)
        Deployments.addBaseWebFiles(archive);
        archive.addAsWebResource(new File("src/main/webapp/delete_user.jsp"));
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
    public void checkExistingLinks(@ArquillianResource URL deploymentURL) {
        Assert.assertNotNull("there should be a browser", browser);
        browser.navigate().to(deploymentURL.toExternalForm() + "index.jsp");
        Helper.captureScreenshot(screenshotter, "RegisterUser", "checkExistingLinks", "navigate");
        Assert.assertEquals("title should be as accepted", Helper.TITLE_STARTPAGE, browser.getTitle());

        WebElement createAccountButton = browser.findElement(Helper.STARTPAGE_REGISTER_BUTTON);
        Assert.assertNotNull("there should be a 'create account'-button on the start-page", createAccountButton);
    }

    @Test
    @InSequence(2)
    public void createNewUser(@ArquillianResource URL deploymentURL) {
        RegisterPage registerPage = new RegisterPage(browser, deploymentURL, "RegisterUser", "createNewUser");
        registerPage.setScreenshotter(screenshotter);
        
        registerPage.checkUserNotRegistered(USERNAME_FOR_NEW_USER);
        registerPage.register(USERNAME_FOR_NEW_USER, PASSWORD_FOR_NEW_USER);
    }

    @Test
    @InSequence(3)
    public void listMembers(@ArquillianResource URL deploymentURL) {
        browser.navigate().to(deploymentURL.toExternalForm() + "members.jsp");
        Helper.captureScreenshot(screenshotter, "RegisterUser", "listMembers", "navigate");
        Assert.assertEquals("title should be as accepted", Helper.TITLE_MEMBERS, browser.getTitle());
        String source = browser.getPageSource();
        Assert.assertTrue("new created user should exist in list", source.contains(USERNAME_FOR_NEW_USER));
    }
    
    @Test
    @InSequence(4)
    public void loginWithNewCreatedUser(@ArquillianResource URL deploymentURL){
        LoginPage loginPage = new LoginPage(browser, deploymentURL, "RegisterUser", "loginWithNewCreatedUser");
        loginPage.setScreenshotter(screenshotter);
        
        loginPage.login(USERNAME_FOR_NEW_USER, PASSWORD_FOR_NEW_USER);
    }
    
    @Test
    @InSequence(5)
    public void removeUser(@ArquillianResource URL deploymentURL) {
        browser.navigate().to(deploymentURL.toExternalForm() + "members.jsp");
        Helper.captureScreenshot(screenshotter, "RegisterUser", "removeUser", "navigate");
        Assert.assertEquals("title should be as accepted", Helper.TITLE_MEMBERS, browser.getTitle());
        
        WebElement deleteLink = browser.findElement(Helper.MEMBERS_DELETE_USER_LINK);
        deleteLink.click();
        Helper.captureScreenshot(screenshotter, "RegisterUser", "removeUser", "afterClick");
        Assert.assertEquals("after deletion, we should be on startpage", Helper.TITLE_STARTPAGE, browser.getTitle());
    }

}
