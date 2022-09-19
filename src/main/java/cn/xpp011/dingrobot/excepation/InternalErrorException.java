package cn.xpp011.dingrobot.excepation;

/**
 * 内部异常
 *
 * @author: xpp011 2022-09-05 22:58
 **/

public class InternalErrorException extends Exception {

    public InternalErrorException() {
        super();
    }

    public InternalErrorException(String message) {
        super(message);
    }

    public InternalErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalErrorException(Throwable cause) {
        super(cause);
    }

    protected InternalErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
