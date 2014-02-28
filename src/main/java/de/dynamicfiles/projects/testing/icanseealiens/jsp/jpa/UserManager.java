package de.dynamicfiles.projects.testing.icanseealiens.jsp.jpa;

import de.dynamicfiles.projects.testing.icanseealiens.jsp.exceptions.NoPasswordProvided;
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
public class UserManager {

    @javax.persistence.PersistenceContext(unitName = "billboard_PU")
    protected transient javax.persistence.EntityManager entityManager;

    @Inject
    @WillBeDeleted
    private Event<User> userDeleteEvent;
    @Inject
    @WasCreated
    private Event<User> userCreatedEvent;

    /**
     * Needs Transactional-annotation.
     *
     * @param username
     * @param password
     *
     * @return true, if user could be created, otherwise returns false (whichs is when username already exists)
     *
     * @throws de.dynamicfiles.projects.testing.icanseealiens.jsp.exceptions.NoPasswordProvided
     *
     */
    public boolean createUser(String username, String password) throws NoPasswordProvided {
        // check if already exists
        if( getUser(username).isAnonymous() ){
            User userToCreate = new User(username, password);
            // avoid having "anonymous" as real user
            if( !userToCreate.isAnonymous() ){
                entityManager.persist(userToCreate);

                // we want to inform all, that this user was created :D (maybe someone is interested in)
                userCreatedEvent.fire(userToCreate);
                return true;
            }
        }
        return false;
    }

    /**
     * Needs Transactional-annotation on usage.
     *
     * @param username
     */
    public void deleteUser(String username) {
        // to avoid detached user being used, freshly search for it
        User foundUser = getUser(username);
        if( !foundUser.isAnonymous() ){
            userDeleteEvent.fire(foundUser);
            entityManager.remove(foundUser);
        }
    }

    /**
     * Tries to get the user with the given name. If no user with given name was found, the returned user is "anonymous" (but never returns null).
     *
     * @param username
     *
     * @return
     */
    public User getUser(String username) {
        // anonymous as default
        User foundUser = new User();
        String jpql = "SELECT u FROM User u WHERE u.name=:username";
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        query.setParameter("username", username);

        List<User> result = query.getResultList();
        if( result != null && !result.isEmpty() ){
            // the "name" is ID, so its only one item in the list
            foundUser = result.get(0);
        }

        return foundUser;
    }

    /**
     * This method does not get all users as User-class, to use less memory :)
     *
     * @return
     */
    public long getUserCount() {
        String jpql = "SELECT COUNT(u) FROM User u";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        List<Long> result = query.getResultList();
        if( result == null ){
            return 0;
        }
        // this should always be there
        return result.get(0);
    }

    /**
     * If there are no users, it returns a empty list
     *
     * @return
     */
    public List<User> getAllUsers() {
        String jpql = "SELECT u FROM User u ORDER BY u.name";
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        List<User> result = query.getResultList();
        if( result != null && !result.isEmpty() ){
            return result;
        }
        return new ArrayList();
    }
}
