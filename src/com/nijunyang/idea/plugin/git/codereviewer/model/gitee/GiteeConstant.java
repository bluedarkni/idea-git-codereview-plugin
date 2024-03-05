package com.nijunyang.idea.plugin.git.codereviewer.model.gitee;

/**
 * Description:
 * Created by nijunyang on 2024/3/4
 */
public class GiteeConstant {

    private GiteeConstant() {

    }

    public static final String ACCESS_TOKEN = "access_token";


    /**
     * 获取用户的某个仓库  get   api/v5/repos/{owner}/{repo}?access_token=xxx
     * owner: 仓库所属空间地址(企业、组织或个人的地址path)
     * repo: 仓库路径(path)
     */
    public static final String OWNER_REPO_URL = "/api/v5/repos/{0}/{1}?access_token={2}";



    /**
     * 获取仓库的所有协作者  get   api/v5/repos/{owner}/{repo}/collaborators?access_token=xxx&page=xx&per_page=xx
     * owner: 仓库所属空间地址(企业、组织或个人的地址path)
     * repo: 仓库路径(path)
     */
    public static final String COLLABORATORS_URL = "/api/v5/repos/{0}/{1}/collaborators?access_token={2}";


    /**
     * 创建Issue  post   api/v5/repos/{owner}/issues
     * owner: 仓库所属空间地址(企业、组织或个人的地址path)
     */
    public static final String ISSUE_URL = "/api/v5/repos/{0}/issues";



}
