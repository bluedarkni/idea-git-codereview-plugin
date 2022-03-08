package com.nijunyang.idea.plugin.git.codereviewer.view;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.nijunyang.idea.plugin.git.codereviewer.model.Channel;
import com.nijunyang.idea.plugin.git.codereviewer.model.EventInfo;
import com.nijunyang.idea.plugin.git.codereviewer.model.GitUser;
import com.nijunyang.idea.plugin.git.codereviewer.model.Token;
import com.nijunyang.idea.plugin.git.codereviewer.model.gitlab.GitLabConstant;
import com.nijunyang.idea.plugin.git.codereviewer.model.gitlab.GitLabIssueBody;
import com.nijunyang.idea.plugin.git.codereviewer.utils.HttpUtil;

import javax.swing.*;
import java.awt.*;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Description:
 * Created by nijunyang on 2022/1/27 15:14
 */
public class CodeReviewUI<T extends GitUser> {

    private static final String TITLE = "Create Issue";
    private static final int WIDTH = 550;
    private static final int HEIGHT = 600;

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

    private JButton commitButton;
    private JButton cancelButton;
    /**
     * issue 标题
     */
    private JTextField issueTitleTextField;
    /**
     * 代码行信息
     */
    private JTextField codeLineRange;

    public static void showIssueDialog(EventInfo eventInfo) {
        AnActionEvent event = eventInfo.getEvent();
        Token token = eventInfo.getToken();
        JDialog dialog = new JDialog();
        try {
            dialog.setTitle(TITLE);
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
            codeReviewUI.receiverComboBox.setModel(new DefaultComboBoxModel<>(eventInfo.getUsers()));

            codeReviewUI.commitButton.addActionListener(e -> {
                if (token.getChannel() == Channel.GIT_LAB) {
                    GitUser gitUser = (GitUser) codeReviewUI.receiverComboBox.getSelectedItem();
                    Integer userId = null;
                    String userIds = null;
                    if (Objects.nonNull(gitUser)) {
                        userId = gitUser.getId();
                        userIds = userId.toString();
                    }
                    String description = codeReviewUI.buildDescription(
                            codeReviewUI.issueArea,
                            codeReviewUI.fileNameText,
                            codeReviewUI.codeContentArea,
                            codeReviewUI.codeLineRange);
                    GitLabIssueBody gitLabIssueBody =
                            new GitLabIssueBody(userId, userIds, description, codeReviewUI.issueTitleTextField.getText());
                    String issueUrl = "https://" + eventInfo.getDomain()
                            + MessageFormat.format(GitLabConstant.ISSUE_URL, eventInfo.getGitProject().getId());
                    Map<String, String> headers = new HashMap<>();
                    headers.put(GitLabConstant.HEADER_PRIVATE_TOKEN, token.getPrivateKey());

                    headers.put("Content-Type", "application/json;charset=UTF-8");
                    HttpUtil.postWithHeader(issueUrl, gitLabIssueBody, headers);
                }
                dialog.dispose();
            });
            codeReviewUI.cancelButton.addActionListener(e -> {
                dialog.dispose();
            });

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int w = (screenSize.width - WIDTH) / 2;
            int h = (screenSize.height - HEIGHT) / 2;
            dialog.setLocation(w, h);

            dialog.setContentPane(codeReviewUI.mainPanel);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.pack();
            dialog.setVisible(true);
        } catch (Exception e) {
            dialog.dispose();
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
