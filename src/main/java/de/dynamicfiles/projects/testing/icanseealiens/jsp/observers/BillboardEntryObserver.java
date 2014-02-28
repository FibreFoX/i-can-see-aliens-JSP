package de.dynamicfiles.projects.testing.icanseealiens.jsp.observers;

import de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.CommentManager;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.model.BillboardEntry;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.SLSB;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.WillBeDeleted;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 *
 * @author Danny Althoff
 */
public class BillboardEntryObserver {

    @Inject
    @SLSB
    private CommentManager commentManager;

    /**
     * When an billboard-entry will be deleted, this method will be called by CDI.
     *
     * @param entry
     */
    public void observeBillboardEntryBeingDeleted(@Observes @WillBeDeleted BillboardEntry entry) {
        commentManager.deleteComments(entry);
    }
}
