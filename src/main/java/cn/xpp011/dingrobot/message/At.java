package cn.xpp011.dingrobot.message;


import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * at
 *
 * @author: xpp011 2022-08-01 22:09
 **/

public class At implements Serializable {

    private List<String> atMobiles = new ArrayList<>();

    private List<String> atUserIds = new ArrayList<>();

    private boolean isAtAll;

    public List<String> getAtMobiles() {
        return atMobiles;
    }

    public List<String> getAtUserIds() {
        return atUserIds;
    }

    public boolean getIsAtAll() {
        return isAtAll;
    }

    private At(List<String> atMobiles, List<String> atUserIds, boolean isAtAll) {
        if (atMobiles != null) this.atMobiles = Collections.unmodifiableList(atMobiles);
        if (atUserIds != null) this.atUserIds = Collections.unmodifiableList(atUserIds);
        this.isAtAll = isAtAll;
    }

    public At() {
    }

    public static class Builder {

        private List<String> atMobiles;

        private List<String> atUserIds;

        private boolean isAtAll;

        public Builder setAtMobiles(List<String> atMobiles) {
            this.atMobiles = atMobiles;
            return this;
        }

        public Builder setAtUserIds(List<String> atUserIds) {
            this.atUserIds = atUserIds;
            return this;
        }

        public Builder setAtAll(Boolean atAll) {
            isAtAll = atAll;
            return this;
        }

        public At builder() {
            return new At(atMobiles, atUserIds, isAtAll);
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("atMobiles", atMobiles)
                .add("atUserIds", atUserIds)
                .add("isAtAll", isAtAll)
                .toString();
    }
}
