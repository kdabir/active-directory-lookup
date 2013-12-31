package io.github.kdabir.adl;

/**
 * This is thrown when searched user does not exist. client must code to catch this.
 * 
 * @author kdabir
 */
public class NotFoundException extends Exception {

    public NotFoundException() {
        super ("User not found");
    }

    public NotFoundException(String msg) {
        super(msg);
    }
}
