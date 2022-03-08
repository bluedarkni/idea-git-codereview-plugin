package com.nijunyang.idea.plugin.git.codereviewer.view;

import com.intellij.openapi.project.Project;
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
    private static final int WIDTH = 500;
    private static final int HEIGHT = 130;
    private JPanel mainPanel;
    private JRadioButton gitHub;
    private JRadioButton gitLab;
    private JRadioButton gitee;
    private JTextField accessToken;
    private JButton commitButton;
    private JButton cancelButton;

    public static void setToken(Project project) {

        JDialog dialog = new JDialog();
        try {
            dialog.setTitle(TITLE);
            TokenConfigUI tokenConfigUI = new TokenConfigUI();
            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(tokenConfigUI.gitHub);
            buttonGroup.add(tokenConfigUI.gitLab);
            buttonGroup.add(tokenConfigUI.gitee);
            tokenConfigUI.commitButton.addActionListener(e -> {
                String key = tokenConfigUI.accessToken.getText();
                Token token = new Token();
                token.setPrivateKey(key);
                String choice  = getSelected(buttonGroup);
                if (Objects.nonNull(choice) && Objects.nonNull(key)) {
                    Channel channel = Channel.getByAliasName(choice);
                    token.setChannel(channel);
                    CodeReviewAction.tokenMap.put(project.getLocationHash(), token);
                    TokenUtil.serialToken(project, token);
                    dialog.dispose();
                }
            });
            tokenConfigUI.cancelButton.addActionListener(e -> {
                dialog.dispose();
            });

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int w = (screenSize.width - WIDTH) / 2;
            int h = (screenSize.height - HEIGHT) / 2;
            dialog.setLocation(w, h);

            dialog.setContentPane(tokenConfigUI.mainPanel);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.pack();
            dialog.setVisible(true);
        } catch (Exception e) {
            dialog.dispose();
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
