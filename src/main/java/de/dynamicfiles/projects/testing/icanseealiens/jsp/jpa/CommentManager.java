package de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa;

import de.dynamicfiles.projects.testing.icanseealiens.jsp.model.BillboardEntry;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.model.Comment;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.model.User;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.WasCreated;
import de.dynamicfiles.projects.testing.icanseealiens.jsp.qualifier.WillBeDeleted;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.TypedQuery;

/**
 *
 * @author Danny Althoff
 */
@Stateless
public class CommentManager {

    @javax.persistence.PersistenceContext(unitName = "billboard_PU")
    protected transient javax.persistence.EntityManager entityManager;

    @Inject
    @WasCreated
    private Event<Comment> commentCreatedEvent;
    @Inject
    @WillBeDeleted
    private Event<Comment> commentWillBeDeletedEvent;

    public List<Comment> getComments(BillboardEntry billboardEntry) {
        if( billboardEntry == null ){
            return new ArrayList();
        }
        String jpql = "SELECT c FROM Comment c WHERE c.owningBillboardEntry = :entry ORDER BY c.id";

        TypedQuery<Comment> query = entityManager.createQuery(jpql, Comment.class);
        query.setParameter("entry", billboardEntry);

        List<Comment> results = query.getResultList();
        if( results == null || results.isEmpty() ){
            return new ArrayList();
        }
        return results;
    }

    public void deleteComments(BillboardEntry billboardEntry) {
        if( billboardEntry == null ){
            return;
        }
        deleteComments(getComments(billboardEntry));
    }

    private void deleteComments(List<Comment> comments) {
        for( Comment comment : comments ){
            deleteComment(comment);
        }
    }

    private void deleteComment(Comment comment) {
        commentWillBeDeletedEvent.fire(comment);
        entityManager.remove(comment);
    }

    public void createComment(User user, BillboardEntry entry, String message) {
        if( user == null || entry == null ){
            return;
        }
        Comment comment = new Comment(user, entry, message);
        entityManager.persist(comment);
        commentCreatedEvent.fire(comment);
    }

    public void removeCommentsFromUser(User owningUser) {
        String jpql = "SELECT c FROM Comment c WHERE c.owningUser = :user";
        TypedQuery<Comment> query = entityManager.createQuery(jpql, Comment.class);
        query.setParameter("user", owningUser);
        List<Comment> results = query.getResultList();

        if( results != null ){
            deleteComments(results);
        }
    }

}
