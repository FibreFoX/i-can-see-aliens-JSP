package de.dynamicfiles.projects.testing.icanseealiens.jsp.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entry filled with "encountered by aliens"-stories
 *
 * @author Danny Althoff
 */
@Entity
public class BillboardEntry implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    private User owningUser;
    private String title;
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private final Date createdOn;

    public BillboardEntry(User creatingUser, String title, String content) {
        this.createdOn = new Date();
        this.owningUser = creatingUser;
        this.title = title;
        this.content = content;
    }

    public BillboardEntry() {
        this.createdOn = new Date();
    }

    public String getContent() {
        return content;
    }

    public User getOwningUser() {
        return owningUser;
    }

    public String getTitle() {
        return title;
    }

    /**
     * Returns a new date-object.
     *
     * @return
     */
    public Date getCreatedOn() {
        return new Date(createdOn.getTime());
    }

    /**
     * Returns the persistence-ID
     * @return 
     */
    public int getId() {
        return id;
    }

}
