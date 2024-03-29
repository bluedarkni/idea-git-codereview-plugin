package com.nijunyang.idea.plugin.git.codereviewer.model;


/**
 * Description: 代码仓库信息
 * Created by nijunyang on 2022/2/9 9:45
 */
public class LocalRepositoryInfo {
    /**
     * 仓库地址
     */
    private String url;
    /**
     * 域名
     */
    private String domain;
    /**
     * 仓库名字
     */
    private String name;

    /**
     * 仓库分组路径
     */
    private String groupPath;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupPath() {
        return groupPath;
    }

    public void setGroupPath(String groupPath) {
        this.groupPath = groupPath;
    }
}
