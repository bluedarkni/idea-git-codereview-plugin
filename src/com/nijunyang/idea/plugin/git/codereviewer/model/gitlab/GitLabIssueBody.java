package com.nijunyang.idea.plugin.git.codereviewer.model.gitlab;


/**
 * Description:
 * Created by nijunyang on 2022/1/27 18:47
 */
public class GitLabIssueBody {

    public GitLabIssueBody(Integer assigneeId, String assigneeIds, String description, String title) {
        this.assigneeId = assigneeId;
        this.assigneeIds = assigneeIds;
        this.description = description;
        this.title = title;
    }

    /**
     * 分配给谁
     */
    public Integer assigneeId;

    /**
     * 分配给谁 高版本gitlab使用这个 ,分隔
     */
    public String assigneeIds;
    /**
     * issue描述
     */
    public String description;
    /**
     * issue标题
     */
    public String title;


    public String convertToPathUrl () {
        String str = "description=" + description + "&" +
                "title=" + title;
        if (assigneeId != null) {
            str = str + "&" +"assignee_id=" + assigneeId;
        }
        if (assigneeIds != null) {
            str = str + "&" +"assignee_ids=" + assigneeIds;
        }
        return  str;


    }


}
