package org.adl;

/**
 * This checked exception is thrown in case of authentication failure (username
 * password mismatch) and client code must catch this. 
 * 
 * @author kdabir
 */
public class BadCredentialsException extends Exception {

    public BadCredentialsException() {
        super ("Invalid Username or Password");
    }

    public BadCredentialsException(String msg) {
        super (msg);
    }
}
