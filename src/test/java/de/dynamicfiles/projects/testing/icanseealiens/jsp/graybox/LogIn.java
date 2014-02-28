package de.dynamicfiles.projects.testing.icanseealiens.jsp.graybox;

import de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.Current;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.session.UserSession;
import java.io.File;
import java.net.URL;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.servlet.AfterServlet;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 *
 * @author Danny Althoff
 */
@RunWith(Arquillian.class)
@WarpTest
@RunAsClient
public class LogIn {

    private static final By USERNAME_FIELD = By.name("name");
    private static final By PASSWORD_FIELD = By.name("pwd");
    private static final By LOGIN_BUTTON = By.xpath("//button[contains(text(),'Login')]");// id("loginSubmitButton");

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL deploymentURL;

//    @ArquillianResource
//    private TakesScreenshot screenshotter;
    @Deployment
    @OverProtocol("Servlet 3.0")
    public static WebArchive getDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "LogIn-Test.war");

        // classes 
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.UserManager.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.producer.slsb.UserManagerSLSB.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.producer.UserSessionProducerDisposer.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.Current.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.SLSB.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.exceptions.NoUserFound.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.annotation.Transactional.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.session.UserSession.class);

        // CDI
        Deployments.addCDI(archive);

        // persistence
        Deployments.addPersistence(archive);

        // web-file(s)
        Deployments.addBaseWebFiles(archive);
        archive.addAsWebResource(new File("src/main/webapp/login.jsp"));
        archive.addAsWebResource(new File("src/main/webapp/logout.jsp"));
        archive.addAsWebResource(new File("src/main/webapp/index.jsp"));

        return archive;
    }

    @Test
    public void logInWithWrongData() {
        Warp.initiate(new Activity() {
            // black-box

            @Override
            public void perform() {
                browser.navigate().to(deploymentURL.toExternalForm() + "login.jsp");
                Assert.assertEquals("title should be as accepted", "Login - I can see aliens (JSP)", browser.getTitle());
                WebElement usernameField = browser.findElement(USERNAME_FIELD);
                WebElement passwordField = browser.findElement(PASSWORD_FIELD);

                usernameField.sendKeys("-nonexisting-username-");
                passwordField.sendKeys("-nonexisting-password-");

                WebElement loginButton = browser.findElement(LOGIN_BUTTON);

                loginButton.click();
            }
        }).inspect(new Inspection() {
            // white-box
            private static final long serialVersionUID = 1L;

            @Inject
            @Current
            private UserSession userSession;

            @AfterServlet
            public void assertAfterServletRequestHasLanded() {
                User currentUser = userSession.getCurrentUser();
                Assert.assertNotNull("everytime an user should exist (but may be anonymous)", currentUser);
                Assert.assertTrue("wrong login should still have an anonymous user", currentUser.isAnonymous());
            }
        });
        Warp.initiate(new Activity() {
            // black-box

            @Override
            public void perform() {
                browser.navigate().to(deploymentURL.toExternalForm() + "logout.jsp");
            }
        }).inspect(new Inspection() {
            // white-box
            private static final long serialVersionUID = 1L;
        });
    }

}
