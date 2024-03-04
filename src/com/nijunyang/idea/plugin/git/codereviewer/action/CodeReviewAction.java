package com.nijunyang.idea.plugin.git.codereviewer.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.nijunyang.idea.plugin.git.codereviewer.model.*;
import com.nijunyang.idea.plugin.git.codereviewer.model.gitee.Collaborator;
import com.nijunyang.idea.plugin.git.codereviewer.model.gitee.GiteeConstant;
import com.nijunyang.idea.plugin.git.codereviewer.model.gitee.GiteeRepository;
import com.nijunyang.idea.plugin.git.codereviewer.model.gitlab.GitLabConstant;
import com.nijunyang.idea.plugin.git.codereviewer.model.gitlab.GitLabRepository;
import com.nijunyang.idea.plugin.git.codereviewer.model.gitlab.GitLabUser;
import com.nijunyang.idea.plugin.git.codereviewer.utils.HttpUtil;
import com.nijunyang.idea.plugin.git.codereviewer.utils.PathUtil;
import com.nijunyang.idea.plugin.git.codereviewer.utils.TokenUtil;
import com.nijunyang.idea.plugin.git.codereviewer.utils.UrlUtil;
import com.nijunyang.idea.plugin.git.codereviewer.view.CodeReviewUI;
import com.nijunyang.idea.plugin.git.codereviewer.view.TokenConfigUI;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * Created by nijunyang on 2022/1/27 11:31
 */
public class CodeReviewAction extends AnAction {

//    public static volatile Token token;

    public static Map<String, Token> tokenMap = new ConcurrentHashMap<>(1);

    public static Map<String, LocalRepositoryInfo> projectInfoMap = new ConcurrentHashMap<>(1);

    /**
     * 1.从.git/config获取项目地址
     * 2.检查内存是有配置Access Token 如果有直接使用
     * 3.检查配置 .idea目录下是否配置了Access Token, 没有配置 弹出配置弹框, 如果配置了则加载
     * 4.根据解析出来项目名字查询获取项目信息，主要是后续的需要项目ID
     * 5.根据项目ID，查询该项目下有哪些人
     * 6.提交issue
     */
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project ideaProject = event.getProject();
        String localId = ideaProject.getLocationHash();
        LocalRepositoryInfo localRepositoryInfo = projectInfoMap.get(localId);
        if (localRepositoryInfo == null) {
            localRepositoryInfo = PathUtil.localRepositoryInfo(Objects.requireNonNull(ideaProject));
            projectInfoMap.put(localId, localRepositoryInfo);
        }
        Token token = tokenMap.get(localId);
        if (token == null) {
            token = TokenUtil.checkOrLoadToken(ideaProject);
            if (Objects.isNull(token)) {
                TokenConfigUI.setToken(ideaProject);
            } else {
                tokenMap.put(localId, token);
                showIssueDialog(event, localRepositoryInfo, token);
            }
        } else {
            showIssueDialog(event, localRepositoryInfo, token);
        }
    }

    private void showIssueDialog(@NotNull AnActionEvent event, LocalRepositoryInfo localRepositoryInfo, Token token) {
        String domain = localRepositoryInfo.getDomain();
        String repositoryName = localRepositoryInfo.getName();
        String url = localRepositoryInfo.getUrl();


        //获取仓库信息
        GitRepository gitRepository = null;
        if (token.getChannel() == Channel.GIT_LAB) {
            String repositoryInfoUrl = HttpUtil.HTTPS_PROTOCOL + domain +
                    MessageFormat.format(GitLabConstant.PROJECT_INFO_URL, repositoryName);
            Map<String, String> headers = new HashMap<>();
            headers.put(GitLabConstant.HEADER_PRIVATE_TOKEN, token.getPrivateKey());
            String gitProjectStr = HttpUtil.getWithHeader(repositoryInfoUrl, headers);
            List<GitLabRepository> projectList = JSON.parseObject(gitProjectStr, new TypeReference<List<GitLabRepository>>() {
            });
            Optional<GitLabRepository> projectOptional = projectList.stream()
                    .filter(e -> e.getHttp_url_to_repo().equals(url) || e.getSsh_url_to_repo().equals(url))
                    .findFirst();
            if (projectOptional.isPresent()) {
                gitRepository = projectOptional.get();
            }
        } else if (token.getChannel() == Channel.GIT_EE) {
            String owner = localRepositoryInfo.getGroupPath();
            String repositoryInfoUrl = HttpUtil.HTTPS_PROTOCOL + domain +
                    MessageFormat.format(GiteeConstant.OWNER_REPO_URL, owner, repositoryName, token.getPrivateKey());
            GiteeRepository giteeRepository = HttpUtil.getWithNoHeader(repositoryInfoUrl, GiteeRepository.class);
            gitRepository = giteeRepository;
        } else if (token.getChannel() == Channel.GIT_HUB) {
            //todo
        }
        assert gitRepository != null;
        //获取该仓库用户信息
        Vector<? extends GitUser> users = new Vector<>();
        if (token.getChannel() == Channel.GIT_LAB) {
            String usersUrl = HttpUtil.HTTPS_PROTOCOL + domain +
                    MessageFormat.format(GitLabConstant.PROJECT_USERS_URL, gitRepository.getId());
            Map<String, String> headers = new HashMap<>();
            headers.put(GitLabConstant.HEADER_PRIVATE_TOKEN, token.getPrivateKey());
            String responseStr = HttpUtil.getWithHeader(usersUrl, headers);
            users = JSON.parseObject(responseStr, new TypeReference<Vector<GitLabUser>>() {});
        } else if (token.getChannel() == Channel.GIT_EE) {
            String owner = localRepositoryInfo.getGroupPath();
            String usersUrl = HttpUtil.HTTPS_PROTOCOL + domain +
                    MessageFormat.format(GiteeConstant.COLLABORATORS_URL, owner, repositoryName, token.getPrivateKey());
            String responseStr = HttpUtil.getWithNoHeader(usersUrl);
            users = JSON.parseObject(responseStr, new TypeReference<Vector<Collaborator>>() {});
        } else if (token.getChannel() == Channel.GIT_HUB) {
            //todo
        }
        CodeReviewUI.showIssueDialog(new EventInfo(gitRepository, localRepositoryInfo, token, event, users), event.getProject());
    }

}
