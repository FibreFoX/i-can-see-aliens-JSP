package de.dynamicfiles.projects.testing.icanseealiens.jsp.session;

import de.dynamicfiles.projects.testing.icanseealiens.jsp.exceptions.NoPasswordProvided;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.exceptions.NoUserFound;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.UserManager;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.annotation.Transactional;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.SLSB;
import java.io.Serializable;
import javax.ejb.EJBException;
import javax.inject.Inject;

/**
 * Contains methods for logging in, registering, and having access to the current user of course ;)
 *
 * @author Danny Althoff
 */
public class UserSession implements Serializable {

    private static final long serialVersionUID = 1L;

    private User currentUser = new User();

    @Inject
    @SLSB
    private transient UserManager userManager;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Tries to login with the given credentials. If no user/password-combination was found, the user will still be anonymous.
     *
     * @param username
     * @param password has to be not-empty
     *
     * @return true when user could be found and password matches
     *
     * @throws de.dynamicfiles.projects.testing.icanseealiens.jsp.exceptions.NoUserFound
     *
     */
    public boolean login(String username, String password) throws NoUserFound {
        User foundUser = userManager.getUser(username);
        if( foundUser.isAnonymous() ){
            throw new NoUserFound();
        }
        if( foundUser.matchesPassword(password) ){
            setCurrentUser(foundUser);
            return true;
        }
        return false;
    }

    /**
     * Tries to register a new user with given credentials.
     *
     * @param username
     * @param password
     *
     * @return
     *
     * @throws NoPasswordProvided
     */
    @Transactional
    @SuppressWarnings("ThrowableResultIgnored")
    public boolean registerUser(String username, String password) throws NoPasswordProvided {
        boolean couldCreateUser = false;
        try{
            couldCreateUser = userManager.createUser(username, password);

        } catch(EJBException npp){
            Throwable cause = npp.getCause();
            // this will throw EJBException, so handle special handling later
            if( cause instanceof java.lang.AssertionError && cause.getCause() instanceof NoPasswordProvided ){
                // TODO improve this :(
                throw new NoPasswordProvided("couldn't create user", npp);
            }
        }
        return couldCreateUser;
    }
}
