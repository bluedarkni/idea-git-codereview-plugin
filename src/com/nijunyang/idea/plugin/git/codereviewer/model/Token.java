package com.nijunyang.idea.plugin.git.codereviewer.model;


/**
 * Description:
 * Created by nijunyang on 2022/1/28 11:40
 */
public class Token {

    private Channel channel;

    private String privateKey;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
