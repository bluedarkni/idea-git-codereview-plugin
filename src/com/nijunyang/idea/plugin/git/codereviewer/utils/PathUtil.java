package com.nijunyang.idea.plugin.git.codereviewer.utils;

import cn.hutool.core.io.FileUtil;
import com.intellij.openapi.project.Project;

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
    public static String loadProjectUrl(Project project) {
        String projectPath = project.getBasePath();
        String configPath = PathUtil.combinePath(projectPath, ".git", "config");
        List<String> stringList = FileUtil.readLines(configPath, Charset.defaultCharset());
        for (String msg : stringList) {
            if (msg.trim().startsWith("url")) {
                String[] split = msg.split("=");
                return split[split.length - 1].trim();
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
