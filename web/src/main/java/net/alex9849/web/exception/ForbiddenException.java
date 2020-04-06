package net.alex9849.web.exception;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException() {
        super("Not permitted");
    }

    public ForbiddenException(String msg) {
        super(msg);
    }
}
