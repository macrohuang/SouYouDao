package exception;

public class ScenicException extends RuntimeException {
    public int errorCode;
    public ScenicException() {
        super();
    }

    public ScenicException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScenicException(String message) {
        super(message);
    }

    public ScenicException(Throwable cause) {
        super(cause);
    }

    public ScenicException(int errorCode, String msg) {
        this(msg);
        this.errorCode = errorCode;
    }

    public ScenicException(int errorCode, String msg, Throwable cause) {
        this(msg, cause);
        this.errorCode = errorCode;
    }
}
