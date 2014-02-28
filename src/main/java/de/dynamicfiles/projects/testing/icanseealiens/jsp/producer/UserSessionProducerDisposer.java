package de.dynamicfiles.projects.testing.icanseealiens.jsp.producer;

import de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.Current;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.session.UserSession;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.New;
import javax.enterprise.inject.Produces;

/**
 *
 * @author Danny Althoff
 */
public class UserSessionProducerDisposer {

    @Produces
    @Current
    @SessionScoped
    public UserSession getUser(@New UserSession userSession) {
        return userSession;
    }

    /**
     * Has to be in the same class as the producer.
     *
     * @param userSession will be injected
     */
    public void disposeUserSession(@Disposes @Current UserSession userSession) {
        // here we could write to database, that the session of the user is outdated/invalid, out simply single-log-out
        userSession.setCurrentUser(null);
    }

}
