package com.nijunyang.idea.plugin.git.codereviewer.model;


/**
 * Description: 项目信息
 * Created by nijunyang on 2022/2/9 9:45
 */
public class ProjectInfo {
    /**
     * 项目地址
     */
    private String projectUrl;
    /**
     * 域名
     */
    private String domain;
    /**
     * 项目名字
     */
    private String projectName;

    public String getProjectUrl() {
        return projectUrl;
    }

    public void setProjectUrl(String projectUrl) {
        this.projectUrl = projectUrl;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
