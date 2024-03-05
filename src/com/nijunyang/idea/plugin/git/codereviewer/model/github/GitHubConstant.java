package com.nijunyang.idea.plugin.git.codereviewer.model.github;

/**
 * Description:
 * Created by GW00295392 on 2024/3/4
 */
public class GitHubConstant {

    private GitHubConstant() {

    }

    public static final String AUTHORIZATION = "Authorization";

    public static final String BEARER = "Bearer ";


    /**
     * 获取用户的某个仓库  get   /repos/:owner/:repo
     * owner: 仓库所属空间地址(企业、组织或个人的地址path)
     * repo: 仓库路径(path)
     */
    public static final String OWNER_REPO_URL = "/repos/{0}/{1}";



    /**
     * 获取仓库的所有协作者  get   /repos/:owner/:repo/collaborators&page=xx&per_page=xx
     * owner: 仓库所属空间地址(企业、组织或个人的地址path)
     * repo: 仓库路径(path)
     */
    public static final String COLLABORATORS_URL = "/repos/{0}/{1}/collaborators";


    /**
     * 创建Issue  post   /repos/{owner}/{repo}/issues
     * owner: 仓库所属空间地址(企业、组织或个人的地址path)
     * repo: 仓库名字
     curl -L \
     -X POST \
     -H "Accept: application/vnd.github+json" \
     -H "Authorization: Bearer <YOUR-TOKEN>" \
     -H "X-GitHub-Api-Version: 2022-11-28" \
     https://api.github.com/repos/OWNER/REPO/issues \
     -d '{"title":"Found a bug","body":"I'\''m having a problem with this.","assignees":["octocat"],"milestone":1,"labels":["bug"]}'
     */
    public static final String ISSUE_URL = "/repos/{0}/{1}/issues";

}
