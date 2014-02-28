package de.dynamicfiles.projects.testing.icanseealiens.jsp.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author Danny Althoff
 */
@Entity
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private long id;
    private String message = "";

    @OneToOne
    private BillboardEntry owningBillboardEntry = null;

    @OneToOne
    private User owningUser = null;

    public Comment(User user, BillboardEntry billboardEntry, String message) {
        this.message = message;
        this.owningBillboardEntry = billboardEntry;
        this.owningUser = user;
    }

    public Comment() {

    }

    public String getMessage() {
        return message;
    }

    public long getId() {
        return id;
    }

    public BillboardEntry getOwningBillboardEntry() {
        return owningBillboardEntry;
    }

    public User getOwningUser() {
        return owningUser;
    }

}
