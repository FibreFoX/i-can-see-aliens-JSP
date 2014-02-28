package de.dynamicfiles.projects.testing.icanseealiens.jsp.observers;

import de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.BillboardEntryManager;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.CommentManager;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.SLSB;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.WasCreated;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.WillBeDeleted;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * Contains CDI-Events for User-instances.
 *
 * There is a bug, that no one can specify the order of Observers: https://issues.jboss.org/browse/CDI-4
 *
 * @author Danny Althoff
 */
public class UserObserver {
    
    @Inject
    @SLSB
    private BillboardEntryManager billboardEntryManager;
    @Inject
    @SLSB
    private CommentManager commentManager;

    public void observeUserBeingDeleted(@Observes @WillBeDeleted User userToBeDeleted) {
        // remove found billboard-entries
        billboardEntryManager.removeEntriesFromUser(userToBeDeleted);
        
        // remove found billboard-comments
        commentManager.removeCommentsFromUser(userToBeDeleted);
    }

    public void observeUserWasCreated(@Observes @WasCreated User userThatWasCreated) {
        // say hello, maybe send email or so
    }
}
