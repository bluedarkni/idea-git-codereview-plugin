package com.nijunyang.idea.plugin.git.codereviewer.model.github;

import java.util.List;

/**
 * Description:
 * Created by nijunyang on 2024/3/4
 */
public class GitHubIssueBody {

    /**
     * 必填
     */
    private String title;

    /**
     * Issue描述
     */
    private String body;

    /**
     * Issue负责人
     */
    private List<String> assignees;

    /**
     * labels
     */
    private List<String> labels;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<String> assignees) {
        this.assignees = assignees;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }
}
