package cn.xpp011.dingrobot.excepation;

/**
 * 钉钉机器人发送消息失败
 *
 * @author: xpp011 2022-08-09 15:25
 **/

public class DingRobotSendMsgFailException extends RuntimeException {

    public DingRobotSendMsgFailException() {
    }

    public DingRobotSendMsgFailException(String message) {
        super(message);
    }

    public DingRobotSendMsgFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public DingRobotSendMsgFailException(Throwable cause) {
        super(cause);
    }

    public DingRobotSendMsgFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
