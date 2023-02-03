package com.tyy.uml.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.tyy.uml.comm.JFrameProc;

public class UMLFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    JFrameProc frameProc;

    TitleBar titleBar;

    private final JPanel stagePanel;

    public UMLFrame() {
        frameProc = new JFrameProc();
        titleBar = new TitleBar(this, frameProc.getJFrameParameters());
        stagePanel = new JPanel();
        stagePanel.setOpaque(false);
        stagePanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        stagePanel.setLayout(new BorderLayout());
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

}
