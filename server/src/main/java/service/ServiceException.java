package service;


/**
 * Indicates there was an error with the service
 */
public class ServiceException extends Exception{
    public ServiceException(String message) {
        super(message);
    }
}
