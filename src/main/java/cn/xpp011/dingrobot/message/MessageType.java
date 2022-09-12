package cn.xpp011.dingrobot.message;

public enum MessageType {

    TEXT("text"),
    LINK("link"),
    MARKDOWN("markdown"),
    ACTION_CARD("actionCard"),
    FEED_CARD("feedCard"),
    ;

    private String type;

    public String getType() {
        return type;
    }

    MessageType(String type) {
        this.type = type;
    }

}
