package de.dynamicfiles.projects.testing.icanseealiens.jsp.functional;

import java.io.File;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.junit.Arquillian;
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
public class Startpage {

    public static final String SCREENSHOT_DIR = "target/screenshots";

    @Drone
    private WebDriver browser;

    @Deployment(testable = false)
    public static WebArchive getDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "Startpage-test.war");

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
        archive.addAsWebResource(new File("src/main/webapp/register.jsp"));
        archive.addAsWebResource(new File("src/main/webapp/index.jsp"));

        return archive;
    }

    @Test
    public void checkExistingLinks(@ArquillianResource URL deploymentURL) {
        Assert.assertNotNull("there should be a browser", browser);
        browser.navigate().to(deploymentURL.toExternalForm() + "index.jsp");
        Assert.assertEquals("title should be as accepted", Helper.TITLE_STARTPAGE, browser.getTitle());

        WebElement createAccountButton = browser.findElement(Helper.STARTPAGE_REGISTER_BUTTON);
        Assert.assertNotNull("there should be a 'create account'-button on the start-page", createAccountButton);

        WebElement loginButton = browser.findElement(Helper.STARTPAGE_LOGIN_BUTTON);
        Assert.assertNotNull("there should be a 'login'-button on the start-page", loginButton);
        captureScreenshot("checkExistingLinks");
    }

    @Test
    public void checkLinkToRegister(@ArquillianResource URL deploymentURL) {
        Assert.assertNotNull("there should be a browser", browser);
        browser.navigate().to(deploymentURL.toExternalForm() + "index.jsp");

        WebElement createAccountButton = browser.findElement(Helper.STARTPAGE_REGISTER_BUTTON);
        Assert.assertNotNull("there should be a 'create account'-button on the start-page", createAccountButton);

        // this will jump to another page
        createAccountButton.click();

        Assert.assertEquals("After clicking on 'create account' we should be able to create one user", Helper.TITLE_REGISTER, browser.getTitle());
        captureScreenshot("checkLinkToRegister");
    }

    @Test
    public void checkLinkToLogIn(@ArquillianResource URL deploymentURL) {
        Assert.assertNotNull("there should be a browser", browser);
        browser.navigate().to(deploymentURL.toExternalForm() + "index.jsp");

        WebElement loginButton = browser.findElement(Helper.STARTPAGE_LOGIN_BUTTON);
        Assert.assertNotNull("there should be a 'login'-button on the start-page", loginButton);

        // this will jump to another page
        loginButton.click();

        Assert.assertEquals("After clicking on 'login' we should be able to login", Helper.TITLE_LOGIN, browser.getTitle());
        captureScreenshot("checkLinkToLogIn");
    }

    @ArquillianResource
    private TakesScreenshot screenshotter;

    public void captureScreenshot(String fileName) {
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
