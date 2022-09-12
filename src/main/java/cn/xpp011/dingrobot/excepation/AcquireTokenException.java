package cn.xpp011.dingrobot.excepation;

/**
 * @program: ding-robot
 * @description: 获取令牌失败异常
 * @author: xpp011
 * @create: 2022-08-02 13:19
 **/

public class AcquireTokenException extends RuntimeException {

    public AcquireTokenException() {
    }

    public AcquireTokenException(String message) {
        super(message);
    }

    public AcquireTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public AcquireTokenException(Throwable cause) {
        super(cause);
    }

    public AcquireTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
