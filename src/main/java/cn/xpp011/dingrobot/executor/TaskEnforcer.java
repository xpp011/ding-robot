package cn.xpp011.dingrobot.executor;


/**
 * @program: ding-robot
 * @description: 执行器
 * @author: xpp011
 * @create: 2022-08-18 22:45
 **/

public interface TaskEnforcer<T> {

    <R> R execute(T params);

}
