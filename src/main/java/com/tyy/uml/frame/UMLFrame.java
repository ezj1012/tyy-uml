package com.tyy.uml.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.tyy.uml.bean.BeanHelper.BeanObservale;
import com.tyy.uml.bean.UMLConfig;
import com.tyy.uml.comm.JFrameProc;
import com.tyy.uml.main.UMLMainPane;
import com.tyy.uml.util.SWUtils;

public class UMLFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    JFrameProc frameProc;

    TitleBar titleBar;

    private final UMLMainPane stagePanel;

    public UMLFrame() {
        frameProc = new JFrameProc();
        titleBar = new TitleBar(this, frameProc.getJFrameParameters());
        stagePanel = new UMLMainPane(this, new File("./uml.cfg"));
        this.getContentPane().setBackground(new Color(0, 0, 0, 0));
        this.getContentPane().add(titleBar, BorderLayout.NORTH);
        this.getContentPane().add(stagePanel, BorderLayout.CENTER);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.addWindowStateListener(new WindowAdapter() {

            @Override
            public void windowStateChanged(WindowEvent e) {
                Container contentPane = getContentPane();
                JComponent cntPane = null;
                if (!(contentPane instanceof JComponent)) { return; }
                cntPane = (JComponent) contentPane;

                if (e.getNewState() == MAXIMIZED_BOTH) {
                    final int padding = 8;
                    cntPane.setBorder(new EmptyBorder(padding, padding, padding, padding));
                } else if (e.getOldState() == MAXIMIZED_BOTH) {
                    cntPane.setBorder(new EmptyBorder(0, 0, 0, 0));
                }
            }

        });
    }

    public TitleBar getTitleBar() {
        return titleBar;
    }

    /**
     * @deprecated {@link #doVisible()}
     */
    @Deprecated
    public void setVisible(boolean b) {
        super.setVisible(b);
    }

    public void doVisible() {
        String titleColor = this.stagePanel.getCfg().getEditorTitleBackColor();
        this.titleBar.refreshVisible(SWUtils.decodeColor(titleColor, UMLConfig.c333333));
        this.pack();
        super.setVisible(true);
        this.frameProc.init(this);
    }

}
