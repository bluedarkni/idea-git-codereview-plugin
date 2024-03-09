package com.nijunyang.idea.plugin.git.codereviewer.view;

import cn.hutool.http.HttpResponse;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.nijunyang.idea.plugin.git.codereviewer.model.*;
import com.nijunyang.idea.plugin.git.codereviewer.model.gitee.Collaborator;
import com.nijunyang.idea.plugin.git.codereviewer.model.gitee.GiteeConstant;
import com.nijunyang.idea.plugin.git.codereviewer.model.gitee.GiteeIssueBody;
import com.nijunyang.idea.plugin.git.codereviewer.model.gitee.GiteeRepository;
import com.nijunyang.idea.plugin.git.codereviewer.model.github.GitHubCollaborator;
import com.nijunyang.idea.plugin.git.codereviewer.model.github.GitHubConstant;
import com.nijunyang.idea.plugin.git.codereviewer.model.github.GitHubIssueBody;
import com.nijunyang.idea.plugin.git.codereviewer.model.gitlab.GitLabConstant;
import com.nijunyang.idea.plugin.git.codereviewer.model.gitlab.GitLabIssueBody;
import com.nijunyang.idea.plugin.git.codereviewer.utils.HttpUtil;

import javax.swing.*;
import java.awt.*;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Description:
 * Created by nijunyang on 2022/1/27 15:14
 */
public class CodeReviewUI<T extends GitUser> {

    private static final String TITLE = "Create Issue";

    private static volatile JDialog DIALOG;

    private JPanel mainPanel;
    /**
     * 文件名
     */
    private JTextField fileNameText;
    /**
     * 代码信息
     */
    private JTextArea codeContentArea;
    /**
     * issue信息
     */
    private JTextArea issueArea;
    /**
     * 意见接收人
     */
    private JComboBox<T> receiverComboBox;

    private JButton confirmButton;
    private JButton cancelButton;
    /**
     * issue 标题
     */
    private JTextField issueTitleTextField;
    /**
     * 代码行信息
     */
    private JTextField codeLineRange;

    public static void showIssueDialog(EventInfo<? extends GitUser> eventInfo, Project project) {
        AnActionEvent event = eventInfo.getEvent();
        Token token = eventInfo.getToken();
        GitRepository gitRepository = eventInfo.getGitRepository();
        if (DIALOG == null) {
            DIALOG = new JDialog();
        }
        DIALOG.setVisible(false);
        // 获取 IDEA 主窗口
        Frame mainWindow = WindowManager.getInstance().getFrame(project);
        // 获取 IDEA 主窗口所在屏幕信息
        GraphicsConfiguration gc = mainWindow.getGraphicsConfiguration();
        Rectangle bounds = gc.getBounds();
        // 计算 JDialog 的位置和大小
        int dialogWidth = bounds.width / 2;
        int dialogHeight = bounds.height / 2;
        int dialogX = bounds.x + (bounds.width - dialogWidth) / 2;
        int dialogY = bounds.y + (bounds.height - dialogHeight) / 2;

        DIALOG.setSize(dialogWidth, dialogHeight);
        DIALOG.setLocation(dialogX, dialogY);

//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        int width = screenSize.width / 2;
//        int height = (screenSize.height / 2);
//        int w = (screenSize.width - width) / 2;
//        int h = (screenSize.height - height) / 2;
//        DIALOG.setLocation(w, h);
//        DIALOG.setSize(width, height);
        try {
            DIALOG.setTitle(TITLE);
            CodeReviewUI<GitUser> codeReviewUI = new CodeReviewUI<>();

            //获取当前操作的类文件
            PsiFile psiFile = event.getData(CommonDataKeys.PSI_FILE);
            if (Objects.nonNull(psiFile.getVirtualFile())) {
                String classPath = psiFile.getVirtualFile().getName();
                if (psiFile instanceof PsiJavaFile) {
                    //java带包
                    classPath = ((PsiJavaFile)psiFile).getPackageName() + "." + classPath;
                }
                codeReviewUI.fileNameText.setText(classPath);
            }
            //获取选中的代码信息
            Editor data = event.getData(CommonDataKeys.EDITOR);
            if (Objects.nonNull(data)) {
                SelectionModel selectionModel = data.getSelectionModel();
                String selectedText = selectionModel.getSelectedText();
                codeReviewUI.codeContentArea.setText(selectedText);
                Document document = data.getDocument();
                int start = document.getLineNumber(selectionModel.getSelectionStart());
                int end = document.getLineNumber(selectionModel.getSelectionEnd());
                codeReviewUI.codeLineRange.setText(start + "-" + end);
            }
            codeReviewUI.receiverComboBox.setModel(new DefaultComboBoxModel(eventInfo.getUsers()));

            codeReviewUI.confirmButton.addActionListener(e -> {
                GitUser receiver = (GitUser) codeReviewUI.receiverComboBox.getSelectedItem();
                String description = codeReviewUI.buildDescription(
                        codeReviewUI.issueArea,
                        codeReviewUI.fileNameText,
                        codeReviewUI.codeContentArea,
                        codeReviewUI.codeLineRange);
                if (token.getChannel() == Channel.GIT_LAB) {
                    gitLabIssue(eventInfo, token, gitRepository, codeReviewUI.issueTitleTextField.getText(), receiver, description);
                } else if (token.getChannel() == Channel.GIT_EE) {
                    giteeIssue(eventInfo, token, gitRepository, codeReviewUI.issueTitleTextField.getText(), receiver, description);
                } else if (token.getChannel() == Channel.GIT_HUB) {
                    githubIssue(eventInfo, token, codeReviewUI.issueTitleTextField.getText(), receiver, description);
                }
                DIALOG.dispose();
            });
            codeReviewUI.cancelButton.addActionListener(e -> {
                DIALOG.dispose();
            });

            DIALOG.setContentPane(codeReviewUI.mainPanel);
            DIALOG.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            DIALOG.pack();
            DIALOG.setVisible(true);
        } catch (Exception e) {
            DIALOG.dispose();
        }
    }

