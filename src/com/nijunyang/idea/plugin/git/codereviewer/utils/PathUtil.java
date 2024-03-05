package com.nijunyang.idea.plugin.git.codereviewer.utils;

import cn.hutool.core.io.FileUtil;
import com.intellij.openapi.project.Project;
import com.nijunyang.idea.plugin.git.codereviewer.model.LocalRepositoryInfo;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Description: 
 * Created by nijunyang on 2022/1/28 10:01
 */
public final class PathUtil {

    private PathUtil() {
    }

    /**
     * 从项目.git目录下config文件中获取项目地址，以便解析域名和项目名
     * @return
     */
    public static LocalRepositoryInfo localRepositoryInfo(Project project) {
        String projectPath = project.getBasePath();
        String configPath = PathUtil.combinePath(projectPath, ".git", "config");
        List<String> stringList = FileUtil.readLines(configPath, Charset.defaultCharset());

        for (String msg : stringList) {
            if (msg.trim().startsWith("url")) {
                String[] split = msg.split("=");
                String url = split[split.length - 1].trim();

                LocalRepositoryInfo localRepositoryInfo = new LocalRepositoryInfo();
                localRepositoryInfo.setUrl(url);
                localRepositoryInfo.setName(project.getName());
                //https url  和 ssh url
                if (url.startsWith(HttpUtil.HTTPS_PROTOCOL)) {
                    String domain = UrlUtil.parseDomainName(url);
                    localRepositoryInfo.setDomain(domain);

                    String[] split1 = url.split("domain");
                    int lastIndex = split1[1].lastIndexOf("/");
                    localRepositoryInfo.setGroupPath(split1[1].substring(1, lastIndex));
                } else if (url.startsWith("git@")) {
                    String[] split1 = url.split("@");
                    String[] split2 = split1[1].split(":");
                    localRepositoryInfo.setDomain(split2[0]);
                    int lastIndex = split2[1].lastIndexOf("/");
                    localRepositoryInfo.setGroupPath(split2[1].substring(0, lastIndex));
                }
                return localRepositoryInfo;
            }
        }

        throw new RuntimeException("not find git config");
    }

    public static String combinePath(String...args){
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("path is empty.");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]);
            if (i == args.length - 1) {
                break;
            }
            sb.append(File.separator);
        }
        return sb.toString();
    }
}
