package com.nijunyang.idea.plugin.git.codereviewer.utils;

import java.net.URI;

/**
 * Description:
 * Created by nijunyang on 2022/1/27 18:06
 */
public final class UrlUtil {

    private UrlUtil() {
    }

    public static String parseDomainName (String url) {
        String domain = "";
        try {
            URI uri = new URI(url);
            domain = uri.getHost();
        } catch (Exception e) {
            //ignore
        }
        return domain;
    }

    public static String parseProjectName (String url) {
        //https://gitlab.com/aaa/bbb/ccc.git
        String[] splits = url.split("/");
        String[] lastSplits = splits[splits.length - 1].split("\\.");
        return lastSplits[0];
    }

}
