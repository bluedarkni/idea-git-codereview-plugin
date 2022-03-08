package com.nijunyang.idea.plugin.git.codereviewer.model;

/**
 * Description:
 * Created by nijunyang on 2022/1/28 11:43
 */
public enum Channel {

    GIT_HUB("GitHub"),
    GIT_LAB("GitLab"),
    GIT_EE("Gitee");

    private final String aliasName;

    Channel(String aliasName) {
        this.aliasName = aliasName;
    }

    public static Channel getByAliasName(String aliasName) {
        for (Channel c: Channel.values()) {
            if (c.aliasName.equalsIgnoreCase(aliasName)) {
                return c;
            }
        }
        return null;
    }
}
