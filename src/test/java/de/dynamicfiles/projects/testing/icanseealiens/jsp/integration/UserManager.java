package de.dynamicfiles.projects.testing.icanseealiens.jsp.integration;

import de.dynamicfiles.projects.testing.icanseealiens.jsp.exceptions.NoPasswordProvided;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User;
import java.io.File;
import java.util.List;
import javax.ejb.EJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 *
 * @author Danny Althoff
 */
public class UserManager extends Arquillian {

    @Deployment
    public static JavaArchive getDeployment() {
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "UserManager-test.jar");
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.UserManager.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.exceptions.NoPasswordProvided.class);
        archive.addClass(de.dynamicfiles.projects.testing.icanseealiens.jsp.exceptions.NoUserFound.class);
        archive.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        archive.addAsResource(new File("src/main/resources/META-INF/persistence.xml"), "META-INF/persistence.xml");
        return archive;
    }

    @EJB
    private de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.UserManager userManager;

    @BeforeMethod
    public void assertInjected() {
        assertNotNull(userManager, "there should be an EJB being injected by the container");
    }

    @Test
    public void userCountOnFreshDatabase() {
        assertEquals(userManager.getAllUsers().size(), 0, "fresh database should have no entries");
        assertEquals(userManager.getUserCount(), 0, "fresh database should have no entry-count (aggregate-query)");
    }

    @Test
    public void storeUserAndTryToGetIt() throws NoPasswordProvided {
        String username = "username";
        String password = "password";
        assertTrue(userManager.createUser(username, password));
        User user = userManager.getUser(username);
        assertNotNull(user, "we created that user, so it should be there");
        assertEquals(user.getName(), username);
        assertEquals(userManager.getUserCount(), 1, "added user should increase user-count");
    }

    @Test
    public void tryToStoreUserAgain() throws NoPasswordProvided {
        storeUserAndTryToGetIt();

        String username = "username";
        String password = "password";
        assertFalse(userManager.createUser(username, password));
    }

    @Test
    public void tryToCreateInvalidUser() throws NoPasswordProvided {
        String username = User.ANONYMOUS_NAME;
        String password = "password";
        assertFalse(userManager.createUser(username, password));
    }

    @Test
    public void tryToGetNonExistingUser() {
        User user = userManager.getUser("NONEXISTING");
        assertNotNull(user);
        assertTrue(user.isAnonymous());
    }

    @Test
    public void tryToDeleteNonExistingUser() {
        // TODO this may make a note, that the user doesn't exist (exception vs boolen-feedback)
        userManager.deleteUser("NONEXISTING 2");
    }

    @AfterMethod
    public void cleanEntries() {
        // TODO this should be refactored via direct Persistence-injection
        List<User> allUsers = userManager.getAllUsers();
        for( User singleUser : allUsers ){
            userManager.deleteUser(singleUser.getName());
        }
    }
}
