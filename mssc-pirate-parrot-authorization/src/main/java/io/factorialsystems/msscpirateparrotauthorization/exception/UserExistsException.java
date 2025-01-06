package io.factorialsystems.msscpirateparrotauthorization.exception;

public class UserExistsException extends RuntimeException{
    public UserExistsException(String msg) {
        super(msg);
    }
}