    private static void githubIssue(EventInfo<? extends GitUser> eventInfo,
                                    Token token, String title,
                                    GitUser receiver, String description) {

        GitHubCollaborator gitHubCollaborator = (GitHubCollaborator) receiver;
        GitHubIssueBody gitHubIssueBody = new GitHubIssueBody();
        gitHubIssueBody.setTitle(title);
        gitHubIssueBody.setBody(description);
        gitHubIssueBody.setAssignees(Arrays.asList(gitHubCollaborator.getLogin()));
//        gitHubIssueBody.setLabels(Arrays.asList("BUG"));
        String domain = eventInfo.getDomain();
        String apiDomain = token.getApiDomain();
        if (apiDomain != null && apiDomain.length() == 0) {
            domain = apiDomain;
        }
        LocalRepositoryInfo localRepositoryInfo = eventInfo.getLocalRepositoryInfo();
        String issueUrl = HttpUtil.HTTPS_PROTOCOL + domain
                + MessageFormat.format(GitHubConstant.ISSUE_URL, localRepositoryInfo.getGroupPath(), localRepositoryInfo.getName());
        Map<String, String> headers = new HashMap<>();
        headers.put(GitHubConstant.AUTHORIZATION, GitHubConstant.BEARER + token.getPrivateKey());
//        headers.put("Content-Type", "application/json;charset=UTF-8");
//        headers.put("Content-Type", "application/vnd.github+json");
        HttpResponse httpResponse = HttpUtil.postWithHeader(issueUrl, gitHubIssueBody, headers);
        checkResponse(httpResponse, issueUrl, eventInfo.getEvent().getProject());
    }

    private static void gitLabIssue(EventInfo<? extends GitUser> eventInfo, Token token,
                                    GitRepository gitRepository, String title,
                                    GitUser receiver, String description) {
        Integer userId = null;
        String userIds = null;
        if (Objects.nonNull(receiver)) {
            userId = receiver.getId();
            userIds = userId.toString();
        }
        GitLabIssueBody gitLabIssueBody =
                new GitLabIssueBody(userId, userIds, description, title);
        String issueUrl = HttpUtil.HTTPS_PROTOCOL + eventInfo.getDomain()
                + MessageFormat.format(GitLabConstant.ISSUE_URL, gitRepository.getId());
        Map<String, String> headers = new HashMap<>();
        headers.put(GitLabConstant.HEADER_PRIVATE_TOKEN, token.getPrivateKey());

        headers.put("Content-Type", "application/json;charset=UTF-8");
        HttpResponse httpResponse = HttpUtil.postWithHeader(issueUrl, gitLabIssueBody, headers);
        checkResponse(httpResponse, issueUrl, eventInfo.getEvent().getProject());
    }

    private static void giteeIssue(EventInfo<? extends GitUser> eventInfo, Token token, GitRepository gitRepository,
                                   String title, GitUser receiver, String description) {
        Collaborator collaborator = (Collaborator) receiver;
        GiteeRepository giteeRepository = (GiteeRepository) gitRepository;
        LocalRepositoryInfo localRepositoryInfo = eventInfo.getLocalRepositoryInfo();
        String issueUrl = HttpUtil.HTTPS_PROTOCOL + eventInfo.getDomain()
                + MessageFormat.format(GiteeConstant.ISSUE_URL, localRepositoryInfo.getGroupPath());
        GiteeIssueBody giteeIssueBody = new GiteeIssueBody();
        giteeIssueBody.setAccess_token(token.getPrivateKey());
        giteeIssueBody.setTitle(title);
        giteeIssueBody.setBody(description);
        giteeIssueBody.setAssignee(collaborator.getLogin());
        giteeIssueBody.setRepo(giteeRepository.getPath());
        HttpResponse httpResponse = HttpUtil.postWithNoHeader(issueUrl, giteeIssueBody);
        checkResponse(httpResponse, issueUrl, eventInfo.getEvent().getProject());
    }

    private static void checkResponse(HttpResponse httpResponse, String url, Project project) {
        Frame mainWindow = WindowManager.getInstance().getFrame(project);
        if (httpResponse.getStatus() >= 300) {
            JOptionPane.showMessageDialog(mainWindow,
                    "异常码：" + httpResponse.getStatus()
                            + System.lineSeparator() + url
                            + System.lineSeparator() + "message:" + httpResponse.body(),
                    "警告", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(mainWindow,
                    "Successfully",
                    "issue", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * 构造更详细的信息
     * @param issueArea
     * @param fileNameText
     * @param codeContentArea
     * @param codeLineRange
     * @return
     */
    private String buildDescription(JTextArea issueArea,
                                    JTextField fileNameText, JTextArea codeContentArea, JTextField codeLineRange) {

        StringBuilder sb = new StringBuilder("**file:** ").append("  <br />  ").append(fileNameText.getText()).append("  <br />  ")
                .append("**line range:** ").append("  <br />  ").append(codeLineRange.getText()).append("  <br />  ")
                .append("**code:** ").append("  <br />").append("\n```java\n")
                .append(codeContentArea.getText()).append("\n```\n")
                .append("**issue:** ").append("  <br />  ").append(issueArea.getText());
        return sb.toString();

    }

}
