package com.nijunyang.idea.plugin.git.codereviewer.view;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import com.nijunyang.idea.plugin.git.codereviewer.action.CodeReviewAction;
import com.nijunyang.idea.plugin.git.codereviewer.model.Channel;
import com.nijunyang.idea.plugin.git.codereviewer.model.Token;
import com.nijunyang.idea.plugin.git.codereviewer.utils.TokenUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;
import java.util.Objects;


/**
 * Description:
 * Created by nijunyang on 2022/2/8 10:33
 */
public class TokenConfigUI {
    private static final String TITLE = "Setting Token";
    private static volatile JDialog DIALOG;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 130;
    private JPanel mainPanel;
    private JRadioButton gitHub;
    private JRadioButton gitLab;
    private JRadioButton gitee;
    private JTextField accessToken;
    private JButton confirmButton;
    private JButton cancelButton;

    public static void setToken(Project project) {

        if (DIALOG == null) {
            DIALOG = new JDialog();
        }
        DIALOG.setVisible(false);
        try {
            DIALOG.setTitle(TITLE);
            TokenConfigUI tokenConfigUI = new TokenConfigUI();
            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(tokenConfigUI.gitHub);
            buttonGroup.add(tokenConfigUI.gitLab);
            tokenConfigUI.gitLab.setSelected(true);
            buttonGroup.add(tokenConfigUI.gitee);
            tokenConfigUI.confirmButton.addActionListener(e -> {
                String key = tokenConfigUI.accessToken.getText();
                Token token = new Token();
                token.setPrivateKey(key);
                String choice  = getSelected(buttonGroup);
                if (Objects.nonNull(choice) && Objects.nonNull(key)) {
                    Channel channel = Channel.getByAliasName(choice);
                    token.setChannel(channel);
                    CodeReviewAction.tokenMap.put(project.getLocationHash(), token);
                    TokenUtil.serialToken(project, token);
                    DIALOG.dispose();
                }
            });
            tokenConfigUI.cancelButton.addActionListener(e -> {
                DIALOG.dispose();
            });
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

            DIALOG.setContentPane(tokenConfigUI.mainPanel);
            DIALOG.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            DIALOG.pack();
            DIALOG.setVisible(true);
        } catch (Exception e) {
            DIALOG.dispose();
        }
    }

    public static String getSelected(ButtonGroup buttonGroup) {
        Enumeration<AbstractButton> elements = buttonGroup.getElements();
        while (elements.hasMoreElements()) {
            AbstractButton abstractButton = elements.nextElement();
            if(abstractButton.isSelected()){
                return abstractButton.getText();
            }
        }
        return null;
    }
}
