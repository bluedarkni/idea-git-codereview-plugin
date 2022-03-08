package com.nijunyang.idea.plugin.git.codereviewer.model;

import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.Vector;

/**
 * Description:
 * Created by nijunyang on 2022/1/27 14:44
 */
public class EventInfo<E extends GitUser> {

    public EventInfo(GitProject gitProject, String domain, Token token, AnActionEvent event, Vector<E> users) {
        this.gitProject = gitProject;
        this.domain = domain;
        this.token = token;
        this.event = event;
        this.users = users;
    }

    private GitProject gitProject;

    private String domain;

    private Token token;

    private Vector<E> users;

    private AnActionEvent event;


    public GitProject getGitProject() {
        return gitProject;
    }

    public void setGitProject(GitProject gitProject) {
        this.gitProject = gitProject;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
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
