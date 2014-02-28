package de.dynamicfiles.projects.testing.icanseealiens.jsp.unittest;

import de.dynamicfiles.projects.testing.icanseealiens.jsp.exceptions.NoPasswordProvided;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 *
 * @author Danny Althoff
 */
public class User {

    @Test
    public void createUser() {
        assertNotNull(new de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User(), "create user with default-constructor");
    }

    @Test(expectedExceptions = NoPasswordProvided.class, description = "create user with all values having NULL")
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void createUser_withNull() throws NoPasswordProvided {
        new de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User(null, null);
    }

    @Test
    public void createUser_withNoUsername() throws NoPasswordProvided {
        assertNotNull(new de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User(null, "password"), "create user with empty username");
    }

    @Test(expectedExceptions = NoPasswordProvided.class, description = "create user with empty password (null)")
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void createUser_withNoPassword_withNull() throws NoPasswordProvided {
        new de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User("username", null);
    }

    @Test(expectedExceptions = NoPasswordProvided.class, description = "create user with empty password (empty)")
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void createUser_withNoPassword_withEmptyString() throws NoPasswordProvided {
        new de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User("username", "");
    }

    @Test
    public void createUser_withValidData() throws NoPasswordProvided {
        assertNotNull(new de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User("username", "password"), "create user with all data");
    }

    @Test
    public void matchesPassword() throws NoPasswordProvided {
        String username = "username";
        String password = "password";
        de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User user = new de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User(username, password);
        assertTrue(user.matchesPassword(password));
    }

    @Test
    public void matchesPasswordNot() throws NoPasswordProvided {
        String username = "username";
        String password = "password";
        String notTheSamePassword = "notTheSamePassword";
        de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User user = new de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User(username, password);
        assertFalse(user.matchesPassword(notTheSamePassword));
    }

    @Test
    public void checkAnonymous() {
        assertTrue(new de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User().isAnonymous(), "new user should be anonymous");
    }

    @Test
    public void checkNotAnonymous() throws NoPasswordProvided {
        String username = "username";
        String password = "password";
        assertFalse(new de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User(username, password).isAnonymous(), "user with data should not be anonymous");
    }

    @Test(expectedExceptions = NoPasswordProvided.class)
    public void changePassword_withNull() throws NoPasswordProvided {
        String username = "username";
        String password = "password";
        String newPassword = null;
        de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User user = new de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User(username, password);
        user.changePassword(password, newPassword);
    }

    @Test(expectedExceptions = NoPasswordProvided.class)
    public void changePassword_withEmptyString() throws NoPasswordProvided {
        String username = "username";
        String password = "password";
        String newPassword = "";
        de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User user = new de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User(username, password);
        user.changePassword(password, newPassword);
    }

    @Test
    public void changePassword_withFalsePassword() throws NoPasswordProvided {
        String username = "username";
        String password = "password";
        String falsePassword = "falsePassword";
        de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User user = new de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User(username, password);
        user.changePassword(falsePassword, "not relevant");
    }

    @Test
    public void changePassword() throws NoPasswordProvided {
        String username = "username";
        String password = "password";
        String newPassword = "newPassword";
        de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User user = new de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User(username, password);
        user.changePassword(password, newPassword);
    }

    @Test
    public void checkUsername() throws NoPasswordProvided {
        String username = "username";
        String password = "password";
        de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User user = new de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User(username, password);
        assertTrue(username.equals(user.getName()));
    }

    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void compareWith() throws NoPasswordProvided {
        String username = "username";
        String password = "password";
        String secondUsername = "secondUsername";
        String secondPassword = "secondPassword";
        de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User user = new de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User(username, password);
        de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User secondUser = new de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User(secondUsername, secondPassword);
        de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User thirdUser = new de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User(username, password);
        assertFalse(user.equals(secondUser));
        assertTrue(user.equals(user));
        assertTrue(user.equals(thirdUser), "equals looks for the username, so this should be true");
        assertFalse(user.equals(new Exception()));

    }
}
