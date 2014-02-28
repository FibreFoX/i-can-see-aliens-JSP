package de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa;

import de.dynamicfiles.projects.testing.icanseealiens.jsp.model.BillboardEntry;
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
public class BillboardEntryManager {

    @javax.persistence.PersistenceContext(unitName = "billboard_PU")
    protected transient javax.persistence.EntityManager entityManager;

    @Inject
    @WasCreated
    private Event<BillboardEntry> billboardEntryCreatedEvent;
    @Inject
    @WillBeDeleted
    private Event<BillboardEntry> billboardEntryWillBeDeletedEvent;

    public void createEntry(User creatingUser, String title, String content) {
        if( !creatingUser.isAnonymous() ){
            BillboardEntry newEntry = new BillboardEntry(creatingUser, title, content);
            entityManager.persist(newEntry);
            billboardEntryCreatedEvent.fire(newEntry);
        }
    }

    public List<BillboardEntry> getAllEntries() {
        String jpql = "SELECT e FROM BillboardEntry e ORDER BY e.createdOn DESC";
        TypedQuery<BillboardEntry> query = entityManager.createQuery(jpql, BillboardEntry.class);
        List<BillboardEntry> results = query.getResultList();
        if( results == null || results.isEmpty() ){
            return new ArrayList();
        }
        return results;
    }

    /**
     * Note: Does not check, if deletion is allowed!
     *
     * @param id
     */
    public void removeEntryWithId(String id) {
        BillboardEntry entry = getBillboardEntry(id);
        deleteEntry(entry);
    }

    public void removeEntryWithId(int id) {
        BillboardEntry entry = getBillboardEntry(id);
        deleteEntry(entry);
    }

    private void deleteEntry(BillboardEntry entry) {
        if( entry == null ){
            return;
        }
        billboardEntryWillBeDeletedEvent.fire(entry);
        entityManager.remove(entry);
    }

    public BillboardEntry getBillboardEntry(String entryId) {
        int id = Integer.parseInt(entryId);
        return getBillboardEntry(id);
    }

    public BillboardEntry getBillboardEntry(int id) {
        String jpql = "SELECT e FROM BillboardEntry e WHERE e.id = :id";
        TypedQuery<BillboardEntry> query = entityManager.createQuery(jpql, BillboardEntry.class);
        query.setParameter("id", id);
        List<BillboardEntry> results = query.getResultList();
        if( results != null && !results.isEmpty() ){
            return results.get(0);
        }
        return null;
    }

    public void removeEntriesFromUser(User userToBeDeleted) {
        String jpql = "SELECT e FROM BillboardEntry e WHERE e.owningUser = :owner";
        TypedQuery<BillboardEntry> query = entityManager.createQuery(jpql, BillboardEntry.class);
        query.setParameter("owner", userToBeDeleted);
        List<BillboardEntry> results = query.getResultList();
        if( results != null && !results.isEmpty() ){
            for( BillboardEntry entry : results ){
                // remember to use this, because we are fireing events
                deleteEntry(entry);
            }
        }
    }
}
