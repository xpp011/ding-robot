package cn.xpp011.dingrobot;

import sun.net.ConnectionResetException;

import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;

/**
 * @author: xpp011 2022-09-03 22:51
 **/

public class ExceptionUtil {

    /**
     * 判断是否为网络异常
     *
     * @param e 异常
     * @return 是否属于网络异常
     */
    public static boolean isNetworkException(Throwable e) {
        try {
            if (e == null) return false;
            if (e instanceof ConnectionResetException
                    || e instanceof ConnectException
                    || e instanceof SocketTimeoutException
                    || e instanceof NoRouteToHostException) {
                return true;
            }
            return isNetworkException(e.getCause());
        } catch (Throwable ex) {
            return false;
        }
    }

}
