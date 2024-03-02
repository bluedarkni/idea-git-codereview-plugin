package com.nijunyang.idea.plugin.git.codereviewer.model.gitlab;

import com.nijunyang.idea.plugin.git.codereviewer.model.GitProject;

/**
 * Description:
 * Created by nijunyang on 2022/1/28 11:46
 */
public class GitLabProject extends GitProject {

    private Integer id;
    private String description;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
