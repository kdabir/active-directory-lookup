package io.github.kdabir.adl.exceptions;

/**
 * This is thrown when searched user does not exist. client must code to catch this.
 * 
 * @author kdabir
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super ("User not found");
    }

    public NotFoundException(String msg) {
        super(msg);
    }
}
