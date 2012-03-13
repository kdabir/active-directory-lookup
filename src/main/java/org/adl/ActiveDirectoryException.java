package org.adl;

/**
 * This is generic exception and underlying exception is wrapped in it
 * 
 * @author kdabir
 */
public class ActiveDirectoryException extends RuntimeException {

    public ActiveDirectoryException(Throwable cause) {
        super ("Problem accessing the Active Directory", cause);
    }

    public ActiveDirectoryException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
