package de.dynamicfiles.projects.testing.icanseealiens.jsp.exceptions;

/**
 * If no password was given, there will be this exception.
 *
 * @author Danny Althoff
 */
public class NoPasswordProvided extends Throwable {

    public NoPasswordProvided(String message) {
        super(message);
    }

    public NoPasswordProvided(String message, Throwable nestedException) {
        super(message, nestedException);
    }

}
