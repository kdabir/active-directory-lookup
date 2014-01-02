package io.github.kdabir.adl.exceptions;

/**
 * This checked exception is thrown in case of authentication failure (username
 * password mismatch) and client code must catch this. 
 * 
 * @author kdabir
 */
public class BadCredentialsException extends ActiveDirectoryException {

    public BadCredentialsException(Throwable cause) {
        super ("Invalid Username or Password", cause);
    }

    public BadCredentialsException(String msg, Throwable cause) {
        super (msg, cause);
    }
}
