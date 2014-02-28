package de.dynamicfiles.projects.testing.icanseealiens.jsp.integration;

import de.dynamicfiles.projects.testing.icanseealiens.jsp.exceptions.NoPasswordProvided;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.exceptions.NoUserFound;
import java.io.File;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Danny Althoff
 */
public class UserSession extends Arquillian {

    @Deployment
    public static JavaArchive getDeployment() {
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "UserSession-test.jar");
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.UserManager.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.SLSB.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.session.UserSession.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.producer.slsb.UserManagerSLSB.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.interceptor.TransactionInterceptor.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.annotation.Transactional.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.exceptions.NoPasswordProvided.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.exceptions.NoUserFound.class);
        archive.addAsManifestResource(new File("src/main/webapp/WEB-INF/beans.xml"), "beans.xml");
        archive.addAsResource(new File("src/main/resources/META-INF/persistence.xml"), "META-INF/persistence.xml");
        return archive;
    }

    @Inject
    private de.dynamicfiles.projects.testing.icanseealiens.jsp.session.UserSession userSession;

    @BeforeMethod
    public void assertInjected() {
        assertNotNull(userSession, "there should be a bean being injected by the container (without scoping)");
    }

    @Test(expectedExceptions = de.dynamicfiles.projects.testing.icanseealiens.jsp.exceptions.NoPasswordProvided.class)
    public void checkUserSession() throws de.dynamicfiles.projects.testing.icanseealiens.jsp.exceptions.NoPasswordProvided {
        userSession.registerUser("", "");
    }

    @Test
    public void checkUserNotBeingAddableTwice() throws NoPasswordProvided {
        String username = "username";
        String password = "password";
        assertTrue(userSession.registerUser(username, password));
        assertFalse(userSession.registerUser(username, password));
    }

    @Test
    public void checkUserAbleToLoginAfterCreation() throws NoPasswordProvided, NoUserFound {
        String username = "usernameLoginCheck";
        String password = "passwordLoginCheck";
        String wrongPassword = "WRONG";
        assertTrue(userSession.registerUser(username, password));
        assertTrue(userSession.login(username, password));
        assertFalse(userSession.login(username, wrongPassword));
    }

    @Test(expectedExceptions = NoUserFound.class)
    public void checkNonExistingUser() throws NoPasswordProvided, NoUserFound {
        String username = "i dont exist";
        String password = "password";
        userSession.login(username, password);
    }
}
