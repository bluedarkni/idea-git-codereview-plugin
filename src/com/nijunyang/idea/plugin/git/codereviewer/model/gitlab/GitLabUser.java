package com.nijunyang.idea.plugin.git.codereviewer.model.gitlab;

import com.nijunyang.idea.plugin.git.codereviewer.model.GitUser;

/**
 * Description:
 * Created by nijunyang on 2022/1/28 11:46
 */
public class GitLabUser extends GitUser {

    private Integer id;

    private String name;
    /**
     * 账户名
     */
    private String username;
    /**
     * active 激活
     */
    private String state;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return  name + '|' + username;
    }
}
