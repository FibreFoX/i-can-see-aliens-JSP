package de.dynamicfiles.projects.testing.icanseealiens.jsp.model;

import de.dynamicfiles.projects.testing.icanseealiens.jsp.exceptions.NoPasswordProvided;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Danny Althoff
 */
@Entity
@Table(name = "BILLBOARDUSER")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String ANONYMOUS_NAME = "-anonymous-";

    @Id
    private String name = ANONYMOUS_NAME;
    private String password = "";

    public User(String username, String password) throws NoPasswordProvided {
        this.name = username;
        setPassword(password);
    }

    // this has to be here, because of anonymous user :)
    public User() {
    }

    public String getName() {
        return name;
    }

    public boolean matchesPassword(String password) {
        return this.password.equals(password);
    }

    public boolean isAnonymous() {
        return ANONYMOUS_NAME.equals(name);
    }

    public void changePassword(String currentPassword, String newPassword) throws NoPasswordProvided {
        if( matchesPassword(currentPassword) ){
            try{
                setPassword(newPassword);
            } catch(NoPasswordProvided npp){
                throw new NoPasswordProvided("could not change password", npp);
            }
        }
    }

    private void setPassword(String password) throws NoPasswordProvided {
        if( password == null || password.trim().length() == 0 ){
            throw new NoPasswordProvided("given password was empty or null");
        }
        this.password = password;
    }

    @Override
    public boolean equals(Object potentialUser) {
        if( potentialUser instanceof User ){
            return this.getName().equals(((User) potentialUser).getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.name);
    }

}
