package com.nijunyang.idea.plugin.git.codereviewer.model;

import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.Vector;

/**
 * Description:
 * Created by nijunyang on 2022/1/27 14:44
 */
public class EventInfo<E extends GitUser> {

    public EventInfo(GitRepository gitRepository, LocalRepositoryInfo localRepositoryInfo, Token token, AnActionEvent event, Vector<E> users) {
        this.gitRepository = gitRepository;
        this.localRepositoryInfo = localRepositoryInfo;
        this.token = token;
        this.event = event;
        this.users = users;
    }

    private GitRepository gitRepository;

//    private String domain;

    private LocalRepositoryInfo localRepositoryInfo;

    private Token token;

    private Vector<E> users;

    private AnActionEvent event;


    public GitRepository getGitRepository() {
        return gitRepository;
    }

    public void setGitRepository(GitRepository gitRepository) {
        this.gitRepository = gitRepository;
    }

    public LocalRepositoryInfo getLocalRepositoryInfo() {
        return localRepositoryInfo;
    }

    public void setLocalRepositoryInfo(LocalRepositoryInfo localRepositoryInfo) {
        this.localRepositoryInfo = localRepositoryInfo;
    }

    public String getDomain() {
        return localRepositoryInfo.getDomain();
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public AnActionEvent getEvent() {
        return event;
    }

    public void setEvent(AnActionEvent event) {
        this.event = event;
    }

    public Vector<E> getUsers() {
        return users;
    }

    public void setUsers(Vector<E> users) {
        this.users = users;
    }
}
