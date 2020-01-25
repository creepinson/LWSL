package xyz.baddeveloper.lwsl.exceptions;

public class ConnectException extends Exception {

    private Exception exception;

    public ConnectException(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }
}
