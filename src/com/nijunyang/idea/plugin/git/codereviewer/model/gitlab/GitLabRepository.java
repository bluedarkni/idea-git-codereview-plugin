package com.nijunyang.idea.plugin.git.codereviewer.model.gitlab;

import com.nijunyang.idea.plugin.git.codereviewer.model.GitRepository;

/**
 * Description:
 * Created by nijunyang on 2022/1/28 11:46
 */
public class GitLabRepository extends GitRepository {

    private Integer id;
    private String description;
    private String name;
    private String ssh_url_to_repo;
    private String http_url_to_repo;
    private String web_url;

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


    public String getSsh_url_to_repo() {
        return ssh_url_to_repo;
    }

    public void setSsh_url_to_repo(String ssh_url_to_repo) {
        this.ssh_url_to_repo = ssh_url_to_repo;
    }

    public String getHttp_url_to_repo() {
        return http_url_to_repo;
    }

    public void setHttp_url_to_repo(String http_url_to_repo) {
        this.http_url_to_repo = http_url_to_repo;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }
}
