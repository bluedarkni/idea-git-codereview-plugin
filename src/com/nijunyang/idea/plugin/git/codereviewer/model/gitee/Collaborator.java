package com.nijunyang.idea.plugin.git.codereviewer.model.gitee;

import com.nijunyang.idea.plugin.git.codereviewer.model.GitUser;

/**
 * Description: 仓库协作用户
 * Created by GW00295392 on 2024/3/4
 */
public class Collaborator extends GitUser {

    private Integer id;

    private String login;

    private String name;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return  login + '|' + name;
    }
}
