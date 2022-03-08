package com.nijunyang.idea.plugin.git.codereviewer.utils;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.intellij.openapi.project.Project;
import com.nijunyang.idea.plugin.git.codereviewer.model.Token;

/**
 * Description:
 * Created by nijunyang on 2022/2/8 14:16
 */
public final class TokenUtil {

    private TokenUtil() {
    }

    public static Token checkOrLoadToken(Project project) {
        String projectPath = project.getBasePath();
        String codeReviewConfigPath = PathUtil.combinePath(projectPath, ".idea", "code_review_config");
        if (FileUtil.exist(codeReviewConfigPath)) {
            String s = FileUtil.readUtf8String(codeReviewConfigPath);
            return JSON.parseObject(s, Token.class);
        }
        return null;
    }

    public static void serialToken(Project project, Token token) {
        String projectPath = project.getBasePath();
        String codeReviewConfigPath = PathUtil.combinePath(projectPath, ".idea", "code_review_config");
        String s = JSON.toJSONString(token);
        FileUtil.writeUtf8String(s, codeReviewConfigPath);
    }

}
