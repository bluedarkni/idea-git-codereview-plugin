package com.nijunyang.idea.plugin.git.codereviewer.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.nijunyang.idea.plugin.git.codereviewer.model.Channel;
import com.nijunyang.idea.plugin.git.codereviewer.model.EventInfo;
import com.nijunyang.idea.plugin.git.codereviewer.model.GitProject;
import com.nijunyang.idea.plugin.git.codereviewer.model.ProjectInfo;
import com.nijunyang.idea.plugin.git.codereviewer.model.Token;
import com.nijunyang.idea.plugin.git.codereviewer.model.gitlab.GitLabConstant;
import com.nijunyang.idea.plugin.git.codereviewer.model.gitlab.GitLabProject;
import com.nijunyang.idea.plugin.git.codereviewer.model.gitlab.GitLabUser;
import com.nijunyang.idea.plugin.git.codereviewer.utils.HttpUtil;
import com.nijunyang.idea.plugin.git.codereviewer.utils.PathUtil;
import com.nijunyang.idea.plugin.git.codereviewer.utils.TokenUtil;
import com.nijunyang.idea.plugin.git.codereviewer.utils.UrlUtil;
import com.nijunyang.idea.plugin.git.codereviewer.view.CodeReviewUI;
import com.nijunyang.idea.plugin.git.codereviewer.view.TokenConfigUI;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * Created by nijunyang on 2022/1/27 11:31
 */
public class CodeReviewAction extends AnAction {

//    public static volatile Token token;

    public static Map<String, Token> tokenMap = new ConcurrentHashMap<>(1);

    public static Map<String, ProjectInfo> projectInfoMap = new ConcurrentHashMap<>(1);

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
        ProjectInfo projectInfo = projectInfoMap.get(localId);
        if (projectInfo == null) {
            String projectUrl = PathUtil.loadProjectUrl(Objects.requireNonNull(ideaProject));
            String domain = UrlUtil.parseDomainName(projectUrl);
            String projectName = UrlUtil.parseProjectName(projectUrl);
            projectInfo = new ProjectInfo();
            projectInfo.setProjectUrl(projectUrl);
            projectInfo.setDomain(domain);
            projectInfo.setProjectName(projectName);
            projectInfoMap.put(localId, projectInfo);
        }
        Token token = tokenMap.get(localId);
        if (token == null) {
            token = TokenUtil.checkOrLoadToken(ideaProject);
            if (Objects.isNull(token)) {
                TokenConfigUI.setToken(ideaProject);
            } else {
                tokenMap.put(localId, token);
                showIssueDialog(event, projectInfo.getDomain(), projectInfo.getProjectName(), token);
            }
        } else {
            showIssueDialog(event, projectInfo.getDomain(), projectInfo.getProjectName(), token);
        }
    }

    private void showIssueDialog(@NotNull AnActionEvent event, String domain, String projectName, Token token) {
        //获取项目信息
        GitProject gitProject = null;
        if (token.getChannel() == Channel.GIT_LAB) {
            String projectInfoUrl = "https://" + domain +
                    MessageFormat.format(GitLabConstant.PROJECT_INFO_URL, projectName);
            Map<String, String> headers = new HashMap<>();
            headers.put(GitLabConstant.HEADER_PRIVATE_TOKEN, token.getPrivateKey());
            String gitProjectStr = HttpUtil.getWithHeader(projectInfoUrl, headers);
            List<GitLabProject> projectList = JSON.parseObject(gitProjectStr, new TypeReference<List<GitLabProject>>() {
            });
            gitProject = projectList.get(0);
        }
        assert gitProject != null;
        //获取该项目用户信息
        Vector<?> users = new Vector<>();
        if (token.getChannel() == Channel.GIT_LAB) {
            String usersUrl = "https://" + domain +
                    MessageFormat.format(GitLabConstant.PROJECT_USERS_URL, gitProject.getId());
            Map<String, String> headers = new HashMap<>();
            headers.put(GitLabConstant.HEADER_PRIVATE_TOKEN, token.getPrivateKey());
            String responseStr = HttpUtil.getWithHeader(usersUrl, headers);
            users = JSON.parseObject(responseStr, new TypeReference<Vector<GitLabUser>>() {});

        }
        CodeReviewUI.showIssueDialog(new EventInfo(gitProject, domain, token, event, users));
    }

}
