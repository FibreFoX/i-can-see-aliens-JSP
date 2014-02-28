package de.dynamicfiles.projects.testing.icanseealiens.jsp.producer.slsb;

import de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa.CommentManager;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.SLSB;
import javax.ejb.EJB;
import javax.enterprise.inject.Produces;

/**
 *
 * @author Danny Althoff
 */
public class CommentManagerSLSB {

    @EJB
    private transient CommentManager commentManager;

    @Produces
    @SLSB
    public CommentManager getCommentManager() {
        return commentManager;
    }
}
