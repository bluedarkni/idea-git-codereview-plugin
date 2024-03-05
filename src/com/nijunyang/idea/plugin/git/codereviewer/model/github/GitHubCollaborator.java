package com.nijunyang.idea.plugin.git.codereviewer.model.github;

import com.nijunyang.idea.plugin.git.codereviewer.model.GitUser;

/**
 * Description:
 * Created by GW00295392 on 2024/3/5
 */
public class GitHubCollaborator extends GitUser {

    private Integer id;

    private String login;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return login;
    }
}
