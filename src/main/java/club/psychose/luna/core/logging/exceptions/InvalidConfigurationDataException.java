package club.psychose.luna.core.logging.exceptions;

public class InvalidConfigurationDataException extends Exception{
    public InvalidConfigurationDataException (String message) {
        super(message, new Throwable());
    }
}