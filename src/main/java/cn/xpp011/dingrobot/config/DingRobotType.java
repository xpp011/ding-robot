package cn.xpp011.dingrobot.config;

/**
 * @author: xpp011 2022-09-10 23:26
 **/

public enum DingRobotType {
    REDIS(RedisDingRobotConfiguration.class.getName()),
    SIMPLE(SimpleDingRobotConfiguration.class.getName()),
    ;

    private String className;

    DingRobotType(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
}
