package vn.edu.hcmute.service;

public class MailDeliveryException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MailDeliveryException(String message, Throwable cause) {
        super(message, cause);
    }
}
