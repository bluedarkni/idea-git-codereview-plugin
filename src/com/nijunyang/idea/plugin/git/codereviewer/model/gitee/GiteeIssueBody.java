package com.nijunyang.idea.plugin.git.codereviewer.model.gitee;

/**
 * Description:
 * Created by GW00295392 on 2024/3/4
 */
public class GiteeIssueBody {


    private String access_token;

    /**
     * 必填
     */
    private String title;

    /**
     * 仓库路径(path)
     */
    private String repo;

    /**
     * Issue描述
     */
    private String body;

    /**
     * Issue负责人
     */
    private String assignee;

    /**
     * Issue协助者, 以 , 分隔
     */
    private String collaborators;


    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(String collaborators) {
        this.collaborators = collaborators;
    }
}
