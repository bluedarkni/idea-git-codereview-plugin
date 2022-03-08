package com.nijunyang.idea.plugin.git.codereviewer.model.gitlab;

/**
 * Description: GitLab常量
 * Created by nijunyang on 2022/1/28 11:09
 */
public class GitLabConstant {


    public static final String HEADER_PRIVATE_TOKEN = "PRIVATE-TOKEN";

    /**
     * 项目信息
     */
    public static final String PROJECT_INFO_URL = "/api/v4/projects?search={0}";

    /**
     * 项目用户列表url
     */
    public static final String PROJECT_USERS_URL = "/api/v4/projects/{0,number,#}/users";

    /**
     * issue创建url
     */
    public static final String ISSUE_URL = "/api/v4/projects/{0,number,#}/issues";
}
